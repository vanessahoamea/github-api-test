package com.github.models.config;

import lombok.Data;

@Data
public class YamlConfig {
    private String version;
    private String released;
    private String baseURL;
    private Boolean local;
}
