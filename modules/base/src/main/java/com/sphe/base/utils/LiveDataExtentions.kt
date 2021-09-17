package com.sphe.base.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

inline fun <T> LiveData<T>.behold(owner: LifecycleOwner, crossinline observer: (T?) -> Unit) {
    this.observe(owner, { observer(it) })
}

inline fun <T> LiveData<T>.witness(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    this.observe(owner, { if (it != null) observer(it) })
}

fun <T> MutableLiveData<T>.asImmutable(): LiveData<T> = this

fun <T> T.toMutableLive(): MutableLiveData<T> = MutableLiveData(this)
fun <T> T.toLive(): LiveData<T> = toMutableLive().asImmutable()

fun <T> LiveData<List<T>>.orEmpty(): List<T> = value.orEmpty()
fun <T> LiveData<Set<T>>.orEmpty(): Set<T> = value.orEmpty()
fun <K, V> LiveData<Map<K, V>>.orEmpty(): Map<K, V> = value.orEmpty()
fun <K, V> LiveData<MutableMap<K, V>>.orImmutableEmpty(): Map<K, V> = value.orEmpty()
fun <K, V> LiveData<MutableMap<K, V>>.orMutableEmpty(): MutableMap<K, V> = orImmutableEmpty().toMutableMap()