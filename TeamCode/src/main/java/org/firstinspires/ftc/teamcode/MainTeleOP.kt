package org.firstinspires.ftc.teamcode

import ro.sparktech24345.logicore.commands.BaseCommand
import ro.sparktech24345.logicore.commands.DelayCommand
import ro.sparktech24345.logicore.core.CoreOpMode
import ro.sparktech24345.logicore.utils.PreciseTimer.TimeSpec
import ro.sparktech24345.logicore.utils.Vec2

class MainTeleOP: CoreOpMode(OpModeType.TESTING) {
    override fun onInit() {
        queue(DelayCommand(TimeSpec.fromMillis(100.0)))
        queue(
            BaseCommand().apply {
                command = { /* the code snippet you want the BaseCommand to run */ }
                startCondition = { true /* the condition for the BaseCommand to start execution */ }
                finishCondition = { true /* the condition for when the BaseCommand's command has finished */ }
                onStart = { /* Execute-once command for when the BaseCommand first starts executing */ }
                onFinish = { /* Execute-once command for when the BaseCommand has finished executing */ }
            }
        )
        execute(BaseCommand().apply { command = { println("Hello, world!") } })
    }

    override fun onInitLoop() {

    }

    override fun onStart() {

    }

    override fun onLoop() {

    }

    override fun onStop() {

    }
}