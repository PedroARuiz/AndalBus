package org.edrodev.andalbus

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform