plugins {
    kotlin("jvm")
}

group = "com.walter"
version = "0.0.1-SNAPSHOT"

val jooqVersion = "3.19.5"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jooq:jooq-codegen:${jooqVersion}")
    runtimeOnly("com.mysql:mysql-connector-j:9.0.0")
}

kotlin {
    jvmToolchain(21)
}