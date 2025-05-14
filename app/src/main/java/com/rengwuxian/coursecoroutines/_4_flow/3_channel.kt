package com.rengwuxian.coursecoroutines._4_flow

import com.rengwuxian.coursecoroutines.common.Contributor
import com.rengwuxian.coursecoroutines.common.gitHub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：Channel 的工作模式详解
 * Channel 本质上是一个挂起队列 => 协程版的 BlockingQueue
 * 可以使用 Channel 实现简单的事件订阅功能
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    // Channel 是一个接口，这是一个工厂函数
    val channel = Channel<List<Contributor>>()
    scope.launch {
        // 在一个协程里发生数据
        channel.send(gitHub.contributors("square", "retrofit"))
    }
    scope.launch {
        while (isActive) {
            // 在另一个协程里获取数据
            channel.receive()
        }
    }
    scope.launch {
        while (isActive) {
            // 多个 receive 只会有一个能读取到数据
            channel.receive()
        }
    }
    // produce 是对 Channel 的封装。捆绑了 Channel 的创建和发送数据
    val receiver = scope.produce {
        while (isActive) {
            val data = gitHub.contributors("square", "retrofit")
            send(data)
        }
    }
    launch {
        delay(5000)
        while (isActive) {
            println("Contributors: ${receiver.receive()}")
        }
    }
    delay(10000)
}