package io.github.tundraclimate.clib

import io.github.tundraclimate.clib.db.DataBase
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener

class ColdLibConfig internal constructor() {
    internal val dbs = mutableMapOf<String, DataBase>()

    fun registerDB(key: String, db: DataBase) {
        if (!dbs.containsKey(key) && !dbs.contains(key))
            dbs[key] = db
    }

    fun registerEvent(event: Listener) = ColdLib.plugin.let { it.server.pluginManager.registerEvents(event, it) }

    fun registerCommand(command: String, executor: CommandExecutor) =
        ColdLib.plugin.let { it.getCommand(command)?.setExecutor(executor) }
}