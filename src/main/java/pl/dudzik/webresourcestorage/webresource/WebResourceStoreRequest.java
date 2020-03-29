package pl.dudzik.webresourcestorage.webresource;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class WebResourceStoreRequest {
    @NotBlank
    private String resourceUrl;
}
