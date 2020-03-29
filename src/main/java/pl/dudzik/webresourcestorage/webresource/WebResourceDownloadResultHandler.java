package pl.dudzik.webresourcestorage.webresource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

import static pl.dudzik.webresourcestorage.webresource.WebResourceDownloadResult.RESOURCE_CONNECTION_FAILURE;
import static pl.dudzik.webresourcestorage.webresource.WebResourceDownloadResult.SUCCESS;

@Slf4j
@Component
class WebResourceDownloadResultHandler {

    private final Map<WebResourceDownloadResult, Consumer<String>> storeResultStrategyMap;

    WebResourceDownloadResultHandler() {
        storeResultStrategyMap = new EnumMap<>(WebResourceDownloadResult.class);
        storeResultStrategyMap.put(SUCCESS, url -> log.info("Web resource [url: {}] stored.", url));
        storeResultStrategyMap.put(RESOURCE_CONNECTION_FAILURE, url -> {
            throw new WebResourceConnectionException(url);
        });
    }

    void handleResult(WebResourceDownloadResult result, String fetchedResourceUrl) {
        storeResultStrategyMap.get(result).accept(fetchedResourceUrl);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private static class WebResourceConnectionException extends RuntimeException {
        WebResourceConnectionException(String url) {
            super(String.format("Could not successfully open connection with given url [%s]", url));
        }
    }
}
