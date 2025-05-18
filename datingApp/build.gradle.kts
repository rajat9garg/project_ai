import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.12"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
    kotlin("kapt") version "1.9.20"
}

group = "learn.ai"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    
    // MongoDB
    implementation("org.mongodb:mongodb-driver-sync")
    implementation("org.mongodb:bson")
    implementation("org.mongodb:mongodb-driver-core")
    
    // BOMs
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.2.12"))
    implementation(platform("org.testcontainers:testcontainers-bom:1.19.1"))
    
    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    
    // Utilities
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.testcontainers:testcontainers:1.19.1")
    testImplementation("org.testcontainers:mongodb:1.19.1")
    testImplementation("org.testcontainers:junit-jupiter:1.19.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.bootJar {
    archiveFileName.set("${project.name}.jar")
    launchScript()
}
