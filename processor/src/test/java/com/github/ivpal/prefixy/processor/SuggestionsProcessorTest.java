package com.github.ivpal.prefixy.processor;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@QuarkusTest
public class SuggestionsProcessorTest {
    @Inject
    @Channel("test-prefix-requests")
    Emitter<String> prefixEmitter;

    @Inject RedisDataSource rds;

    @BeforeEach
    void setup() {
        rds.key().del(SuggestionStoreRedis.KEY);
    }

    @Test
    void sendEventShouldCreateIndexTest() {
        var prefix = "phone";
        prefixEmitter.send(prefix);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                var length = rds.autosuggest().ftSugLen(SuggestionStoreRedis.KEY);
                assertThat(length).isEqualTo(1);
            });
    }
}
