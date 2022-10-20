package io.github.tundraclimate.clib.serialize

import org.bukkit.util.Vector
import org.bukkit.util.io.BukkitObjectOutputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayOutputStream

class VectorSerializer: Base64Serializer<Vector> {
    override fun encode(t: Vector): String? {
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