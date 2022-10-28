package io.github.tundraclimate.clib.json

class JsonNode(
    val name: String = "",
    val value: JsonValueType = JsonValueType.NULL,
    val childs: List<JsonNode> = emptyList()
)