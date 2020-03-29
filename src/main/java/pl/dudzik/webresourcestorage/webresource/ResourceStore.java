package pl.dudzik.webresourcestorage.webresource;

import java.io.InputStream;

public interface ResourceStore {

    void storeResource(String url, InputStream is, String contentType);
}
