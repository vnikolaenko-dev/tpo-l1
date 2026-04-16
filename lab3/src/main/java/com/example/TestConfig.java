package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = TestConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getValidLogin() {
        return props.getProperty("valid.login");
    }

    public static String getValidPassword() {
        return props.getProperty("valid.password");
    }

    public static String getInvalidLogin() {
        return props.getProperty("invalid.login");
    }

    public static String getInvalidPassword() {
        return props.getProperty("invalid.password");
    }

    public static String getUrlHome() {
        return props.getProperty("url.home");
    }

    public static String getUrlProjects() {
        return props.getProperty("url.projects");
    }

    public static String getTestPhrases() {
        return props.getProperty("test.phrases");
    }

    public static String getTestFilePath() {
        return props.getProperty("test.file.path");
    }

    public static String getTestVkCommunityUrl() {
        return props.getProperty("test.vk.community.url");
    }
    public static String getTestPhrasesForClustering() {
        return props.getProperty("test.clustering.phrases");
    }

    public static String getTestFilePathForClustering() {
        return props.getProperty("test.clustering.file.path");
    }
    
    public static String getTestFilePathForMetaTags() {
        return props.getProperty("test.metatags.file.path");
    }

    public static String getTestFilePathForAssociation() {
       return props.getProperty("test.association.file.path");
    }

    public static String getTestFilePathForCompetitorAds() {
        return props.getProperty("test.competitorads.file.path");
    }
}