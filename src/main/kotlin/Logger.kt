package org.arjix

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Logger {
    companion object {
        val INSTANCE = LoggerFactory.getLogger(::main.javaClass)
    }
}
