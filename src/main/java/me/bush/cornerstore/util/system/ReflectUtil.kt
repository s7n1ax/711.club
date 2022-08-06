package me.bush.cornerstore.util.system

import me.bush.cornerstore.CornerStore
import me.bush.cornerstore.api.common.mc
import me.bush.cornerstore.util.lang.async
import me.bush.cornerstore.util.timer.MilliTimer
import net.minecraft.crash.CrashReport
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ConfigurationBuilder
import sun.misc.Unsafe
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

/**
 * List of classes in our client. Will be null after client is loaded.
 */
private var CLASSES: List<KClass<out Any>>? by async {
    Reflections(
        ConfigurationBuilder()
            .forPackages("me.bush.cornerstore")
            .setScanners(SubTypesScanner(false))
    ).getSubTypesOf(Any::class.java).mapNotNull { it.toKotlin }
}

/**
 * Checks the metadata of a class to check if it is a valid kotlin class.
 */
val Class<*>.toKotlin get() = if (getAnnotation(Metadata::class.java)?.kind == 1) kotlin else null

/**
 * Returns a set of subclasses for the calling class.
 */
@Suppress("UNCHECKED_CAST") // FUCK OFF
val <T : Any> KClass<T>.subclasses
    get() = CLASSES!!.filter { isSuperclassOf(it) && it != this } as List<KClass<out T>>

/**
 * Returns a member with a matching name. Will throw NPE if there are no matches.
 */
operator fun KClass<*>.get(member: String) = allMembers.find { it.name == member }!!

/**
 * Returns all members (properties + methods) of a [KClass], including inherited
 */
val <T : Any> KClass<T>.allMembers
    get() = (declaredMembers + allSuperclasses.flatMap { it.declaredMembers }).asSequence()

/**
 * Returns true if the calling [KClass] has at least one method with the given return type.
 */
inline fun <reified T : Any> KClass<*>.hasMethodsOfType() = declaredFunctions.any { it.returnType == typeOf<T>() }

/**
 * Returns a list of the values of every property with type T (or subtypes) for the calling instance
 */
@Suppress("UNCHECKED_CAST") // Holy fucking shit Kotlin star projections are so bad
inline fun <reified T : Any> Any.getPropertiesOfType(): Sequence<T> = this::class.allMemberProperties.mapNotNull {
    // Can't set accessible here, for some reason it resets when casting
    when {
        it.returnType.isSubtypeOf(typeOf<T>()) -> {
            // Much security
            it.isAccessible = true
            it.handleCall(this) as T
        }
        it is KProperty0<*> -> {
            it.isAccessible = true
            // If the delegate is not T, return null
            it.getDelegate() as? T
        }
        it is KProperty1<*, *> -> {
            it.isAccessible = true
            (it as KProperty1<Any, *>).getDelegate(this) as? T
        }
        else -> null
    }
}

/**
 * Returns all properties of the calling class, including inherited properties
 */
val <T : Any> KClass<T>.allMemberProperties get() = allMembers.filterIsInstance<KProperty<*>>()

/**
 * Returns the name of the closest package. For example, java.lang.String would return "lang"
 */
fun KClass<*>.getClosestPackageName(): String? {
    val packages = qualifiedName?.split(".")
    return packages?.get(packages.size - 2)
}

/**
 * Call stuff regardless of if its static or not.
 */
fun <R> KCallable<R>.handleCall(receiver: Any? = null): R {
    isAccessible = true
    return runCatching { call(receiver) }.getOrElse { call() }
}

object ReflectUtil {

    /**
     * Pro hacher shit
     */
    val UNSAFE = Unsafe::class["theUnsafe"].handleCall() as Unsafe

    fun warmup() {
        // Doesn't matter what class we use, this just helps
        // reflection work smoother if we do something first.
        val instance = MilliTimer::class.createInstance()
        MilliTimer::class["time"].handleCall(instance)
        // Calling this function also initializes our CLASSES
        // and UNSAFE so they can load in the background before we need them.
    }

    fun cleanup() {
        // Null because its a very big list that we don't really need,
        // This will let gc know it isn't needed anymore.
        CLASSES = null
    }

    fun antiFeather() {
        runCatching {
            Class.forName("net.digitalingot.FeatherMod")
        }.onSuccess {
            mc.crashed(
                CrashReport(
                    """
            ${CornerStore.NAME}: Feather Client was detected.
            
            Shutting down because Feather is cringe. You shouldn't have bought glorified Forge LOL!
            
            Remember: ${CornerStore.NAME} on top!
            
             /$$$$$$$$  /$$     /$$        /$$$$$$  /$$       /$$   /$$ /$$$$$$$ 
            |_____ $$//$$$$   /$$$$       /$${"$"}__  $$| $$      | $$  | $$| $${"$"}__  $$
                 /$$/|_  $$  |_  $$      | $$  \__/| $$      | $$  | $$| $$  \ $$
                /$$/   | $$    | $$      | $$      | $$      | $$  | $$| $$$$$$$ 
               /$$/    | $$    | $$      | $$      | $$      | $$  | $$| $${"$"}__  $$
              /$$/     | $$    | $$      | $$    $$| $$      | $$  | $$| $$  \ $$
             /$$/     /$$$$$$ /$$$$$$ /$$|  $$$$$$/| $$$$$$$$|  $$$$$$/| $$$$$$$/
            |__/     |______/|______/|__/ \______/ |________/ \______/ |_______/   
            """.trimIndent(), Exception("Feather sux cope")
                )
            )
        }
    }
}
