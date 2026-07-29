package ro.sparktech24345.logicore.hardware

import com.qualcomm.robotcore.hardware.Servo
import dev.frozenmilk.dairy.cachinghardware.CachingServo
import ro.sparktech24345.logicore.core.CoreModule
import ro.sparktech24345.logicore.core.CoreOpMode
import ro.sparktech24345.logicore.states.BaseStateSet
import ro.sparktech24345.logicore.states.HasStates

class CoreServo<T : BaseStateSet>(val name: String, states: T = BaseStateSet() as T) : CoreModule,
    HasStates<T>(states) {

    lateinit var servo: CachingServo

    var position: Double
        set(value) {
            servo.position = value / scale
        }
        get() {
            val value = servo.position
            return if (value.isNaN()) 0.0 else value * scale
        }

    var scale: Double = 1.0

    override fun init() {
        servo = CachingServo(CoreOpMode.instance!!.hardwareMap[name] as Servo)
    }

    override fun loop() {}
}
