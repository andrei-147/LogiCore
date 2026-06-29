package ro.sparktech24345.logicore.core

interface CoreModule {
    fun init()
    fun loop()
    fun init_loop() {}
    fun start() {}
    fun stop() {}
}