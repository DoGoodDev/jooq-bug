package dev.dogood.testcontainers.postgres

import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.internal.resources.SharedResource
import org.testcontainers.Testcontainers
import org.testcontainers.containers.PostgreSQLContainer

abstract class PostgresSharedResource : BuildService<BuildServiceParameters.None>, AutoCloseable {
    val postgresInstance: PostgreSQLContainer<Nothing>

    init {
        postgresInstance = PostgreSQLContainer<Nothing>("postgres:13").apply{
            withDatabaseName("test")
            withUsername("test")
            withPassword("test")
        }
        postgresInstance.start()
    }

    override fun close() {
        postgresInstance.close()
    }
}