package com.stephenbain.lines.common

val <T> T.exhaustive: T
    get() = this

fun <K, V> List<V>.toHashMap(generateKey: (V) -> K): HashMap<K, V> {
    val map = hashMapOf<K, V>()
    forEach { map[generateKey(it)] = it }
    return map
}