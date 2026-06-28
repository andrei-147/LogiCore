package ro.sparktech24345.logicore.commands

class GenericCommand(
    override var command: () -> Unit,
    override var startCondition: () -> Boolean = { true },
    override var finishCondition: () -> Boolean = { true }
): BaseCommand()