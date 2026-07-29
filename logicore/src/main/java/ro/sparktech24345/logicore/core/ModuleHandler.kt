package ro.sparktech24345.logicore.core

import ro.sparktech24345.logicore.utils.Weighted
import ro.sparktech24345.logicore.utils.WeightedArray

class ModuleHandler : CoreModule {

    private var modules = WeightedArray<CoreModule>()
    private var initialized = false
    private var started = false
    private var stopped = false

    val size: Int
        get() = modules.size

    fun <T : CoreModule> install(module: T, priority: Float = 1.0f) {
        modules.add(module, priority)
        if (initialized) module.init()
        if (started) module.start()
        if (stopped) module.stop()
    }

    override fun init() {
        for (i in 0..modules.size) modules[i].value.init()
        initialized = true
    }

    override fun init_loop() {
        for (i in 0..modules.size) modules[i].value.init_loop()
    }

    override fun start() {
        for (i in 0..modules.size) modules[i].value.start()
        started = true
    }

    override fun loop() {
        for (i in 0..modules.size) modules[i].value.loop()
    }

    override fun stop() {
        for (i in 0..modules.size) modules[i].value.stop()
        stopped = true
    }
}
