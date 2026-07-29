package ro.sparktech24345.logicore.core

import ro.sparktech24345.logicore.commands.BaseCommand

class CoreQueuer : CommandQueuer {
    val queuer: ArrayDeque<BaseCommand> = ArrayDeque()
    val executor: MutableList<BaseCommand> = mutableListOf()
    var pause = false

    override fun queue(command: BaseCommand) {
        queuer += command
    }

    override fun execute(command: BaseCommand) {
        executor += command
    }

    override fun clear() {
        queuer.clear()
        executor.clear()
    }

    override fun update(): Boolean {
        if (pause) return false
        val iter = queuer.iterator()
        while (iter.hasNext()) {
            val command = iter.next()
            command.update()
            if (command.finished) iter.remove()
            else break
        }
        executor.removeIf { command ->
            command.update()
            command.finished
        }
        return true
    }
}
