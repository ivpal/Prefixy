package com.github.ivpal.prefixy.api;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.autosuggest.GetArgs;
import io.quarkus.redis.datasource.autosuggest.Suggestion;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SuggestionStoreRedis implements SuggestionStore {
    public static final String KEY = "suggestions";
    @ConfigProperty(name = "suggestions.max-return-list-size")
    int maxReturnListSize;

    @Inject RedisDataSource rds;

    @Override
    public List<String> list(String prefix) {
        var result = new ArrayList<String>();
        rds.withConnection(c -> {
            var commands = c.autosuggest();
            if (c.key().exists(KEY)) {
                var list = commands
                    .ftSugGet(KEY, prefix, new GetArgs().max(maxReturnListSize))
                    .stream()
                    .map(Suggestion::suggestion)
                    .toList();
                result.addAll(list);
            }
        });
        return result;
    }
}
