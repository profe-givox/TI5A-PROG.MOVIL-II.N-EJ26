package net.ivanvega.mikmpnetworkinganddatastorage

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform