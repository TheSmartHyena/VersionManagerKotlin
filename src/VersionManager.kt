package com.example.myapplication_test

import kotlin.math.min

enum class UpdateType {
    PATCH, MINOR, MAJOR
}

const val DEFAULT_VERSION = "1.O.0"

class VersionManager() {
    private var initialVersion = ""
    private val versions: MutableList<String> = ArrayList()

    init {
        this.versions.add(DEFAULT_VERSION)
        this.initialVersion = DEFAULT_VERSION
    }

    constructor(version: String) : this() {
        this.initialVersion = version

        val splitted = version.split(".")
        val result = arrayOfNulls<String>(3)

        val indexMax = min(splitted.size.toDouble(), 2.0).toInt()
        val indexMin = splitted.size

        for (i in 0..indexMax) {
            result[i] = if (this.isNumeric(splitted[i])) splitted[i] else "0"
        }

        for (i in indexMin..2) {
            result[i] = "0"
        }

        this.versions.add(result.joinToString("."))
    }

    fun rollback(): VersionManager {
        if (this.versions.isNotEmpty()) {
            this.versions.removeLast()
        } else {
            println("Cannot rollback!")
        }
        return this
    }

    private fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }

    private fun genericUpdate(index: Int) {
        val splitted: MutableList<String> = this.versions.last().split(".").toMutableList()
        val newValue = (splitted[index].toInt() + 1)

        splitted[index] = newValue.toString()

        for (i in index + 1..2) {
            splitted[i] = "0"
        }

        this.versions.add(splitted.joinToString("."))
    }

    fun major(): VersionManager {
        this.genericUpdate(0)
        return this
    }

    fun minor(): VersionManager {
        this.genericUpdate(1)
        return this
    }

    fun patch(): VersionManager {
        this.genericUpdate(2)
        return this
    }

    fun update(type: UpdateType): VersionManager {
        return when(type) {
            UpdateType.PATCH -> this.patch()
            UpdateType.MINOR -> this.minor()
            UpdateType.MAJOR -> this.major()
        }
    }

    fun release(): String {
        if (this.versions.isNotEmpty()) {
            return this.versions.last()
        }

        return ""
    }

    fun reset() {
        this.versions.clear()
        this.versions.add(this.initialVersion)
    }
}