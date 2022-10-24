package io.github.tundraclimate.clib.front.ui

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

interface InventoryGUI {
    val handlers: MutableMap<Int, (event: InventoryClickEvent) -> Unit>
    fun open(player: Player)
    fun close(player: Player)
    fun setClickHandler(index: Int, onClick: (event: InventoryClickEvent) -> Unit)
    fun setElement(element: ItemStack, index: Int)
    fun getElement(index: Int): ItemStack?
}