package io.github.tundraclimate.clib.json

sealed interface JsonValueType {
    data class STRING(val value: String): JsonValueType
    data class NUMBER(val value: Double): JsonValueType
    data class BOOL(val value: Boolean): JsonValueType
    object ARRAY: JsonValueType
    object OBJECT: JsonValueType
    object NULL: JsonValueType
}