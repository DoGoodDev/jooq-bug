plugins {
    id("dev.dogood.conventions.jooq")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.flywaydb:flyway-core:8.5.11")
    implementation("org.testcontainers:postgresql:1.17.2")
    implementation("org.postgresql:postgresql:42.3.6")
}