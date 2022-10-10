package io.github.tundraclimate.clib.item

import com.google.common.collect.Multimap
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class NewItemStack private constructor(stack: ItemStack) {

    private val item = stack.clone()

    companion object {
        @JvmStatic
        fun from(from: ItemStack): NewItemStack = NewItemStack(from)

        @JvmStatic
        fun create(
            from: ItemStack,
            displayName: String? = null,
            lore: List<String>? = null,
            unbreakable: Boolean = false,
            customModelData: Int? = null,
            localizeName: String? = null,
            enchants: Map<Enchantment, Int>? = null,
            attributeModifiers: Multimap<Attribute, AttributeModifier>? = null,
            itemFlags: Array<ItemFlag>? = null,
        ): ItemStack {
            val stack = from.clone()
            val meta = stack.itemMeta!!
            meta.setDisplayName(displayName)
            meta.lore = lore
            meta.isUnbreakable = unbreakable
            meta.setCustomModelData(customModelData)
            meta.setLocalizedName(localizeName)
            meta.attributeModifiers = attributeModifiers
            itemFlags?.let { meta.addItemFlags(*it) }
            enchants?.let { stack.addUnsafeEnchantments(it) }
            stack.itemMeta = meta

            return stack
        }

        @JvmStatic
        fun create(
            type: Material,
            amount: Int = 1,
            displayName: String? = null,
            lore: List<String>? = null,
            unbreakable: Boolean = false,
            customModelData: Int? = null,
            localizeName: String? = null,
            enchants: Map<Enchantment, Int>? = null,
            attributeModifiers: Multimap<Attribute, AttributeModifier>? = null,
            itemFlags: Array<ItemFlag>? = null
        ): ItemStack {
            return create(
                ItemStack(type, amount),
                displayName,
                lore,
                unbreakable,
                customModelData,
                localizeName,
                enchants,
                attributeModifiers,
                itemFlags
            )
        }
    }

    fun <T, Z : Any> setPersistentData(key: NamespacedKey, data: Pair<PersistentDataType<T, Z>, Z>): NewItemStack {
        val meta = item.itemMeta!!
        meta.persistentDataContainer.set(key, data.first, data.second)
        item.itemMeta = meta
        return this
    }

    fun toItemStack(): ItemStack = item
}