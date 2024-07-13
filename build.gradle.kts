import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Generate
import org.jooq.meta.jaxb.Strategy

plugins {
    id("org.springframework.boot") version "3.2.7"
    id("io.spring.dependency-management") version "1.1.5"
    id("dev.monosoul.jooq-docker") version "6.0.14"
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
    implementation(kotlin("stdlib"))
    implementation("org.jooq:jooq:${jooqVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //jooqGenerator("com.mysql:mysql-connector-j")
    jooqCodegen(project(":jOOQ-custom"))
    jooqCodegen("org.jooq:jooq:${jooqVersion}")
    jooqCodegen("org.jooq:jooq-meta:${jooqVersion}")
    jooqCodegen("org.jooq:jooq-codegen:${jooqVersion}")
    jooqCodegen("jakarta.xml.bind:jakarta.xml.bind-api:4.0.0")
    jooqCodegen("org.flywaydb:flyway-core:10.8.1")
    jooqCodegen("org.flywaydb:flyway-mysql:10.8.1")
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
    withContainer {
        image {
            name = "mysql:8.0.29"
            envVars = mapOf(
                "MYSQL_ROOT_PASSWORD" to "password",
                "MYSQL_DATABASE" to "sakila"
            )
        }

        db {
            username = "root"
            password = "password"
            name = "sakila"
            port = 3306
            jdbc {
                schema = "jdbc:mysql"
                driverClassName = "com.mysql.cj.jdbc.Driver"
            }
        }
    }
}

tasks {
    generateJooqClasses {
        schemas.set(listOf("sakila"))
        outputDirectory.set(layout.projectDirectory.dir("src/generated"))
        includeFlywayTable.set(false)

        usingJavaConfig {
            withName("org.jooq.codegen.KotlinGenerator")
            generate = Generate().apply {
                isJavaTimeTypes = true
                isDeprecated = false
                isDaos = true
                isFluentSetters = true
                isRecords = true
                isKotlinNotNullRecordAttributes = false
            }

            withStrategy(
                Strategy().withName("jooq.custom.generator.JPrefixGeneratorStrategy")
            )

            database.withForcedTypes(
                listOf(
                    ForcedType().apply {
                        userType = "java.lang.Long"
                        types = "int unsigned"
                    },
                    ForcedType().apply {
                        userType = "java.lang.Integer"
                        types = "tinyint unsigned"
                    },
                    ForcedType().apply {
                        userType = "java.lang.Integer"
                        types = "smallint unsigned"
                    }
                )
            )
        }
    }
}