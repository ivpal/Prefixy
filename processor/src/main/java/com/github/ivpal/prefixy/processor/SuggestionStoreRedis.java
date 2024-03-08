package com.github.ivpal.prefixy.processor;

import io.quarkus.redis.datasource.RedisDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class SuggestionStoreRedis implements SuggestionStore {
    public static final String KEY = "suggestions";

    @ConfigProperty(name = "suggestions.max-prefix-length")
    int maxPrefixLength;

    @Inject RedisDataSource rds;

    @Override
    public void increment(String prefix) {
        rds.withConnection(c -> {
            var truncated = truncate(prefix, maxPrefixLength);
            var commands = rds.autosuggest();
            commands.ftSugAdd(KEY, truncated, 1.0);
        });
    }

    private static String truncate(String text, int length) {
        if (text.length() <= length) {
            return text;
        } else {
            return text.substring(0, length);
        }
    }
}
