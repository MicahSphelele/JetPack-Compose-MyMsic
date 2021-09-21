package com.sphe.models.db.base

import androidx.paging.PagingSource
import com.sphe.models.pagination.BaseEntity
import com.sphe.models.pagination.PaginatedEntity
import kotlinx.coroutines.flow.Flow

abstract class BaseDao<E : BaseEntity> {

    abstract fun entries(): Flow<List<E>>
    abstract fun entriesObservable(count: Int, offset: Int): Flow<List<E>>
    abstract fun entriesPagingSource(): PagingSource<Int, E>
    abstract fun entry(id: String): Flow<E>
    abstract fun entryNullable(id: String): Flow<E?>
    abstract fun entriesById(ids: List<String>): Flow<List<E>>
    abstract suspend fun has(id: String): Int
}

abstract class EntityDao<Params : Any, E : BaseEntity> : BaseDao<E>() {
    abstract fun entriesPagingSource(params: Params): PagingSource<Int, E>
    abstract suspend fun count(params: Params): Int
}

abstract class PaginatedEntryDao<Params : Any, E : PaginatedEntity> : EntityDao<Params, E>() {
    abstract fun entriesObservable(params: Params, page: Int): Flow<List<E>>
}