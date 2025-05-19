import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Core plugins
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
    kotlin("plugin.allopen") version "1.9.23"
    kotlin("plugin.noarg") version "1.9.23"
    id("org.openapi.generator") version "7.3.0"
}

group = "learn.ai"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    google()
    maven { url = uri("https://repo.spring.io/milestone") }
    maven { url = uri("https://repo.spring.io/snapshot") }
}

// Dependency versions
val swaggerVersion = "2.2.20"
val openapiVersion = "1.8.0"
val jjwtVersion = "0.11.5"
val testContainersVersion = "1.19.3"
val mockkVersion = "1.13.8"
val springMockkVersion = "4.0.2"
val assertjVersion = "3.24.2"

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    
    // Spring Data
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    
    // Spring Data
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    
    // OpenAPI Annotations (Jakarta)
    implementation("io.swagger.core.v3:swagger-annotations-jakarta:$swaggerVersion")
    implementation("io.swagger.core.v3:swagger-models-jakarta:$swaggerVersion")
    
    
    // Jakarta EE (Spring Boot 3.x)
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    
    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    
    // JWT
    implementation("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")
    
    // MongoDB
    implementation("org.mongodb:mongodb-driver-sync")
    
    // OpenAPI and Documentation
    implementation("io.swagger.core.v3:swagger-annotations:$swaggerVersion")
    implementation("io.swagger.core.v3:swagger-models:$swaggerVersion")
    
    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    
    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
    testImplementation("org.testcontainers:mongodb:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    
    // Development tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
}

// Configure dependency management for Spring Boot
dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.2.5")
    }
}

// Configure Kotlin compilation tasks
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = "21"
    }
}

// Configure test tasks
tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

// OpenAPI Generator configuration
openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/src/main/resources/openapi/api.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("learn.ai.generated.api")
    modelPackage.set("learn.ai.generated.model")
    
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "useSpringBoot3" to "true",
            "useBeanValidation" to "true",
            "documentationProvider" to "springdoc",
            "serializationLibrary" to "jackson",
            "enumPropertyNaming" to "UPPERCASE",
            "serializableModel" to "true",
            "library" to "spring-boot",
            "reactive" to "false",
            "useTags" to "true",
            "skipDefaultInterface" to "false",
            "performBeanValidation" to "true"
        )
    )
    
    typeMappings.set(
        mapOf(
            "OffsetDateTime" to "java.time.Instant",
            "LocalDate" to "java.time.LocalDate"
        )
    )
    
    importMappings.set(
        mapOf(
            "java.time.OffsetDateTime" to "java.time.Instant",
            "org.springframework.data.domain.Pageable" to "org.springdoc.core.converters.models.Pageable"
        )
    )
    
    generateApiTests.set(false)
    generateModelTests.set(false)
    generateApiDocumentation.set(false)
    generateModelDocumentation.set(false)
    withXml.set(false)
    skipOverwrite.set(false)
    logToStderr.set(true)
    validateSpec.set(false) // Skip validation during build
    generateAliasAsModel.set(false)
    skipOperationExample.set(true)
}

// Configure task dependencies to ensure proper build order
plugins.withId("org.jetbrains.kotlin.kapt") {
    tasks.named("kaptGenerateStubsKotlin") {
        dependsOn(tasks.named("openApiGenerate"))
    }

    tasks.named("compileKotlin") {
        dependsOn(tasks.named("kaptGenerateStubsKotlin"))
    }
}

// Fallback for when kapt is not used
tasks.named("compileKotlin") {
    dependsOn(tasks.named("openApiGenerate"))
}

// Configure Java compilation
tasks.withType<JavaCompile> {
    sourceCompatibility = "21"
    targetCompatibility = "21"
}

// Add generated sources to the main source set
sourceSets.main {
    java.srcDirs(
        "$buildDir/generated/src/main/kotlin",
        "$projectDir/src/main/kotlin"
    )
}

// Ensure the OpenAPI client is generated during build
tasks.build {
    dependsOn("openApiGenerate")
}

// Configure the build to generate OpenAPI client when building
tasks.compileKotlin {
    dependsOn("openApiGenerate")
}


tasks.bootJar {
    archiveFileName.set("${project.name}.jar")
    launchScript()
}