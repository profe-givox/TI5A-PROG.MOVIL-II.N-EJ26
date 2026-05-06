package net.ivanvega.miprimierkmpcompose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform