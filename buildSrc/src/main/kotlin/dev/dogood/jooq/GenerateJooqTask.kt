package dev.dogood.jooq

import dev.dogood.testcontainers.postgres.PostgresSharedResource
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.Configuration
import java.sql.DriverManager

abstract class GenerateJooqTask: DefaultTask() {

    @get:Internal
    abstract val postgresSharedResource: Property<PostgresSharedResource>

    @Internal
    val jooqConfiguration: Property<Configuration> = project.objects.property(Configuration::class.java)

    @TaskAction
    fun run() {
        val pgInstance = postgresSharedResource.get().postgresInstance

        DriverManager.getConnection(pgInstance.jdbcUrl, pgInstance.username, pgInstance.password).use {
            val generationTool = GenerationTool()
            generationTool.setConnection(it)
            generationTool.run(jooqConfiguration.get())
        }
    }

}