package ro.sparktech24345.logicore.core

import ro.sparktech24345.logicore.commands.BaseCommand

interface CommandQueuer {
    fun <T: BaseCommand> queue(command: T): CommandQueuer
    fun <T: BaseCommand> execute(command: T): CommandQueuer
    fun clear(): CommandQueuer

    fun update(): CommandQueuer
}