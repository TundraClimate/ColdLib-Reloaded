package io.github.tundraclimate.clib.serialize

import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayOutputStream

class ItemStackSerializer: Base64Serializer<ItemStack> {
    override fun encode(t: ItemStack): String? {
        val os = ByteArrayOutputStream()
        val bos = BukkitObjectOutputStream(os)
        return try {
            bos.writeObject(t)

            Base64Coder.encodeLines(os.toByteArray())
        } catch (e: Exception) {
            null
        } finally {
            os.close()
            bos.close()
        }
    }
}