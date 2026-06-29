package ro.sparktech24345.logicore.core

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.LynxVoltageSensor
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.VoltageSensor
import ro.sparktech24345.logicore.commands.BaseCommand
import ro.sparktech24345.logicore.utils.PreciseTimer

@Suppress("PROPERTY_HIDES_JAVA_FIELD")
abstract class CoreOpMode(
    val type: OpModeType,
    val dash: Boolean = true,
    var debug: Boolean = false
) : OpMode(), CommandQueuer {
    companion object {
        var instance: CoreOpMode? = null
    }
    enum class OpModeType {
        TELEOP,
        AUTONOMOUS,
        TESTING
    }
    lateinit var hubs: List<LynxModule>
        private set
    val queuer: CoreQueuer = CoreQueuer()
    lateinit var telemetry: CoreTelemetry
        private set
    lateinit var gamepad: CoreGamepad
        private set
    lateinit var voltageSensor: VoltageSensor
        private set


    private fun benchmark(
        run: () -> Unit = {},
        name: String = "GENERIC_BENCHMARK_NAME"
    ) {
        if (!debug) return
        val bm = PreciseTimer(name).start()
        run()
        bm.log(telemetry)
        println("Timer: ${bm.name} -- ${bm.getTimer().get() ?: 0} ms")
    }

    private fun upd(fn: () -> Unit) {
        benchmark({
            for (hub in hubs) hub.clearBulkCache()
            benchmark(gamepad::update, "GAMEPAD UPDATE")
            benchmark(fn, "MAIN UPDATE")
        }, "LOOP ITERATION")
        benchmark(telemetry::update, "TELEMETRY UPDATE")
    }

    final override fun init() {
        telemetry = CoreTelemetry(super.telemetry)
        if (dash) telemetry.addTelemetry(FtcDashboard.getInstance().telemetry)
        gamepad = CoreGamepad(gamepad1, gamepad2)
        hubs = hardwareMap.getAll(LynxModule::class.java)
        voltageSensor = hardwareMap.get(VoltageSensor::class.java, "Control Hub")
        for (hub in hubs) hub.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL
        upd(this::onInit)
    }

    final override fun init_loop() {
        upd(this::onInitLoop)
    }

    final override fun start() {
        upd(this::onStart)
    }

    final override fun loop() {
        upd(this::onLoop)
    }

    final override fun stop() {
        upd(this::onStop)
    }

    abstract fun onInit()
    abstract fun onInitLoop()
    abstract fun onStart()
    abstract fun onLoop()
    abstract fun onStop()


    final override fun queue(command: BaseCommand): CommandQueuer {
        queuer.queue(command)
        return queuer
    }
    final override fun execute(command: BaseCommand): CommandQueuer {
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