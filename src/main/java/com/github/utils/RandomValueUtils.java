package com.github.utils;

import com.github.javafaker.Faker;

public class RandomValueUtils {
    private static final Faker faker = new Faker();

    public static String generateRandomSentence() {
        return faker.lorem().sentence();
    }

    public static String generateRandomDomain() {
        return faker.internet().domainName();
    }
}
