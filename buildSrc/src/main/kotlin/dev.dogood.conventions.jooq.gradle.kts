import dev.dogood.flyway.MigrateDbTask
import dev.dogood.jooq.GenerateJooqTask
import dev.dogood.testcontainers.postgres.PostgresSharedResource
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

val postgres = project.gradle.sharedServices.registerIfAbsent("postgres", PostgresSharedResource::class.java) {

}

val migrateDbTask = tasks.register<MigrateDbTask>("migrateDb") {
    usesService(postgres)
    postgresSharedResource.set(postgres)
    migrationDir.set("${projectDir}/src/main/resources/db")
}

val generateJooq = tasks.register<GenerateJooqTask>("generateJooq") {
    usesService(postgres)
    postgresSharedResource.set(postgres)
    jooqConfiguration.set(
        dev.dogood.jooq.jooqDefaultConfiguration().apply {
            generator.apply {
                name = "org.jooq.codegen.KotlinGenerator"
                database.apply {
                    name = "org.jooq.meta.postgres.PostgresDatabase"
                    inputSchema = "public"
                }
                target.apply {
                    packageName = "org.example.db.generated"
                    directory = "${buildDir}/generated-src/kotlin/"
                }
            }
        }
    )
    dependsOn(migrateDbTask)
}

dependencies {
    implementation("org.jooq:jooq:3.16.6")
}

sourceSets["main"].java {
    srcDir("${buildDir}/generated-src/kotlin/")
}

tasks.withType<KotlinCompile> {
    dependsOn(generateJooq)
}