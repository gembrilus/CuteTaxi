package ua.com.cuteteam.cutetaxiproject.extentions

fun <K, V> Map<K, V>.findBy(predicate: (Map.Entry<K, V>) -> Boolean): Map.Entry<K, V>? {
    for (entry in this) {
        if (predicate(entry)) return entry
    }
    return null
}

fun <K, V> Map<K, V>.copy(): Map<K, V> {
    val newMap: Map<K, V> = emptyMap()
    for (entry in this) {
        newMap.plus(entry.key to entry.value)
    }
    return newMap
}
