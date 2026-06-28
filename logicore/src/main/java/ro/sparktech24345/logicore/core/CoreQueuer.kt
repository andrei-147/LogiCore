package ro.sparktech24345.logicore.core

import ro.sparktech24345.logicore.commands.BaseCommand
import java.util.ArrayDeque
import java.util.Queue

class CoreQueuer: CommandQueuer {
    val queuer: Queue<BaseCommand> = ArrayDeque()
    val executor: ArrayList<BaseCommand> = ArrayList()

    private val pause = false

    override fun <T : BaseCommand> queue(command: T): CommandQueuer {
        queuer.add(command)
        return this
    }

    override fun <T : BaseCommand> execute(command: T): CommandQueuer {
        executor.add(command)
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