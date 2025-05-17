package com.rengwuxian.coursecoroutines._3_scope_context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * 标题：自定义 CoroutineContext
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val customContext = Logger()
    scope.launch(customContext) {
        coroutineContext[Logger]?.log()
    }
    delay(10000)
}

class Logger : AbstractCoroutineContextElement(Logger) {
    // 参照 CoroutineName 声明 Key
    companion object Key : CoroutineContext.Key<Logger>

    // 实现比如日志功能
    suspend fun log() {
        println("Current coroutine: $coroutineContext")
    }
}

///**
// * User-specified name of coroutine. This name is used in debugging mode.
// * See [newCoroutineContext][CoroutineScope.newCoroutineContext] for the description of coroutine debugging facilities.
// */
//public data class CoroutineName(
//    /**
//     * User-defined coroutine name.
//     */
//    val name: String
//) : AbstractCoroutineContextElement(CoroutineName) {
//    /**
//     * Key for [CoroutineName] instance in the coroutine context.
//     */
//    public companion object Key : CoroutineContext.Key<CoroutineName>
//
//    /**
//     * Returns a string representation of the object.
//     */
//    override fun toString(): String = "CoroutineName($name)"
//}
