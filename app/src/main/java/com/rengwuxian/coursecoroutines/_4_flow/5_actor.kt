package com.rengwuxian.coursecoroutines._4_flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：actor()：把 SendChannel 暴露出来
 * produce => 创建 Channel 并返回 ReceiveChannel 用于接收数据
 * actor   => 创建 Channel 并返回 SendChannel    用于发送数据
 */
@OptIn(ObsoleteCoroutinesApi::class)
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val sender = scope.actor<Int> {
        for (num in this) {
            println("Number: $num")
        }
    }
    scope.launch {
        for (num in 1..100) {
            sender.send(num)
            delay(1000)
        }
    }
    delay(10000)
}