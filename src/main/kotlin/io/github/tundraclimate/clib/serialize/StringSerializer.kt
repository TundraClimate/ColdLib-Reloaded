package io.github.tundraclimate.clib.serialize

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class StringSerializer: Base64Serializer<String> {
    override fun encode(t: String): String? {
        val os = ByteArrayOutputStream()
        val o = ObjectOutputStream(os)
        return try {
            o.writeChars(t)

            Base64Coder.encodeLines(os.toByteArray())
        } catch (e: Exception) {
            null
        } finally {
            os.close()
            o.close()
        }
    }

    override fun decode(str: String): String? {
        val ips = ByteArrayInputStream(Base64Coder.decodeLines(str))
        val ois = ObjectInputStream(ips)
        return try {
            ois.readUTF()
        } catch (e: Exception) {
            null
        } finally {
            ips.close()
            ois.close()
        }
    }
}