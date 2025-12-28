package com.github.config;

import com.github.models.config.YamlConfig;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
public class Config {
    public static final String GITHUB_API_URL;
    public static final Boolean LOCAL;
    public static final String ACCESS_TOKEN;

    static {
        try {
            // parse .yaml file
            Path path = Paths.get("src", "main", "resources", "environments", "staging.yaml");
            InputStream inputStream = Files.newInputStream(path);
            YamlConfig configuration = new Yaml().loadAs(inputStream, YamlConfig.class);

            // parse .env file
            Dotenv env = Dotenv.configure().directory(".").ignoreIfMissing().load();

            // initialize constants
            GITHUB_API_URL = configuration.getBaseURL();
            LOCAL = configuration.getLocal();
            ACCESS_TOKEN = Optional.ofNullable(env.get("ACCESS_TOKEN")).orElse(System.getProperty("ACCESS_TOKEN"));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }
}
