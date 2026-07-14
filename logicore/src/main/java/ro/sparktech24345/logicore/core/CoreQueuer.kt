package ro.sparktech24345.logicore.core

import ro.sparktech24345.logicore.commands.BaseCommand

class CoreQueuer: CommandQueuer {
    val queuer: ArrayDeque<BaseCommand> = ArrayDeque()
    val executor: MutableList<BaseCommand> = mutableListOf()

    var pause = false

    override fun queue(command: BaseCommand): CommandQueuer {
        queuer += command
        return this
    }

    override fun execute(command: BaseCommand): CommandQueuer {
        executor += command
        return this
    }

    override fun clear(): CommandQueuer {
        queuer.clear()
        executor.clear()
        return this
    }

    override fun update(): CommandQueuer {
        if (pause) return this
        val iter = queuer.iterator()
        while (iter.hasNext()) {
            val command = iter.next()
            command.update()
            if (command.finished) iter.remove()
            else break
        }
        executor.removeIf {
            command -> command.update()
            command.finished
        }
        return this
    }
}