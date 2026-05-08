package net.ivanvega.mikmpnetworkinganddatastorage.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import net.ivanvega.mikmpnetworkinganddatastorage.AppDatabase
import java.util.Properties

class JvmDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        return JdbcSqliteDriver("jdbc:sqlite:launch.db", Properties(),
            AppDatabase.Schema)
    }
}