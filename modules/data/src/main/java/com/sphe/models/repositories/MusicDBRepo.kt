package com.sphe.models.repositories

import com.sphe.base.base.CoroutineDispatchers
import com.sphe.models.db.base.BaseDao
import com.sphe.models.pagination.BaseEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

abstract class MusicDBRepo<E : BaseEntity>(
    private val dao: BaseDao<E>,
    private val dispatchers: CoroutineDispatchers
) {
     suspend fun entries() = withContext(dispatchers.io) { dao.entries() }

    suspend fun isEmpty(): Flow<Boolean> {
        return entries().map { it.isNotEmpty() }
    }

    suspend fun entry(id: String) = withContext(dispatchers.io) { dao.entry(id) }
}