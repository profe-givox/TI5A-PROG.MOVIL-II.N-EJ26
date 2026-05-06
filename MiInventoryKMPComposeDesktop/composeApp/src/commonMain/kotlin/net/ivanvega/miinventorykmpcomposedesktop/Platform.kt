package net.ivanvega.miinventorykmpcomposedesktop

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform