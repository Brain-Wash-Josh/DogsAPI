plugins {
    id("io.micronaut.application") version "4.5.4"
    id("com.gradleup.shadow") version "8.3.7"
    id("io.micronaut.aot") version "4.5.4"
    id("io.micronaut.test-resources") version "4.4.2"

    kotlin("jvm") version "1.9.25"
    kotlin("plugin.allopen") version "1.9.25"
    kotlin("kapt") version "1.9.25"
}

version = "0.1"
group = "com.task"

repositories {
    mavenCentral()
}

dependencies {
    // Micronaut Core
    kapt("io.micronaut:micronaut-http-validation")
    kapt("io.micronaut.serde:micronaut-serde-processor")
    kapt("io.micronaut.data:micronaut-data-processor")
    
    // Map Structure
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")
    
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    
    // Database
    runtimeOnly("com.h2database:h2")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("org.springframework:spring-jdbc:7.0.0-M9")
    
    // Validation
    implementation("io.micronaut.validation:micronaut-validation")
    
    // Logging
    runtimeOnly("ch.qos.logback:logback-classic")
    
    // Testing
    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:3.24.2")
}


application {
    mainClass = "com.task.Application"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
    targetCompatibility = JavaVersion.toVersion("21")
}


//graalvmNative.toolchainDetection = false

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.task.*")
    }
   /*  aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading = false
        convertYamlToJava = false
        precomputeOperations = true
        cacheEnvironment = true
        optimizeClassLoading = true
        deduceEnvironment = true
        optimizeNetty = true
        replaceLogbackXml = true
    } */
}

tasks.withType<Test> {
    useJUnitPlatform()
}

allOpen {
    annotation("io.micronaut.http.annotation.Controller")
    annotation("jakarta.inject.Singleton")
}

kapt {
    arguments {
        arg("mapstruct.defaultComponentModel", "jsr330")
    }
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}


