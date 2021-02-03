package com.holland.db.service.impl.mysql

import com.holland.db.DBController
import com.holland.db.service.FetchTables
import com.holland.db.service.TableTemplate

@Suppress("unused", "SqlDialectInspection", "SqlNoDataSourceInspection")
class MysqlFetchTablesImpl(private val dbController: DBController) : FetchTables {
    override fun execute(): List<TableTemplate> {
        val result = mutableListOf<TableTemplate>()
        val statement =
            dbController.connection.prepareStatement("select table_name,table_comment from information_schema.tables where table_schema=?")
        statement.setString(1, dbController.schema)
        statement.execute()
        statement.resultSet.apply {
            while (next()) {
                result.add(
                    TableTemplate(
                        getString("table_name"),
                        "TABLE",
                        getString("table_comment")
                    )
                )
            }
        }

        try {
            statement.resultSet.close()
        } finally {
            try {
                statement.close()
            } finally {
//                dbController.connection.close()
            }
        }

        return result
    }
}