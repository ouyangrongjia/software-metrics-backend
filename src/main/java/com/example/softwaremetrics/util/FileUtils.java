package com.example.softwaremetrics.util;

import java.nio.file.Files;
import java.nio.file.Paths;


public class FileUtils {
    public static String[] getAllDirs(String path) {
        try {
            return Files.walk(Paths.get(path))
                    .filter(Files::isDirectory)
                    .filter(x -> !isGitDir(x.toAbsolutePath().toString()))
                    .map(x -> x.toAbsolutePath().toString())
                    .toArray(String[]::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] getAllJavaFiles(String path) {
        return getAllFiles(path, "java");
    }

    public static String[] getAllJars(String path) {
        return getAllFiles(path, "jar");
    }

    private static String[] getAllFiles(String path, String ending) {
        try {
            return Files.walk(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .filter(x -> !isGitDir(x.toAbsolutePath().toString()))
                    .filter(x -> x.toAbsolutePath().toString().toLowerCase().endsWith(ending))
                    .map(x -> x.toAbsolutePath().toString())
                    .toArray(String[]::new);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isGitDir(String path) {
        return path.contains("/.git/");
    }
}
