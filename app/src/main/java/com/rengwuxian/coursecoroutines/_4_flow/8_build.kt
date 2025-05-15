package com.rengwuxian.coursecoroutines._4_flow

import com.rengwuxian.coursecoroutines.common.Contributor
import com.rengwuxian.coursecoroutines.common.gitHub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

/**
 * 标题：Flow 的创建
 */
fun main() = runBlocking<Unit> {
    val flow1 = flowOf(1, 2, 3)
    val flow2 = listOf(1, 2, 3).asFlow()
    val flow3 = setOf(1, 2, 3).asFlow()
    val flow4 = sequenceOf(1, 2, 3).asFlow()
    val channel = Channel<Int>()
    // consumeAsFlow Hot；只能被 collect 一次；collect 之后 channel 会被关闭；用于你打算彻底消费 channel 并关闭它的情况。
    val flow5 = channel.consumeAsFlow()
    // receiveAsFlow Hot；多次 collect 数据会被瓜分；多次 collect 是安全的
    val flow6 = channel.receiveAsFlow()
    // channelFlow Cold；适合跨协程生产需求
    val flow7 = channelFlow {
        launch { // 可以启动子协程；flow 函数启动不了子协程，不允许换协程
            delay(2000)
            send(2)
        }
        delay(1000)
        send(1)
    }
    val flow8 = flow {
        launch {
            delay(2000)
            emit(2)
        }
        delay(1000)
        emit(1)
    }
    // channelFlow 和 callbackFlow 用途一样，区别是强制调用 awaitClose
    // 如果是单次可以使用 suspendCancellableCoroutine 替代 callbackFlow
    // callbackFlow 可以看作是 flow 版的 suspendCancellableCoroutine
    val flow9 = callbackFlow {
        gitHub.contributorsCall("square", "retrofit")
            .enqueue(object : Callback<List<Contributor>> {
                override fun onResponse(
                    call: Call<List<Contributor>>,
                    response: Response<List<Contributor>>,
                ) {
                    trySend(response.body()!!)
                    close() // 手动关闭 Channel
                }

                override fun onFailure(call: Call<List<Contributor>>, error: Throwable) {
                    cancel(CancellationException(error))
                }
            })
        awaitClose() // 挂起协程，否则回调还没调用就关闭了
    }
    val scope = CoroutineScope(EmptyCoroutineContext)
    scope.launch {
        flow9.collect {
            println("channelFlow with callback: $it")
        }
//        flow8.collect {
//            println("channelFlow: $it")
//        }
//        flow5.collect {
//            println("Flow6 - 1: $it")
//        }
    }
    scope.launch {
//        flow5.collect {
//            println("Flow6 - 2: $it")
//        }
    }
//    channel.send(1)
//    channel.send(2)
//    channel.send(3)
//    channel.send(4)
    delay(10000)
}