package io.github.tundraclimate.clib.db

sealed interface CreateType {
    private interface ICreateType<out T : CreateType> : CreateType {
        fun more(moreOptions: String): T
    }

    class TABLE(name: String, vararg columns: String) : ICreateType<TABLE> {
        private val sql = StringBuilder("TABLE IF NOT EXISTS ")

        init {
            sql.append(name)
            sql.append("(${columns.reduce { it, next -> "$it, $next" }})")
        }

        override fun more(moreOptions: String): TABLE {
            sql.append(" $moreOptions")
            return this
        }

        override fun toString(): String {
            return sql.toString()
        }
    }

    class VIEW(name: String, select: String) : ICreateType<VIEW> {
        private val sql = StringBuilder("VIEW ")

        init {
            sql.append(name)
            sql.append(" AS ")
            sql.append(select)
        }

        override fun more(moreOptions: String): VIEW {
            sql.append(" $moreOptions")
            return this
        }

        override fun toString(): String {
            return sql.toString()
        }
    }

    class INDEX(name: String, isUnique: Boolean, table: String, vararg columns: String) : ICreateType<INDEX> {
        private val sql = StringBuilder()

        init {
            if (isUnique) sql.append("UNIQUE ")
            sql.append("INDEX ")
            sql.append(name)
            sql.append(" ON ")
            sql.append("$table(${columns.reduce { it, next -> "$it, $next" }})")
        }

        override fun more(moreOptions: String): INDEX {
            sql.insert(sql.length - 1, ", $moreOptions")
            return this
        }

        override fun toString(): String {
            return sql.toString()
        }
    }

    class TRIGGER(name: String, timing: String, table: String, where: String = "", vararg foreach: String) :
        ICreateType<TRIGGER> {
        private val sql = StringBuilder("TRIGGER ")

        init {
            sql.append("$name ")
            sql.append(timing)
            sql.append(" ON ")
            sql.append("$table ")
            if (where.isNotEmpty()) sql.append("$where ")
            sql.append("BEGIN ")
            sql.append(foreach.reduce { it, next -> "$it; $next" })
            sql.append(";")
            sql.append("END")
        }

        override fun more(moreOptions: String): TRIGGER {
            sql.insert(sql.length - 4, moreOptions)
            return this
        }

        override fun toString(): String {
            return sql.toString()
        }
    }
}
