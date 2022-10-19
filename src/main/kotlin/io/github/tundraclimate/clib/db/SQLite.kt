package io.github.tundraclimate.clib.db

import io.github.tundraclimate.clib.ColdLib
import io.github.tundraclimate.clib.Result
import io.github.tundraclimate.clib.db.provide.SQLiteProvider
import java.io.File
import java.sql.*

class SQLite private constructor(private val dbpath: File): DataBase {
    private lateinit var con: Connection
    private lateinit var state: Statement
    private lateinit var pres: PreparedStatement

    fun exec(execute: (con: Connection, state: Statement) -> Unit): Result<Unit, SQLException> {
        try {
            File(ColdLib.plugin.dataFolder, dbpath.path).let {
                if (!File(it.parent).exists()) File(it.parent).mkdirs()
                con = DriverManager.getConnection("jdbc:sqlite:${it.path}")
            }
            state = con.createStatement()
            execute(con, state)
        } catch (e: SQLException) {
            return Result.err(e)
        } finally {
            state.close()
            con.close()
        }
        return Result.ok(Unit)
    }

    fun preparedState(
        sql: String,
        resultSetType: Int = -1,
        resultSetConcurrency: Int = -1,
        resultSetHoldability: Int = -1,
        execute: (pres: PreparedStatement) -> Unit
    ) {
        try {
            pres =
                if (resultSetType != -1 && resultSetConcurrency == -1 && resultSetHoldability == -1) con.prepareStatement(
                    sql, resultSetType
                )
                else if (resultSetType != -1 && resultSetConcurrency != -1 && resultSetHoldability == -1) con.prepareStatement(
                    sql, resultSetType, resultSetConcurrency
                )
                else if (resultSetType != -1 && resultSetConcurrency != -1) con.prepareStatement(
                    sql, resultSetType, resultSetConcurrency, resultSetHoldability
                )
                else con.prepareStatement(sql)
            execute(pres)
        } finally {
            pres.close()
        }
    }

    fun createProvider(): SQLiteProvider {
        return SQLiteProvider.create(this)
    }

    companion object {
        @JvmStatic
        fun create(
            dbpath: File, initialize: SQLite.(con: Connection, state: Statement) -> Unit = { _, _ -> }
        ): SQLite {
            Class.forName("org.sqlite.JDBC")
            if (!ColdLib.plugin.dataFolder.exists()) ColdLib.plugin.dataFolder.mkdirs()
            val sqLite = SQLite(dbpath)
            sqLite.exec { con, state ->
                sqLite.initialize(con, state)
            }
            return sqLite
        }

    }
}