package ro.sparktech24345.logicore.hardware

import com.qualcomm.robotcore.hardware.ColorSensor
import ro.sparktech24345.logicore.core.CoreModule
import ro.sparktech24345.logicore.core.CoreOpMode

class CoreColorSensor(val name: String, private var tickRate: UInt = 1u) : CoreModule {
    lateinit var sensor: ColorSensor
    private var readCount: UInt = 0u
    private var color: UInt = 0u
    var r: UInt = 0u
    var g: UInt = 0u
    var b: UInt = 0u
    var a: UInt = 0u

    override fun init() {
        sensor = CoreOpMode.instance!!.hardwareMap[name] as ColorSensor
        if (tickRate < 1u) tickRate = 1u
    }

    override fun init_loop() {
        this.loop()
    }

    override fun loop() {
        if (readCount % tickRate != 0u) return
        color = sensor.argb().toUInt()
        a = (color shr 24) and 0xFFu
        r = (color shr 16) and 0xFFu
        g = (color shr 8) and 0xFFu
        b = color and 0xFFu
    }
}
