package dev.dogood.jooq.bugs

import org.example.db.generated.tables.references.EXAMPLE
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.JSON
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import org.testcontainers.containers.PostgreSQLContainer

data class SomeJsonWrapper(val someJson: JSON?)

fun main() {
    val dslContext = dslContext()
    val exampleId = 1L

    dslContext.insertInto(EXAMPLE)
        .set(EXAMPLE.ID, exampleId)
        .set(EXAMPLE.SOME_JSON, JSON.json("{}"))
        .execute()

    val example1 = EXAMPLE
    val example2 = EXAMPLE

    dslContext.select(
        //this line fails with Class class org.jooq.JSON is not supported
        row(example1.SOME_JSON).mapping(::SomeJsonWrapper),

        //I can sort of work around it by using multiset then calling single, but it's noisier and falls over if there's
        //not exactly one match
        multiset(
            selectFrom(example2).where(example1.ID.eq(example2.ID))
        ).convertFrom { r -> SomeJsonWrapper(r.single().someJson) },
    ).from(example1)
        .where(example1.ID.eq(exampleId))
        .fetchSingle()
}

/**
 * Create a DSLContext backed by Postgres, after being migrated by Flyway.
 */
private fun dslContext(): DSLContext {
    val postgresInstance = PostgreSQLContainer<Nothing>("postgres:13").apply {
        withDatabaseName("test")
        withUsername("test")
        withPassword("test")
    }
    postgresInstance.start()

    Flyway.configure()
        .dataSource(postgresInstance.jdbcUrl, postgresInstance.username, postgresInstance.password)
        .locations("classpath:db")
        .schemas("public")
        .load()
        .migrate()

    return DSL.using(
        postgresInstance.jdbcUrl,
        postgresInstance.username,
        postgresInstance.password,
    )
}