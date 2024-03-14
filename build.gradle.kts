plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.3.4"
    id("io.micronaut.aot") version "4.3.4"
    kotlin("jvm")
}

version = "0.1"
group = "com.objectcomputing"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("info.picocli:picocli-codegen")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")

    implementation("info.picocli:picocli")
    implementation("io.micronaut.picocli:micronaut-picocli")
    implementation("io.micronaut.serde:micronaut-serde-jackson")

    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly("ch.qos.logback:logback-classic")

    implementation("dev.langchain4j:langchain4j:0.28.0")
    implementation("dev.langchain4j:langchain4j-open-ai:0.28.0")
    implementation("dev.langchain4j:langchain4j-ollama:0.28.0")
    implementation("dev.langchain4j:langchain4j-embeddings-all-minilm-l6-v2:0.28.0")
    implementation(kotlin("stdlib-jdk8"))
}

application {
    mainClass.set("com.objectcomputing.Application")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.objectcomputing.*")
    }
    aot {
    // Please review carefully the optimizations enabled below
    // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
    }
}

tasks.withType<JavaExec>() {
    standardInput = System.`in`
}
kotlin {
    jvmToolchain(17)
}