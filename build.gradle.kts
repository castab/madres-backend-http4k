plugins {
    kotlin("jvm") version "2.1.21"
    id("com.gradleup.shadow") version "9.2.2"
    application
}

application {
    mainClass.set("maders.backend.MainKt")
}

group = "madres.backend"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // http4k
    implementation("org.http4k:http4k-core:6.22.0.0")
    implementation("org.http4k:http4k-server-netty:6.22.0.0")
    implementation("org.http4k:http4k-format-jackson:6.22.0.0")

    // hikariCP
    implementation("com.zaxxer:HikariCP:7.0.2")

    // JDBI
    implementation("org.jdbi:jdbi3-core:3.50.0")
    implementation("org.jdbi:jdbi3-kotlin:3.50.0")

    // Postgres Driver
    runtimeOnly("org.postgresql:postgresql:42.7.8")

    // Hoplite config
    implementation("com.sksamuel.hoplite:hoplite-core:2.9.0")
    implementation("com.sksamuel.hoplite:hoplite-hocon:2.9.0")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.5.21")

    // Jackson data type
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.20.1")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveClassifier.set("all")
    mergeServiceFiles()
}

kotlin {
    jvmToolchain(21)
}