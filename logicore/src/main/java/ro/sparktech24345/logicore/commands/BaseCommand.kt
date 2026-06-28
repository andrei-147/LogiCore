package ro.sparktech24345.logicore.commands

abstract class BaseCommand {
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

    fun setName(name: String): BaseCommand {
        this.name = name
        return this
    }

    fun setStartingCondition(condition: () -> Boolean): BaseCommand {
        startCondition = condition
        return this
    }

    fun setFinishCondition(condition: () -> Boolean): BaseCommand {
        finishCondition = condition
        return this
    }

    fun setCommand(command: () -> Unit): BaseCommand {
        this.command = command
        return this
    }

    fun setOnStart(command: () -> Unit): BaseCommand {
        this.onStart = command
        return this
    }

    fun setOnFinish(command: () -> Unit): BaseCommand {
        this.onFinish = command
        return this
    }
}