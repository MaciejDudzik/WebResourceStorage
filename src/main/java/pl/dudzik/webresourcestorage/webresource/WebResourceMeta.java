package pl.dudzik.webresourcestorage.webresource;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class WebResourceMeta {
    private final UUID resourceId;
    private final String url;
    private final OffsetDateTime date;
    private final String contentType;
    private final int contentLength;
}
