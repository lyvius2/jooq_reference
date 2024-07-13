package com.walter.reference.config

import org.jooq.ExecuteContext
import org.jooq.ExecuteListener
import org.jooq.tools.StopWatch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration

class PerformanceListener(
    val log: Logger? = LoggerFactory.getLogger(PerformanceListener::class.java),
    val SLOW_QUERY_LIMIT: Duration? = Duration.ofSeconds(3),
) : ExecuteListener {
    lateinit var watch: StopWatch

    override fun executeStart(ctx: ExecuteContext?) {
        watch = StopWatch()
    }

    override fun executeEnd(ctx: ExecuteContext?) {
        val queryTimeNano = watch.split()
        if (queryTimeNano > SLOW_QUERY_LIMIT!!.nano) {
            val query = ctx!!.query()
            val executeTime = Duration.ofNanos(queryTimeNano)
            log!!.warn(
            """
                ### SLOW SQL 탐지 >>
                경로 jOOQ로 실행된 쿼리 중 ${SLOW_QUERY_LIMIT.toSeconds()}초 이상 실행된 쿼리가 있습니다.
                실행시간 : ${executeTime.toMillis() / 1000.0} 초
                실행쿼리 : $query
            """)
        }
    }
}