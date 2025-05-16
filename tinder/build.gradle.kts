plugins {
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
	kotlin("plugin.serialization") version "1.9.24"
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	id("org.jetbrains.kotlin.plugin.allopen") version "1.9.24"
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

kotlin {
	jvmToolchain(21)
}

tasks.withType<KotlinCompile>().configureEach {
	kotlinOptions.jvmTarget = "21"
}

group = "learn.ai"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

val coroutinesVersion = "1.8.0"
val reactorKotlinExtensionsVersion = "1.2.2"
val kotlinxSerializationVersion = "1.6.3"
val mockkVersion = "1.13.10"
val kotestVersion = "5.8.1"
val elasticsearchVersion = "8.12.1"
val springDataElasticsearchVersion = "5.2.0"

dependencies {
	// Spring Framework Core
	implementation("org.springframework:spring-core")
	// Spring Boot Starters
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	
	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
	
	// Kotlin Coroutines
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutinesVersion")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:$reactorKotlinExtensionsVersion")
	
	// Reactor Netty (required for reactive Elasticsearch)
	implementation("io.projectreactor.netty:reactor-netty-http")
	
	// Jackson
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	
	// Elasticsearch
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch") {
        exclude(group = "org.elasticsearch.client", module = "elasticsearch-rest-high-level-client")
    }
    implementation("co.elastic.clients:elasticsearch-java:$elasticsearchVersion")
    implementation("org.springframework.data:spring-data-elasticsearch:$springDataElasticsearchVersion")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	
	// MongoDB
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	
	// Cassandra
	implementation("org.springframework.boot:spring-boot-starter-data-cassandra")
	implementation("org.springframework.boot:spring-boot-starter-data-cassandra-reactive")
	
	// Redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.2")
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:3.2.2")
	implementation("org.springframework.data:spring-data-redis:3.2.2")
	implementation("io.lettuce:lettuce-core:6.2.5.RELEASE")
	implementation("com.fasterxml.jackson.core:jackson-databind")
	
	// Elasticsearch
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	implementation("co.elastic.clients:elasticsearch-java:$elasticsearchVersion")
	implementation("org.springframework.data:spring-data-elasticsearch:$springDataElasticsearchVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
	
	// Kafka
	implementation("org.springframework.kafka:spring-kafka")
	implementation("io.projectreactor.kafka:reactor-kafka:1.3.22")
	
	// Development
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	
	// Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
	testImplementation("io.mockk:mockk:$mockkVersion")
	testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
	testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	allOpen {
		annotation("javax.persistence.Entity")
		annotation("org.springframework.data.annotation.PersistenceConstructor")
	}
	
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
		freeCompilerArgs.addAll("-Xopt-in=kotlin.RequiresOptIn")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
