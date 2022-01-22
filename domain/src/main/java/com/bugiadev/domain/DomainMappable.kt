package com.bugiadev.domain

interface DomainMappable<R> {
    fun toDomain(): R
}