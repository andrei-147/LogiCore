package ro.sparktech24345.logicore.utils

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import dev.frozenmilk.dairy.cachinghardware.CachingDcMotorEx
import ro.sparktech24345.logicore.core.CoreModule
import ro.sparktech24345.logicore.core.CoreOpMode

class DriveTrain (
    val gamepad: Gamepad,
    val rightFront: String = "frontright",
    val leftFront: String = "frontleft",
    val rightBack: String = "backright",
    val leftBack: String = "backleft",
) : CoreModule {
    private lateinit var rf: CachingDcMotorEx
    private lateinit var lf: CachingDcMotorEx
    private lateinit var rb: CachingDcMotorEx
    private lateinit var lb: CachingDcMotorEx

    var zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        set(value) {
            field = value
            rf.zeroPowerBehavior = field
            lf.zeroPowerBehavior = field
            rb.zeroPowerBehavior = field
            lb.zeroPowerBehavior = field
        }

    var directionFlip = false
    var slowdownMultiplier = 1.0
        set(value) { field = value.coerceIn(0.0..1.0) }

    override fun init() {
        val map = CoreOpMode.instance!!.hardwareMap
        rf = CachingDcMotorEx(map[rightFront] as DcMotorEx)
        lf = CachingDcMotorEx(map[leftFront]  as DcMotorEx)
        rb = CachingDcMotorEx(map[rightBack]  as DcMotorEx)
        lb = CachingDcMotorEx(map[leftBack]   as DcMotorEx)

        lf.direction = DcMotorSimple.Direction.REVERSE
        lb.direction = DcMotorSimple.Direction.REVERSE
        this.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    override fun loop() {
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

        rf.power = (rfp * slowdownMultiplier)
        rb.power = rbp * slowdownMultiplier
        lf.power = lfp * slowdownMultiplier
        lb.power = lbp * slowdownMultiplier
    }
}