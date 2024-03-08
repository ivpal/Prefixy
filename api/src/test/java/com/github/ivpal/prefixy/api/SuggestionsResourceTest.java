package com.github.ivpal.prefixy.api;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class SuggestionsResourceTest {
    @Inject RedisDataSource rds;

    @BeforeEach
    void setup() {
        rds.key().del(SuggestionStoreRedis.KEY);
    }

    @Test
    void shouldListSuggestions() {
        List<String> list = given()
            .when()
            .get("/api/suggestions?q=ph")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>() {});

        assertThat(list).isEmpty();

        given()
            .when()
            .post("/api/suggestions?q=phone")
            .then()
            .statusCode(202);

        rds.autosuggest().ftSugAdd(SuggestionStoreRedis.KEY, "phone", 1.0, true);

        list = given()
            .when()
            .get("/api/suggestions?q=ph")
            .then()
            .statusCode(200)
            .extract()
            .as(new TypeRef<>() {});

        assertThat(list).hasSize(1);
        assertThat(list.getFirst()).isEqualTo("phone");
    }
}
