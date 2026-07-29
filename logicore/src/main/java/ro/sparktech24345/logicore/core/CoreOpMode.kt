package ro.sparktech24345.logicore.core

import com.acmerobotics.dashboard.FtcDashboard
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.hardware.lynx.LynxVoltageSensor
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.VoltageSensor
import org.firstinspires.ftc.robotcore.external.Telemetry
import ro.sparktech24345.logicore.commands.BaseCommand
import ro.sparktech24345.logicore.utils.PreciseTimer

@Suppress("PROPERTY_HIDES_JAVA_FIELD")
abstract class CoreOpMode(
    val type: OpModeType,
    val dash: Boolean = true,
    var debug: Boolean = false
) : OpMode(), CommandQueuer by queuer, Telemetry by telemetry {

    enum class GameStage {
        INIT,
        INIT_LOOP,
        START,
        LOOP,
        STOP,
    }

    override fun clear() = queuer.clear()
    override fun update(): Boolean = CoreOpMode.telemetry.update()

    companion object {
        var instance: CoreOpMode? = null
        val queuer: CoreQueuer = CoreQueuer()
        var period = GameStage.INIT
            private set
        private var modules = ModuleHandler()
        private var internalModules = ModuleHandler()
        val telemetry = CoreTelemetry()

    }

    fun <T : CoreModule> install(module: T, priority: Float = 1.0f) = modules.install(module, priority)

    enum class OpModeType {
        TELEOP,
        AUTONOMOUS,
        TESTING
    }

    val hubs = CoreHubController()
    var pauseQueuer: Boolean
        set(value) {
            queuer.pause = value
        }
        get() = queuer.pause
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
        bm.log(CoreOpMode.telemetry)
        println("Timer: ${bm.name} -- ${bm.getTimer().get() ?: 0} ms")
    }

    private fun updateAccordingly(mod: CoreModule) {
        when (period) {
            GameStage.INIT -> mod.init()
            GameStage.INIT_LOOP -> mod.init_loop()
            GameStage.START -> mod.start()
            GameStage.LOOP -> mod.loop()
            GameStage.STOP -> mod.stop()
        }
    }

    private fun upd(fn: () -> Unit) {
        updateAccordingly(internalModules)
        fn()
        updateAccordingly(modules)
    }

    final override fun init() {
        CoreOpMode.telemetry.addTelemetry(super.telemetry)
        if (dash) CoreOpMode.telemetry.addTelemetry(FtcDashboard.getInstance().telemetry)
        internalModules.install(CoreOpMode.telemetry)
        gamepad = CoreGamepad(gamepad1, gamepad2)
        internalModules.install(gamepad)
        voltageSensor = hardwareMap.get(VoltageSensor::class.java, "Control Hub")
        internalModules.install(hubs)
        upd(this::onInit)
        period = GameStage.INIT_LOOP
    }

    final override fun init_loop() {
        upd(this::onInitLoop)
    }

    final override fun start() {
        period = GameStage.START
        upd(this::onStart)
        period = GameStage.LOOP
    }

    final override fun loop() {
        upd(this::onLoop)
    }

    final override fun stop() {
        period = GameStage.STOP
        upd(this::onStop)
    }

    abstract fun onInit()
    abstract fun onInitLoop()
    abstract fun onStart()
    abstract fun onLoop()
    abstract fun onStop()
}
