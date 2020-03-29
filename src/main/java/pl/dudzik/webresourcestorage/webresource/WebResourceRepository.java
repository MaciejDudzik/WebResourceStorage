package pl.dudzik.webresourcestorage.webresource;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WebResourceRepository implements ResourceStore {

    private final DataSource dataSource;

    @Override
    public void storeResource(String url, InputStream is, String contentType) {
        createWebResource(url, is, contentType);
    }

    void createWebResource(String url, InputStream is, String contentType) {
        String sql = "INSERT INTO web_resource (url, content, content_type) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, url);
            ps.setBinaryStream(2, is);
            ps.setString(3, contentType);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new WebResourceAccessException(ex);
        }
    }

    Optional<WebResource> getResource(UUID resourceId) {
        String sql = "SELECT id, url, date, content_type, octet_length(content) AS size, content " +
                "FROM web_resource " +
                "WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, resourceId);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    final WebResourceMeta webResourceMeta = WebResourceMapper.mapResultSet(resultSet);
                    return Optional.of(new WebResource(resultSet.getBinaryStream("content"), webResourceMeta));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException ex) {
            throw new WebResourceAccessException(ex);
        }
    }

    List<WebResourceMeta> getWebResourcesMetadataLike(String searchedResource) {
        String sql = "SELECT id, url, date, content_type, octet_length(content) AS size " +
                "FROM web_resource " +
                "WHERE url LIKE ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, String.format("%%%s%%", searchedResource));
            try (ResultSet resultSet = ps.executeQuery()) {
                List<WebResourceMeta> webResourcesMetadataList = new ArrayList<>();
                while (resultSet.next()) {
                    webResourcesMetadataList.add(WebResourceMapper.mapResultSet(resultSet));
                }
                return webResourcesMetadataList;
            }
        } catch (SQLException ex) {
            throw new WebResourceAccessException(ex);
        }
    }


    List<WebResourceMeta> getWebResourcesMetadata() {
        String sql = "SELECT id, url, date, content_type, octet_length(content) AS size FROM web_resource";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {
            List<WebResourceMeta> webResourcesMetadataList = new ArrayList<>();
            while (resultSet.next()) {
                webResourcesMetadataList.add(WebResourceMapper.mapResultSet(resultSet));
            }
            return webResourcesMetadataList;
        } catch (SQLException ex) {
            throw new WebResourceAccessException(ex);
        }
    }

    private static class WebResourceMapper {
        static WebResourceMeta mapResultSet(ResultSet resultSet) throws SQLException {
            return new WebResourceMeta(
                    UUID.fromString(resultSet.getString("id")),
                    resultSet.getString("url"),
                    OffsetDateTime.ofInstant(resultSet.getTimestamp("date").toInstant(), TimeZone.getDefault().toZoneId()),
                    resultSet.getString("content_type"),
                    resultSet.getInt("size"));
        }
    }
}
