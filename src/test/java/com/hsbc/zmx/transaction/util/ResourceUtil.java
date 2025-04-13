package com.hsbc.zmx.transaction.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class ResourceUtil {

    public static String readFileContent(String filename) {
        try {
            Path path = Paths.get(ClassLoader.getSystemResource(filename).toURI());
            return new String(Files.readAllBytes(path));
        } catch (Exception ex) {
            log.error("fail to load file: ", ex);
        }
        return "";
    }
}
