package pl.dudzik.webresourcestorage.webresource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotBlank;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebResourceStorage {

    private final ExecutorService executor;
    private final WebResourceRepository webResourceRepository;
    private final WebResourceDownloadResultHandler fetchResultHandler;

    void storeResource(@NotBlank String resourceUrl) {
        final URL url = toUrl(resourceUrl);
        final Future<WebResourceDownloadResult> fetchFuture = executor.submit(new WebResourceDownloadTask(url, webResourceRepository));
        try {
            fetchResultHandler.handleResult(fetchFuture.get(), resourceUrl);
        } catch (InterruptedException | ExecutionException e) {
            throw new WebResourceDownloadFailedException(e);
        }
    }

    private URL toUrl(String resourceUrl) {
        try {
            return new URL(resourceUrl);
        } catch (MalformedURLException e) {
            throw new MalformedUrlGivenException(resourceUrl);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class MalformedUrlGivenException extends RuntimeException {
        MalformedUrlGivenException(String url) {
            super(String.format("Given url [%s] is malformed", url));
        }
    }

    public class WebResourceDownloadFailedException extends RuntimeException {
        public WebResourceDownloadFailedException(Exception ex) {
            super("Web resource storing failed", ex);
        }
    }
}
