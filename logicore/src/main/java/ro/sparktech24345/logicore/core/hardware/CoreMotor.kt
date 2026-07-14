package ro.sparktech24345.logicore.core.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx
import ro.sparktech24345.logicore.core.BaseStateSet
import ro.sparktech24345.logicore.core.CoreModule
import ro.sparktech24345.logicore.core.CoreOpMode
import ro.sparktech24345.logicore.core.StatesTrait
import kotlin.math.floor

class CoreMotor<T: BaseStateSet>(val name: String, states: T = BaseStateSet() as T) : CoreModule, StatesTrait<T>(states) {

    lateinit var motor: CachingDcMotorEx
        private set

    var unitsPerRev: Double = 1.0
        private set

    var power: Double
        set(value) {
            runMode = MotorRunMode.POWER
            motor.mode = motorRunMode()
            motor.power = value
        }
        get() = motor.power
    val currentPosition: Double
        get() = motor.currentPosition.toDouble()
    var position: Double
        set(value) {
            runMode = MotorRunMode.POSITION
            motor.mode = motorRunMode()
            motor.targetPosition = floor(value).toInt()
        }
        get() = motor.targetPosition.toDouble()
    var velocity: Double
        set(value) {
            runMode = MotorRunMode.VELOCITY
            motor.mode = motorRunMode()
            motor.velocity = value
        }
        get() = motor.velocity
    var zeroPowerBehavior: DcMotor.ZeroPowerBehavior
        set(value) { motor.zeroPowerBehavior = value }
        get() = motor.zeroPowerBehavior

    enum class MotorRunMode {
        POWER,
        POSITION,
        VELOCITY,
        CUSTOM
    }

    var customTarget: Double = 0.0
    var customLoop: (DcMotorEx, Double) -> Unit = { motor, target -> }
        set(value) {
            field = value
            runMode = MotorRunMode.CUSTOM
        }

    var runMode = MotorRunMode.POWER
        set(value) {
            field = value
            motor.mode = motorRunMode(value)
            motor.setPIDFCoefficients(motor.mode, pidf)
        }

    private fun motorRunMode(runMode: MotorRunMode = this.runMode): DcMotor.RunMode = when (runMode) {
        MotorRunMode.POWER -> DcMotor.RunMode.RUN_WITHOUT_ENCODER
        MotorRunMode.POSITION -> DcMotor.RunMode.RUN_TO_POSITION
        else -> DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun reverse(enabled: Boolean = true) {
        motor.direction = if (enabled) DcMotorSimple.Direction.REVERSE else DcMotorSimple.Direction.FORWARD
    }

    fun encoder(enabled: Boolean = true) {
        motor.mode = motorRunMode()
    }

    var pidf: PIDFCoefficients = PIDFCoefficients(0.0, 0.0, 0.0, 0.0)
        set(value) {
            motor.setPIDFCoefficients(motorRunMode(), value)
            field = value
        }

    override fun init() {
        motor = CachingDcMotorEx(CoreOpMode.instance!!.hardwareMap[name] as DcMotorEx)
        unitsPerRev = (motor.dcMotorEx as DcMotorImplEx?)?.controller?.getMotorType(motor.portNumber)?.ticksPerRev ?: 1.0
    }

    override fun init_loop() {
        this.loop()
    }

    override fun loop() {
        if (runMode == MotorRunMode.CUSTOM) customLoop(motor, customTarget)
    }
}