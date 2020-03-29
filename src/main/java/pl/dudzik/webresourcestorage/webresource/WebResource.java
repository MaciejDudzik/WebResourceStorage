package pl.dudzik.webresourcestorage.webresource;

import lombok.Value;

import java.io.InputStream;

@Value
class WebResource {
    InputStream resource;
    WebResourceMeta webResourceMeta;
}
