package ro.sparktech24345.logicore.commands

open class BaseCommand {
    var started = false
        private set
    var finished = false
        private set
    open var startCondition: () -> Boolean = { true }
    open var finishCondition: () -> Boolean = { true }
    open var command: () -> Unit = {}
    open var onStart: () -> Unit = {}
    open var onFinish: () -> Unit = {}
    var name: String = "GENERIC_ACTION_NAME"

    fun update() {
        if (!started) {
            started = startCondition()
            if (started) onStart()
        }
        if (!finished && started) {
            command()
            finished = finishCondition()
            if (finished) onFinish()
        }
    }
}