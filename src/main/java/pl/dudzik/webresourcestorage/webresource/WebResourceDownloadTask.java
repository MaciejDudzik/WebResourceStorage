package pl.dudzik.webresourcestorage.webresource;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

@Slf4j
public class WebResourceDownloadTask implements Callable<WebResourceDownloadResult> {

    private final URL resourceUrl;
    private final ResourceStore resourceStore;

    WebResourceDownloadTask(URL resourceUrl, ResourceStore resourceStore) {
        this.resourceUrl = resourceUrl;
        this.resourceStore = resourceStore;
    }

    @Override
    public WebResourceDownloadResult call() throws Exception {
        log.info("Fetching of web resource [url: {}] started", resourceUrl);
        try {
            URLConnection conn = resourceUrl.openConnection();
            String contentType = conn.getContentType().split(";")[0];
            resourceStore.storeResource(resourceUrl.toString(), conn.getInputStream(), contentType);
            return WebResourceDownloadResult.SUCCESS;
        } catch (IOException e) {
            return WebResourceDownloadResult.RESOURCE_CONNECTION_FAILURE;
        }
    }
}
