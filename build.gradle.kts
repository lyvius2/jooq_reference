import org.jooq.impl.SQLDataType
import org.jooq.meta.jaxb.ForcedType

plugins {
    id("org.springframework.boot") version "3.2.7"
    id("io.spring.dependency-management") version "1.1.5"
    id("nu.studer.jooq") version "9.0"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "com.walter"
version = "0.0.1-SNAPSHOT"

val jooqVersion = "3.19.5"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    named("main") {
        kotlin.srcDirs("src/main/kotlin", "src/generated")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq") {
        exclude("org.jooq:jooq")
    }
    implementation("org.jooq:jooq:${jooqVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //jooqGenerator("com.mysql:mysql-connector-j")
    jooqGenerator(project(":jOOQ-custom"))
    jooqGenerator("org.jooq:jooq:${jooqVersion}")
    jooqGenerator("org.jooq:jooq-meta:${jooqVersion}")
    jooqGenerator("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jooq {
    version = jooqVersion
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)
    configurations {
        create("sakilaDb") {
            jooqConfiguration.apply {
                jdbc.apply {
                    url = "jdbc:mysql://localhost:3306/sakila"
                    user = System.getenv("DB_USER")
                    password = System.getenv("DB_PASSWORD")
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        inputSchema = "sakila"
                        isUnsignedTypes = true
                        forcedTypes.addAll(
                            listOf(
                                ForcedType().apply {
                                    userType = "java.lang.Long"
                                    includeTypes = "INTEGERUNSIGNED"
                                }
                            )
                        )
                    }
                    generate.apply {
                        isDaos = true
                        isRecords = true
                        isFluentSetters = true
                        isJavaTimeTypes = true
                        isDeprecated = false
                    }
                    target.apply {
                        directory = "src/generated"
                    }
                    strategy.apply {
                        name = "jooq.custom.generator.JPrefixGeneratorStrategy"
                    }
                }
            }
        }
    }
}