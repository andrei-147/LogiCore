package ro.sparktech24345.logicore.core

import ro.sparktech24345.logicore.commands.BaseCommand

interface CommandQueuer {
    fun queue(command: BaseCommand)
    fun execute(command: BaseCommand)
    fun clear()
    fun update(): Boolean
}
