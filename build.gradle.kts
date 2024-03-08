subprojects {
    buildscript {
        repositories {
            mavenCentral()
            mavenLocal()
        }
    }

    repositories {
        mavenCentral()
        mavenLocal()
    }

    val quarkusPlatformGroupId: String by project
    val quarkusPlatformArtifactId: String by project
    val quarkusPlatformVersion: String by project

    apply(plugin = "io.quarkus")

    dependencies {
        implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
        implementation("io.quarkus:quarkus-smallrye-reactive-messaging-kafka")
        implementation("io.quarkus:quarkus-redis-client")
        implementation("io.quarkus:quarkus-arc")
        implementation("io.quarkus:quarkus-container-image-jib")
        implementation("io.quarkus:quarkus-opentelemetry")

        testImplementation("io.quarkus:quarkus-junit5")
        testImplementation("io.rest-assured:rest-assured")
        testImplementation("org.assertj:assertj-core:3.8.0")
    }

    tasks.withType<Test> {
        systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }
}

plugins {
    java
    id("io.quarkus")
}
