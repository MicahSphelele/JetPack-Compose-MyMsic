package com.sphe.models.pagination

internal class PaginatedEntryRemoteMediator<Params: Any, E>(private val fetch: suspend (page: Int, refreshing: Boolean) -> Unit) {
}