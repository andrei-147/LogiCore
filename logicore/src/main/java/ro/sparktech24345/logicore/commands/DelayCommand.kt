package ro.sparktech24345.logicore.commands

import ro.sparktech24345.logicore.utils.PreciseTimer
import ro.sparktech24345.logicore.utils.PreciseTimer.TimeSpec

class DelayCommand(private val time: TimeSpec): BaseCommand() {
    private var timer = PreciseTimer()
    override var onStart: () -> Unit = { timer.start() }
    override var finishCondition = { timer.getTimer() >= time }
}