package pl.dudzik.webresourcestorage.webresource;

import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

class WebResourceFileUtils {

    private WebResourceFileUtils() {
    }

    static String getResourceFileName(WebResourceMeta resourceMeta) {
        return resourceMeta.getResourceId().toString().concat(getResourceFileExtension(resourceMeta));
    }

    static String getResourceFileExtension(WebResourceMeta resourceMeta) {
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        try {
            return allTypes.forName(resourceMeta.getContentType()).getExtension();
        } catch (MimeTypeException e) {
            return "";
        }
    }
}
