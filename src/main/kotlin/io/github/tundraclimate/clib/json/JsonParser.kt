package io.github.tundraclimate.clib.json

import java.lang.StringBuilder

class JsonParser(private val root: JsonNode) {
    fun parse(): String {
        val res = StringBuilder()
        fun p(node: JsonNode) {
            if (node.name.isNotEmpty()) {
                res.append("\"${node.name}\":")
            }
            when (node.value) {
                is JsonValueType.STRING -> {
                    res.append("\"${node.value.value}\"")
                }
                is JsonValueType.ARRAY -> {
                    res.append("[")
                    node.childs.forEach { p(it) }
                    res.deleteAt(res.length - 1)
                    res.append("]")
                }
                is JsonValueType.BOOL -> {
                    res.append(node.value.value)
                }
                JsonValueType.NULL -> {
                    if (node.childs.isNotEmpty()) {
                        res.append("{")
                        node.childs.forEach { p(it) }
                        res.deleteAt(res.length - 1)
                        res.append("}")
                    } else res.append("null")
                }
                is JsonValueType.NUMBER -> {
                    res.append(node.value.value)
                }
                is JsonValueType.OBJECT -> {
                    res.append("{")
                    node.childs.forEach { p(it) }
                    res.deleteAt(res.length - 1)
                    res.append("}")
                }
            }
            res.append(",")
        }
        p(root)
        res.deleteAt(res.length - 1)
        return res.toString()
    }

    override fun toString(): String {
        return parse()
    }
}