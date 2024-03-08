plugins {
    java
}

dependencies {
    testImplementation("org.awaitility:awaitility:4.2.0")
}

group = "com.github.ivpal.prefixy.processor"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
