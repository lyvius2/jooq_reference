package com.walter.reference.actor.repository

import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqSlowQueryTest(
    @Autowired val dslContext: DSLContext
) {
    @Test
    @DisplayName("SLOW 쿼리 탐지 테스트")
    fun slowQueryMonitoringTest() {
        dslContext.select(DSL.field("SLEEP(4)"))
            .from(DSL.dual())
            .execute()
    }
}