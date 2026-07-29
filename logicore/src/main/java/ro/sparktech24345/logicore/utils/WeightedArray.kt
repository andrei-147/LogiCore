package ro.sparktech24345.logicore.utils

data class Weighted<T>(
    val value: T,
    val weight: Float
)

class WeightedArray<T> {
    private val arr: ArrayList<Weighted<T>> = arrayListOf()

    operator fun plusAssign(entry: Weighted<T>) = add(entry)

    fun add(entry: Weighted<T>) = this.add(entry.value, entry.weight)

    fun add(value: T, weight: Float = 1.0f) {
        arr.add(
            arr.binarySearchBy(-weight) { -it.weight }
                .let { if (it >= 0) it else -(it + 1) },
            Weighted(value, weight)
        )
    }

    operator fun get(index: Int): Weighted<T> = arr[index]

    val size: Int
        get() = arr.size

    fun remove(index: Int) = arr.removeAt(index)

    fun list(): List<Weighted<T>> = arr
}
