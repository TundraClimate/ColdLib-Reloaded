package io.github.tundraclimate.clib.db.provide

import io.github.tundraclimate.clib.db.CreateType
import io.github.tundraclimate.clib.db.SQLite

class BufferedSQLiteProvider(private val sqlite: SQLite) {
    private val buff = mutableListOf<Pair<String, List<String>?>>()

    fun executeCreate(type: CreateType) {
        val sql = "CREATE $type"
        buff.add(sql to null)
    }

    fun executeInsert(table: String, vararg values: String) {
        val sql = "INSERT INTO $table VALUES(${values.reduce { it, _ -> "$it, ?" }})"
        buff.add(sql to values.toList())
    }

    fun executeUpdate(table: String, where: String = "", vararg newValues: String) {
        val sql = "UPDATE $table SET ${newValues.reduce { it, _ -> "$it, ?" }} $where"
        buff.add(sql to newValues.toList())
    }

    fun executeDelete(table: String, where: String = "") {
        val sql = "DELETE FROM $table $where"
        buff.add(sql to null)
    }

    fun flush() {
        sqlite.exec { _, state ->
            buff.forEach {
                if (it.second != null) {
                    sqlite.preparedState(it.first) { pres ->
                        it.second!!.forEachIndexed { index, n ->
                            pres.setString(index + 1, n)
                        }
                        pres.executeUpdate()
                    }
                } else {
                    state.executeUpdate(it.first)
                }
            }
        }
    }
}