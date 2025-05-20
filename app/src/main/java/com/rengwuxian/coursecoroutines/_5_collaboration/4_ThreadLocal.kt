package com.rengwuxian.coursecoroutines._5_collaboration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asContextElement
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext

val kotlinLocalString = ThreadLocal<String>()

/**
 * 标题：ThreadLocal
 * ThreadLocal：线程的局部变量
 * CoroutineContext 就是协程里的 ThreadLocal；协程的局部变量
 */
fun main() = runBlocking {
    val scope = CoroutineScope(EmptyCoroutineContext)
    scope.launch {
        // kotlinLocalString.set() 不行
        val stringContext = kotlinLocalString.asContextElement("rengwuxian") // 转换成 CoroutineContext
        withContext(stringContext) {
            delay(100)
            kotlinLocalString.get()
        }
    }
    delay(10000)
}