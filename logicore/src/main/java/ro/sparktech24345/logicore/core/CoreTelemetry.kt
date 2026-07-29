package ro.sparktech24345.logicore.core

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import org.firstinspires.ftc.robotcore.external.Func
import org.firstinspires.ftc.robotcore.external.Telemetry

class CoreTelemetry : Telemetry by telemetry, CoreModule {
    companion object {
        val telemetry = MultipleTelemetry()
    }

    fun addTelemetry(vararg telemetry: Telemetry) {
        for (tel in telemetry) CoreTelemetry.telemetry.addTelemetry(tel)
    }

    override fun init() {}
    override fun loop() {
        telemetry.update()
    }
}
