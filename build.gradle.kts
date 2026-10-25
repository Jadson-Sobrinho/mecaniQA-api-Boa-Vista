plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.mecaniQA"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
	                                                                                                
	                                                                                             
	System.getenv("TEST_DB_URL")?.let { environment("DB_URL", it) }
	System.getenv("TEST_DB_USER")?.let { environment("DB_USER", it) }
	System.getenv("TEST_DB_PASSWORD")?.let { environment("DB_PASSWORD", it) }
}
