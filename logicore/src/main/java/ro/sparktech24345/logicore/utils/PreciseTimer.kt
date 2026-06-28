package ro.sparktech24345.logicore.utils

import ro.sparktech24345.logicore.core.CoreTelemetry
import java.time.temporal.ChronoUnit

class PreciseTimer(val name: String = "GENERIC_TIMER_NAME") {
    class TimeSpec(private val timeNano: Long = System.nanoTime()): Comparable<TimeSpec> {

        override fun compareTo(other: TimeSpec) = timeNano.compareTo(other.timeNano)
        companion object {
            fun fromNano(timeNano: Long): TimeSpec {
                return TimeSpec(timeNano)
            }

            fun fromMillis(timeMillis: Double): TimeSpec {
                return TimeSpec((timeMillis * 1e6).toLong())
            }

            fun fromSeconds(timeSec: Double): TimeSpec {
                return fromMillis(timeSec * 1e3)
            }
        }

        fun get(unit: ChronoUnit = ChronoUnit.MILLIS): Double? {
            return when (unit) {
                ChronoUnit.NANOS -> getNs()
                ChronoUnit.MILLIS -> getMs()
                ChronoUnit.SECONDS -> getSec()
                else -> null
            }
        }

        fun getNs(): Double {
            return timeNano.toDouble()
        }

        fun getMs(): Double {
            return timeNano / 1.0e6
        }

        fun getSec(): Double {
            return getMs() / 1.0e3
        }
    }

    private var time: Long = System.nanoTime()

    fun start(): PreciseTimer {
        time = System.nanoTime()
        return this
    }

    fun getTimer(): TimeSpec {
        return TimeSpec(time)
    }

    fun log(telemetry: CoreTelemetry?, unit: ChronoUnit = ChronoUnit.MILLIS) {
        telemetry?.addLine("Timer: $name -- ${getTimer().get(unit) ?: 0} ms")
    }
}