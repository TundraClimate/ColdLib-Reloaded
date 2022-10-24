package io.github.tundraclimate.clib

import io.github.tundraclimate.clib.db.DataBase
import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class ColdLibConfig internal constructor() {
    internal val dbs = mutableMapOf<String, DataBase>()
    private val looper: MutableMap<String, BukkitTask> = mutableMapOf()
    private val loopQueue = mutableMapOf<String, MutableList<() -> Unit>>()

    fun registerDB(key: String, db: DataBase) {
        if (!dbs.containsKey(key) && !dbs.contains(key))
            dbs[key] = db
    }

    fun registerEvent(event: Listener) = ColdLib.plugin.let { it.server.pluginManager.registerEvents(event, it) }

    fun registerCommand(command: String, executor: CommandExecutor) =
        ColdLib.plugin.let { it.getCommand(command)?.setExecutor(executor) }

    fun addLooper(key: String) {
        if (looper.containsKey(key)) {
            loopQueue[key] = mutableListOf()
            looper[key] = (object : BukkitRunnable() {
                override fun run() {
                    loopQueue[key]?.removeFirst()?.let { it() }
                }
            }.runTaskTimerAsynchronously(ColdLib.plugin, 0L, 0L))
        }
    }

    fun getHandler(looperKey: String): (() -> Unit) -> Unit {
        return { process: () -> Unit -> loopQueue[looperKey]?.add(process) }
    }

    fun deleteLooper(key: String) {
        if (looper.containsKey(key) && key != "main") {
            looper[key]?.cancel()
            looper.remove(key)
            loopQueue.remove(key)
        }
    }
}