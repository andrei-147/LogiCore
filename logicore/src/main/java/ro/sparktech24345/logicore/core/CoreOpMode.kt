package ro.sparktech24345.logicore.core

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import ro.sparktech24345.logicore.commands.BaseCommand
import ro.sparktech24345.logicore.utils.PreciseTimer
import ro.sparktech24345.logicore.utils.DriveTrain

@Suppress("PROPERTY_HIDES_JAVA_FIELD")
abstract class CoreOpMode(val dash: Boolean = true, val type: OpModeType) : LinearOpMode(), CommandQueuer {
    enum class OpModeType {
        TELEOP,
        AUTONOMOUS,
        TESTING
    }
    protected val hubs: List<LynxModule> by lazy { hardwareMap.getAll(LynxModule::class.java) }
    protected var debug = true

    private val queuer: CoreQueuer = CoreQueuer()

    protected var telemetry: CoreTelemetry? = null
        private set
    protected var useTelemetry = false
        set(value) {
            if (value && !field && telemetry == null) {
                telemetry = CoreTelemetry(super.telemetry)
                if (dash) telemetry?.addTelemetry(FtcDashboard.getInstance().telemetry)
            }
            field = value
        }

    protected var gamepad: CoreGamepad? = null
        private set
    protected var useGamepad = false
        set(value) {
            if (value && !field && gamepad == null) gamepad =
                CoreGamepad(super.gamepad1, super.gamepad2)
            field = value
        }

    protected var driveTrain: DriveTrain? = null
        private set
    protected var useDriveTrain = false
        set(value) {
            if (value && !field && driveTrain == null) driveTrain =
                DriveTrain(super.hardwareMap, gamepad1)
            field = value
        }

    private fun benchmarkIf(
        cond: () -> Boolean = { true },
        run: () -> Unit = {},
        name: String = "GENERIC_BENCHMARK_NAME"
    ) {
        if (!debug) return
        if (!cond()) return
        val bm = PreciseTimer(name).start()
        run()
        if (useTelemetry) bm.log(telemetry)
        println("Timer: ${bm.name} -- ${bm.getTimer().get() ?: 0} ms")
    }

    private fun upd(fn: () -> Unit) {
        benchmarkIf({ true }, {
            for (hub in hubs) hub.clearBulkCache()
            benchmarkIf({ useGamepad }, gamepad!!::update, "GAMEPAD UPDATE")
            benchmarkIf({ true }, fn, "MAIN UPDATE")
        }, "LOOP ITERATION")
        benchmarkIf({ useTelemetry }, telemetry!!::update, "TELEMETRY UPDATE")
    }

    final override fun runOpMode() {
        for (hub in hubs) hub.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL
        upd(this::onInit)
        while (!super.isStarted && !super<LinearOpMode>.isStopRequested) upd(this::onInitLoop)
        upd(this::onStart)
        while (opModeIsActive()) upd(this::onLoop)
        upd(this::onStop)
    }

    abstract fun onInit()
    abstract fun onInitLoop()
    abstract fun onStart()
    abstract fun onLoop()
    abstract fun onStop()


    final override fun <T: BaseCommand> queue(command: T): CommandQueuer {
        queuer.queue(command)
        return queuer
    }
    final override fun <T: BaseCommand> execute(command: T): CommandQueuer {
        queuer.execute(command)
        return queuer
    }
    final override fun clear(): CommandQueuer {
        queuer.clear()
        return queuer
    }
    final override fun update(): CommandQueuer {
        queuer.update()
        return queuer
    }
}