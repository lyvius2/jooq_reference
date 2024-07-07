package com.walter.reference.utils.jooq

import org.jooq.Condition
import org.jooq.Field
import org.jooq.impl.DSL
import org.jooq.tools.StringUtils
import org.springframework.util.CollectionUtils

class JooqListConditionUtil {
    companion object {
        fun <T> inIfNotEmpty(actorId: Field<T?>, uIntegerIds: List<T>?): Condition {
            if (CollectionUtils.isEmpty(uIntegerIds)) {
                return DSL.noCondition()
            }
            return actorId.`in`(uIntegerIds)
        }

        fun containsIfNotBlank(field: Field<String?>, inputValue: String?): Condition {
            if (StringUtils.isEmpty(inputValue)) {
                return DSL.noCondition()
            }
            return field.like("%$inputValue%")
        }
    }
}