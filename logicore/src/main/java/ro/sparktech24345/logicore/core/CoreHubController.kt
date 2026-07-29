package ro.sparktech24345.logicore.core

import com.qualcomm.hardware.lynx.LynxModule

class CoreHubController() : CoreModule {
    lateinit var hubs: List<LynxModule>
        private set
    override fun init() {
        hubs = CoreOpMode.instance!!.hardwareMap.getAll(LynxModule::class.java)
        for (hub in hubs) hub.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL
    }

    override fun loop() {
        for (hub in hubs) hub.clearBulkCache()
    }
}
