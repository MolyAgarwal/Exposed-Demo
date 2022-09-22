import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-server-core:2.1.1")
    implementation("io.ktor:ktor-server-netty:2.1.1")
    implementation("org.jetbrains.exposed", "exposed-core", "0.39.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.39.1")
    implementation("org.postgresql:postgresql:42.2.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}