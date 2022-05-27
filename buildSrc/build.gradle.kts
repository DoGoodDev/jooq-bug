plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    implementation("org.flywaydb:flyway-core:8.5.11")
    implementation("org.jooq:jooq-codegen:3.16.6")
    implementation("org.testcontainers:postgresql:1.17.2")
    implementation("org.postgresql:postgresql:42.3.6")
}
