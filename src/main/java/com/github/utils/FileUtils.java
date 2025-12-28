package com.github.utils;

import java.io.File;
import java.nio.file.Paths;

public class FileUtils {
    private static final String SOURCE_FOLDER = "src";
    private static final String SCHEMAS_FOLDER = "main/resources/schemas";

    public static File getJsonFile(String fileName) {
        return Paths.get(SOURCE_FOLDER, SCHEMAS_FOLDER).resolve(fileName).toFile();
    }
}
