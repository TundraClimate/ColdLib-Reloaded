package io.github.tundraclimate.clib.front.ui

import io.github.tundraclimate.clib.ColdLib
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class InventoryGUIFactory(private val baseInventory: Inventory) {
    fun create(): InventoryGUI {
        val gui = object : InventoryGUI {
            override val handlers: MutableMap<Int, (event: InventoryClickEvent) -> Unit> = mutableMapOf()
            val inv = baseInventory
            override fun getElement(index: Int): ItemStack? {
                return inv.getItem(index)
            }

            override fun setElement(element: ItemStack, index: Int) {
                inv.setItem(index, element)
            }

            override fun open(player: Player) {
                player.openInventory(inv)
            }

            override fun close(player: Player) {
                player.closeInventory()
            }

            override fun setClickHandler(index: Int, onClick: (event: InventoryClickEvent) -> Unit) {
                handlers[index] = onClick
            }
        }
        val handler = object : Listener {
            @EventHandler
            fun onClick(e: InventoryClickEvent) {
                if (gui.inv == e.clickedInventory && gui.handlers.containsKey(e.slot)) gui.handlers[e.slot]?.let { it(e) }
            }
        }
        Bukkit.getServer().pluginManager.registerEvents(handler, ColdLib.plugin)
        return gui
    }
}