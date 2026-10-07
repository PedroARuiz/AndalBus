package org.edrodev.andalbus

class Greeting {
    private val platform = getPlatform()

    fun greet(): String = sayHello(platform.name)
}
