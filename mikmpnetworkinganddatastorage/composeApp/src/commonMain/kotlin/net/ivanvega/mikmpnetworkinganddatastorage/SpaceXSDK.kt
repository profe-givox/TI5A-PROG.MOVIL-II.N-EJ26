package net.ivanvega.mikmpnetworkinganddatastorage

import net.ivanvega.mikmpnetworkinganddatastorage.cache.Database
import net.ivanvega.mikmpnetworkinganddatastorage.cache.DatabaseDriverFactory
import net.ivanvega.mikmpnetworkinganddatastorage.entity.RocketLaunch
import net.ivanvega.mikmpnetworkinganddatastorage.network.SpaceXApi


class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory,
                val api: SpaceXApi = SpaceXApi()
) {
    private val database = Database(databaseDriverFactory)

    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunches().also {
                database.clearAndCreateLaunches(it)
            }
        }
    }
}