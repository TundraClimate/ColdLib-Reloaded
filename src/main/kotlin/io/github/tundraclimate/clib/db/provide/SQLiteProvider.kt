package io.github.tundraclimate.clib.db.provide

import io.github.tundraclimate.clib.db.CreateType
import io.github.tundraclimate.clib.db.SQLite
import java.sql.ResultSet

class SQLiteProvider private constructor(private val sqlite: SQLite) {
    fun executeCreate(type: CreateType) {
        sqlite.exec { _, state ->
            val sql = "CREATE $type"
            state.executeUpdate(sql)
        }
    }

    fun executeInsert(table: String, vararg values: String) {
        val sql = "INSERT INTO $table VALUES(${values.reduce { it, _ -> "$it ?," }.removeSuffix(",")})"
        sqlite.exec { _, _ ->
            sqlite.preparedState(sql) { pres ->
                values.forEachIndexed { index, n ->
                    pres.setString(index + 1, n)
                }
                pres.executeUpdate()
            }
        }
    }

    fun executeUpdate(table: String, where: String = "", vararg newValues: String) {
        val sql = "UPDATE $table SET ${newValues.reduce { it, _ -> "$it ?," }.removeSuffix(",")} $where"
        sqlite.exec { _, _ ->
            sqlite.preparedState(sql) { pres ->
                newValues.forEachIndexed { index, n ->
                    pres.setString(index + 1, n)
                }
                pres.executeUpdate()
            }
        }
    }

    fun executeDelete(table: String, where: String = "") {
        val sql = "DELETE FROM $table $where"
        sqlite.exec { _, state ->
            state.executeUpdate(sql)
        }
    }

    fun executeSelect(table: String, where: String, vararg columns: String): ResultSet {
        val sql = "SELECT ${columns.reduce { it, next -> "$it $next" } } FROM $table $where"
        var resultSet: ResultSet? = null
        sqlite.exec { _, state ->
            val result = state.executeQuery(sql)
            resultSet = result
        }
        return resultSet!!
    }

    companion object {
        @JvmStatic
        fun create(sqLite: SQLite): SQLiteProvider {
            return SQLiteProvider(sqLite)
        }
    }
}