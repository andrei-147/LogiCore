package ro.sparktech24345.logicore.core.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import ro.sparktech24345.logicore.core.CoreModule
import ro.sparktech24345.logicore.core.CoreOpMode
import ro.sparktech24345.logicore.utils.MathUtils

class CoreMotor(val name: String) : CoreModule {

    lateinit var motor: DcMotorImplEx
    var range = Pair(-1.0, 1.0)
    var target: Double = 0.0
        set(value) {
            field = value
        }
    enum class MotorRunMode {
        POWER,
        POSITION,
        VELOCITY,
        CUSTOM
    }

    var customLoop: (DcMotorImplEx) -> Unit = {}
        set(value) {
            field = value
            runMode = MotorRunMode.CUSTOM
        }

    var runMode = MotorRunMode.POWER
        set(value) {
            field = value
            motor.mode = when (value) {
                MotorRunMode.POWER -> DcMotor.RunMode.RUN_WITHOUT_ENCODER
                MotorRunMode.POSITION -> DcMotor.RunMode.RUN_TO_POSITION
                else -> DcMotor.RunMode.RUN_USING_ENCODER
            }
        }

    fun encoder(enabled: Boolean = true) {
        motor.mode = if (enabled) DcMotor.RunMode.RUN_USING_ENCODER else DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    fun pidf(p: Double, i: Double, d: Double, f: Double = 0.0) {
        motor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, PIDFCoefficients(p, i, d, f))
    }

    override fun init() {
        motor = CoreOpMode.instance?.hardwareMap[name] as DcMotorImplEx
        motor.velocity
    }

    override fun loop() {
        if (runMode != MotorRunMode.CUSTOM) return
        customLoop(motor)
    }

}