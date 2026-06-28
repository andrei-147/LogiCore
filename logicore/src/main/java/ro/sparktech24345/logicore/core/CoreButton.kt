package ro.sparktech24345.logicore.core

import ro.sparktech24345.logicore.utils.PreciseTimer
import ro.sparktech24345.logicore.utils.PreciseTimer.TimeSpec
import ro.sparktech24345.logicore.utils.MathUtils

open class CoreButton(private var isPressed: () -> Double = { 0.0 }) {
    private var trackPress = true
    private var trackRelease = true
    private var wasPressed: (() -> Boolean)? = null
        set(value) {
            field = value
            trackPress = value == null
        }
    private var wasReleased: (() -> Boolean)? = null
        set(value) {
            field = value
            trackRelease = value == null
        }

    enum class ToggleMode {
        ON_PRESS,
        ON_RELEASE
    }

    var toggleMode = ToggleMode.ON_PRESS

    var pressed = false
        private set
    var released = false
        private set
    var held = false
        private set
    var toggled = false
        private set
    var toggledOnPress = false
        private set
    var toggledOnRelease = false
        private set
    var heldTime = TimeSpec(0)
        private set
    private var heldTimer = PreciseTimer()

    companion object {
        fun ofBool(
            isPressed: () -> Boolean,
            wasPressed: (() -> Boolean)? = null,
            wasReleased: (() -> Boolean)? = null
        ): CoreButton {
            val obj = CoreButton { MathUtils.eval(isPressed()) }
            obj.wasPressed = wasPressed
            obj.wasReleased = wasReleased
            return obj
        }

        fun ofDouble(
            isPressed: () -> Double,
            wasPressed: (() -> Boolean)? = null,
            wasReleased: (() -> Boolean)? = null
        ): CoreButton {
            val obj = CoreButton(isPressed)
            obj.wasPressed = wasPressed
            obj.wasReleased = wasReleased
            return obj
        }
    }



    fun raw(): Double {
        return isPressed()
    }

    fun update() {
        var input = MathUtils.eval(isPressed())
        pressed = if (trackPress) !held && input else wasPressed?.invoke() ?: false
        released = if (trackRelease) held && !input else wasReleased?.invoke() ?: false
        held = input
        if (pressed) {
            heldTimer.start()
            toggledOnPress = !toggledOnPress
        }
        if (released) {
            heldTime = heldTimer.getTimer()
            toggledOnRelease = !toggledOnRelease
        }
        toggled = when (toggleMode) {
            ToggleMode.ON_PRESS -> toggledOnPress
            ToggleMode.ON_RELEASE -> toggledOnRelease
        }
    }
}