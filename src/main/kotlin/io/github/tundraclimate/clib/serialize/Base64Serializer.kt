package io.github.tundraclimate.clib.serialize

import org.bukkit.util.io.BukkitObjectInputStream
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream

interface Base64Serializer<T> {
    fun encode(t: T): String?
    fun decode(str: String): T? {
        val bis = ByteArrayInputStream(Base64Coder.decodeLines(str))
        val bois = BukkitObjectInputStream(bis)
        return try {
            val r = bois.readObject()
            r as? T
        } catch (e: Exception) {
            null
        } finally {
            bis.close()
            bois.close()
        }
    }

    fun encodeNotNull(t: T): String = encode(t)!!
    fun decodeNotNull(str: String): T = decode(str)!!
}