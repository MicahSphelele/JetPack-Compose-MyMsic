package com.sphe.models.pagination

interface BaseEntity {
    val id: Long
}

interface PaginatedEntity: BaseEntity {
    var page: Int
}

abstract class BasePaginatedEntity: PaginatedEntity {

    companion object {
        const val defaultParams = ""
        const val defaultPage = 0
    }
}