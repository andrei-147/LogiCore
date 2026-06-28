package ro.sparktech24345.logicore.utils

class MathUtils {
    companion object {
        fun abs(num: Double): Double {
            return if (num > 0) num else -num
        }

        fun signum(num: Double): Double {
            return when {
                num < 0.0 -> -1.0
                num > 0.0 -> 1.0
                else -> 0.0
            }
        }

        fun max(vararg nums: Double): Double {
            require(nums.isNotEmpty()) { "max requires at least one argument" }
            var mx: Double = nums[0]
            for (num in nums) mx = if (mx < num) num else mx
            return mx
        }

        fun min(vararg nums: Double): Double {
            require(nums.isNotEmpty()) { "max requires at least one argument" }
            var mn: Double = nums[0]
            for (num in nums) mn = if (mn > num) num else mn
            return mn
        }

        fun clip(num: Double, lo: Double, hi: Double): Double {
            return min(max(num, lo), hi)
        }

        fun eval(num: Double): Boolean {
            return abs(num) > .1
        }

        fun eval(num: Boolean): Double {
            if (num) return 1.0
            return 0.0
        }
    }
}