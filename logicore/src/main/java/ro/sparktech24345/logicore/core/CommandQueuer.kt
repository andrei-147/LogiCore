package ro.sparktech24345.logicore.core

import ro.sparktech24345.logicore.commands.BaseCommand

interface CommandQueuer {
    fun queue(command: BaseCommand): CommandQueuer
    fun execute(command: BaseCommand): CommandQueuer
    fun clear(): CommandQueuer

    fun update(): CommandQueuer
}