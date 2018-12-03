package com.ivko.hello.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheServiceLayerTest {

    @Test
    public void createCacheFromDB() {
        //GIVEN:
        Map<Long, String> dbCache = new ConcurrentHashMap<>();
        long expectedKey = 1;
        String expectedValue = "Elon Musk";

        //WHEN:
        dbCache.put(expectedKey, expectedValue);
        long actualKey = 0;
        String actualValue = null;
        for (Map.Entry<Long, String> entry : dbCache.entrySet()) {
            actualKey = entry.getKey();
            actualValue = entry.getValue();
        }

        //THEN:
        Assert.assertEquals(expectedValue, actualValue);
        Assert.assertEquals(expectedKey, actualKey);
        Assert.assertEquals(1, dbCache.size());
    }
}