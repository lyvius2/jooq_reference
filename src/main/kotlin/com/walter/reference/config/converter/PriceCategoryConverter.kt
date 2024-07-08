package com.walter.reference.config.converter

import com.walter.reference.film.enum.PriceCategory

import org.jooq.impl.EnumConverter

class PriceCategoryConverter:
    EnumConverter<String, PriceCategory>(String::class.java, PriceCategory::class.java, PriceCategory::code) {
}