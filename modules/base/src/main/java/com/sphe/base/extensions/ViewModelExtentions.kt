package com.sphe.base.extensions

import androidx.lifecycle.SavedStateHandle

inline fun <reified T : Any> SavedStateHandle.require(key: String): T = requireNotNull(get(key))