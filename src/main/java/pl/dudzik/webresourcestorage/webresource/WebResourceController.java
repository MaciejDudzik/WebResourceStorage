package pl.dudzik.webresourcestorage.webresource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class WebResourceController {

    private final WebResourceStorage webResourceStorage;
    private final WebResourceFetcher webResourceFetcher;
    private final HttpHeadersResolver headersResolver;

    @PostMapping("/store")
    public void storeResource(@RequestBody @Valid WebResourceStoreRequest request) {
        log.info("Resource [url: {}] requested to be stored", request.getResourceUrl());
        webResourceStorage.storeResource(request.getResourceUrl());
    }

    @GetMapping
    public List<WebResourceMeta> getResourcesMetadata() {
        log.info("Get all resources meta requested");
        return webResourceFetcher.getResourcesMetadata();
    }

    @GetMapping(params = "like")
    public List<WebResourceMeta> searchResourcesMetadata(@RequestParam("like") String searchedResource) {
        log.info("Get all resources meta like [searchedResource: {}] requested", searchedResource);
        return webResourceFetcher.getResourcesMetadataWithUrlLike(searchedResource);
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<Resource> getResource(@PathVariable UUID resourceId) {
        log.info("Get resource [id: {}] requested", resourceId);
        final WebResource webResource = webResourceFetcher.getResource(resourceId);
        return ResponseEntity.status(OK)
                .headers(headersResolver.resolveHeaders(webResource.getWebResourceMeta()))
                .body(new InputStreamResource(webResource.getResource()));
    }
}
