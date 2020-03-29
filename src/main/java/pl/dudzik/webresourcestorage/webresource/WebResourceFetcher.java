package pl.dudzik.webresourcestorage.webresource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebResourceFetcher {

    private final WebResourceRepository webResourceRepository;

    List<WebResourceMeta> getResourcesMetadata() {
        return webResourceRepository.getWebResourcesMetadata();
    }

    List<WebResourceMeta> getResourcesMetadataWithUrlLike(String searchedResource) {
        return webResourceRepository.getWebResourcesMetadataLike(searchedResource);
    }

    WebResource getResource(UUID resourceId) {
        return webResourceRepository.getResource(resourceId).orElseThrow(() -> new ResourceNotFound(resourceId));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class ResourceNotFound extends RuntimeException {
        ResourceNotFound(UUID resourceId) {
            super(String.format("Resource with id: %s not found", resourceId));
        }
    }
}
