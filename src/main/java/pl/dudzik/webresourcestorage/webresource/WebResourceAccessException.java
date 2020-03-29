package pl.dudzik.webresourcestorage.webresource;

import java.sql.SQLException;

class WebResourceAccessException extends RuntimeException {
    WebResourceAccessException(SQLException ex) {
        super("An error occurred while accessing web resource entity", ex);
    }
}
