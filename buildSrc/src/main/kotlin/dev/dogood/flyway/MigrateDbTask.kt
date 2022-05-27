package dev.dogood.flyway

import dev.dogood.testcontainers.postgres.PostgresSharedResource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

abstract class MigrateDbTask: DefaultTask() {
    @get:Internal
    abstract val postgresSharedResource: Property<PostgresSharedResource>

    @get:Input
    abstract val migrationDir: Property<String>

    @TaskAction
    fun run() {
        val pgInstance = postgresSharedResource.get().postgresInstance

        Flyway.configure()
            .dataSource(pgInstance.jdbcUrl, pgInstance.username, pgInstance.password)
            .locations("filesystem:${migrationDir.get()}")
            .schemas("public")
            .load()
            .migrate()
    }
}
