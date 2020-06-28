package com.example.bilyoner.base;

import com.jayway.restassured.RestAssured;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {BaseIntegrationTest.Initializer.class})
public class BaseIntegrationTest {

    @LocalServerPort
    private Integer port;

    protected static MongoDBContainer mongodbContainer;

    @BeforeAll
    protected static void beforeAll() {
        mongodbContainer = new MongoDBContainer();
        mongodbContainer.start();
    }

    @BeforeEach
    public void setup() throws Exception {
        RestAssured.port = port;
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(new ImmutableList.Builder<String>()
                    .add("spring.data.mongodb.host=" + mongodbContainer.getHost())
                    .add("spring.data.mongodb.port=" + mongodbContainer.getFirstMappedPort())
                    .build()).applyTo(configurableApplicationContext);
            ConfigurationPropertySources.attach(configurableApplicationContext.getEnvironment());
        }
    }

}
