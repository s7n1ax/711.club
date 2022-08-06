package me.bush.cornerstore.util.system

import com.google.gson.*
import org.apache.commons.io.IOUtils
import java.io.BufferedReader
import java.io.BufferedWriter
import java.nio.file.*
import kotlin.io.path.isDirectory
import kotlin.reflect.KClass
import kotlin.streams.toList

fun KClass<*>.getResourceAsString(path: String): String = IOUtils.toString(
    java.classLoader.getResourceAsStream(path.let { if (it.startsWith("/")) it.drop(1) else it }), "UTF-8"
)

inline val String.isValidPath get() = runCatching { Paths.get(this); true }.getOrElse { false }

inline val Path.exists get() = Files.exists(this)

inline val Path.subDirectories get() = Files.walk(also { createPath() }, 1).filter { it.isDirectory() && it !== this }.toList()

inline val Path.writer: BufferedWriter get() = Files.newBufferedWriter(this)

inline val Path.reader: BufferedReader get() = Files.newBufferedReader(this)

/**
 * Creates the given path and file. Returns false if it already exists.
 */
fun Path.createFile(): Boolean = if (exists) false else {
    if (false == parent?.exists) Files.createDirectories(parent)
    Files.createFile(this)
    true
}

/**
 * Creates the given path. Returns false if it already exists.
 */
fun Path.createPath(): Boolean = if (exists) false else {
    Files.createDirectories(this)
    true
}

/**
 * Deletes this path and all subdirectories.
 */
fun Path.deleteAll() {
    Files.walk(this).sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
}

val GSON: Gson = GsonBuilder().setPrettyPrinting().setLenient().create()

fun Path.writeJson(jsonElement: JsonElement) {
    createFile()
    writer.use {
        GSON.toJson(jsonElement, it)
    }
}

val PARSER = JsonParser()

/**
 * Attempts to read a [JsonElement] from the calling path. Does not catch any errors.
 */
fun Path.readJson(): JsonElement = PARSER.parse(reader)
