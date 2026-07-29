package ro.sparktech24345.logicore.commands

import ro.sparktech24345.logicore.core.CoreQueuer

class SequenceCommand(private val run: (CoreQueuer) -> Unit) : BaseCommand() {
    private val queuer = CoreQueuer()
    override var onStart = { run(queuer) }
    override var command = { queuer.update(); Unit }
}
