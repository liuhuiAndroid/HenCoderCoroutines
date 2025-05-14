package com.rengwuxian.coursecoroutines._4_flow

import com.rengwuxian.coursecoroutines.common.gitHub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：用 produce() 来提供跨协程的事件流
 * 通过 produce 创建一个内部提供了 SendChannel 的协程
 * 可以通过 send 函数生产数据，提供给其他协程使用
 * 其他协程使用 produce 返回的 ReceiveChannel 调用 receive 来获取数据
 * 把 produce 当成多条数据版的 async 来用 => 将 Channel 的创建和发送合并到了一起
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
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