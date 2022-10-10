package io.github.tundraclimate.clib

import org.bukkit.plugin.java.JavaPlugin

class ColdLib {
    companion object {
        lateinit var plugin: JavaPlugin
        internal val conf = ColdLibConfig()

        fun init(plugin: JavaPlugin, initialize: ColdLibConfig.() -> Unit) {
            this.plugin = plugin
            conf.initialize()
        }

        fun getDB(key: String) = conf.dbs[key]
    }
}