package ro.sparktech24345.logicore.utils

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorImplEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import kotlin.text.get

class DriveTrain(
    val hardwareMap: HardwareMap,
    val gamepad: Gamepad,
    val rightFront: String = "frontright",
    val leftFront: String = "frontleft",
    val rightBack: String = "backright",
    val leftBack: String = "backleft",
) {
    private val rf: DcMotorImplEx = hardwareMap[rightFront] as DcMotorImplEx
    private val lf: DcMotorImplEx = hardwareMap[leftFront]  as DcMotorImplEx
    private val rb: DcMotorImplEx = hardwareMap[rightBack]  as DcMotorImplEx
    private val lb: DcMotorImplEx = hardwareMap[leftBack]   as DcMotorImplEx

    var zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        set(value) {
            field = value
            rf.zeroPowerBehavior = field
            lf.zeroPowerBehavior = field
            rb.zeroPowerBehavior = field
            lb.zeroPowerBehavior = field
        }

    init {
        lf.direction = DcMotorSimple.Direction.REVERSE
        lb.direction = DcMotorSimple.Direction.REVERSE
        this.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    var directionFlip = false
    var slowdownMultiplier = 1.0
        set(value) { field = MathUtils.clip(value, 0.0, 1.0) }

    fun update() {
        var vertical   = -gamepad.left_stick_y.toDouble()
        var horizontal = -gamepad.left_stick_x.toDouble()
        val pivot      =  gamepad.right_stick_x.toDouble()

        if (directionFlip) {
            horizontal *= -1
            vertical *= -1
        }

        var rfp = vertical + horizontal - pivot
        var rbp = vertical - horizontal - pivot
        var lfp = vertical - horizontal + pivot
        var lbp = vertical + horizontal + pivot

        val div = MathUtils.max(
            MathUtils.abs(rfp),
            MathUtils.abs(rbp),
            MathUtils.abs(lfp),
            MathUtils.abs(lbp)
        )

        if (div > 1.0) {
            rfp /= div
            rbp /= div
            lfp /= div
            lbp /= div
        }

        rf.power = rfp * slowdownMultiplier
        rb.power = rbp * slowdownMultiplier
        lf.power = lfp * slowdownMultiplier
        lb.power = lbp * slowdownMultiplier
    }
}