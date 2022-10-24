package io.github.tundraclimate.clib

import io.github.tundraclimate.clib.command.ICommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class ColdLib {
    companion object {
        lateinit var plugin: JavaPlugin
        internal val conf = ColdLibConfig()

        fun init(plugin: JavaPlugin, initialize: ColdLibConfig.() -> Unit) {
            this.plugin = plugin
            conf.addLooper("main")
            conf.initialize()
        }

        fun getDB(key: String) = conf.dbs[key]

        fun createCommand(
            command: String,
            onTab: (sender: CommandSender, command: Command, label: String, args: Array<out String>) -> MutableList<String>? = { _, _, _, _ -> null },
            onCmd: (sender: CommandSender, command: Command, label: String, args: Array<out String>) -> Boolean
        ): ICommand {
            val com = object : ICommand {
                override val name: String
                    get() = command

                override fun onCommand(
                    sender: CommandSender, command: Command, label: String, args: Array<out String>
                ): Boolean {
                    return onCmd(sender, command, label, args)
                }

                override fun onTabComplete(
                    sender: CommandSender, command: Command, label: String, args: Array<out String>
                ): MutableList<String>? {
                    return onTab(sender, command, label, args)
                }
            }
            return com
        }
    }
}