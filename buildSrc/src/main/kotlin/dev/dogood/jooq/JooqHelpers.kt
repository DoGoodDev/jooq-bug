package dev.dogood.jooq

import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target

fun jooqDefaultConfiguration(): Configuration {
    return Configuration()
        .withJdbc(Jdbc())
        .withGenerator(
            Generator()
                .withStrategy(Strategy())
                .withDatabase(Database())
                .withGenerate(Generate())
                .withTarget(
                    Target()
                        .withDirectory(null)
                )
        )
}