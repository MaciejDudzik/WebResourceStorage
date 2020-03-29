package pl.dudzik.webresourcestorage.webresource;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;

@Component
@RequiredArgsConstructor
public class HttpHeadersResolver {

    HttpHeaders resolveHeaders(WebResourceMeta resourceMeta) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.asMediaType(MimeType.valueOf(resourceMeta.getContentType())));
        headers.setContentLength(resourceMeta.getContentLength());
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename(WebResourceFileUtils.getResourceFileName(resourceMeta)).build());
        return headers;
    }
}
