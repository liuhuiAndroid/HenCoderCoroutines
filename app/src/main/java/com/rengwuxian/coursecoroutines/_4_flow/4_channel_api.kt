package com.rengwuxian.coursecoroutines._4_flow

import com.rengwuxian.coursecoroutines.common.Contributor
import com.rengwuxian.coursecoroutines.common.gitHub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.FileWriter
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 标题：Channel API 详解
 *
 * channel.send(api.get("xxx"))     // 挂起函数，如果队列满了会挂起协程
 * channel.receive()
 * channel.close() 	                // 不再发送数据了，属于 SendChannel => 属性 isClosedForSend 标记是否能发送数据，true 不允许 send()，抛出 ClosedSendChannelException; 依旧可以 receive() 获取缓冲区数据/挂机的send
 * channel.cancel()	                // 不再接收数据了，属于 ReceiveChannel => 属性 isClosedForReceive 标记是否能接收数据，之后再调用 receive()/send() 抛出 CancellationException 异常；缓冲区数据/挂机的send数据也失效；直接两头关闭
 * channel.close(IllegalStateException("xxx"))     // 自定义关闭异常
 * channel.trySend(api.get("xxx"))  // 非挂起函数，不等待；返回 ChannelResult 包含 isSuccess、isFailure、isClosed
 * channel.tryReceive()             // 非挂起函数，不等待；返回 ChannelResult 包含 isSuccess、isFailure、isClosed
 */
fun main() = runBlocking<Unit> {
    val scope = CoroutineScope(EmptyCoroutineContext)
    val fileChannel = Channel<FileWriter> { it.close() }
    fileChannel.send(FileWriter("test.txt"))
    // Channel capacity 参数：缓存区容量，默认为 RENDEZVOUS = 0 没有缓冲亲手交接；UNLIMITED；
    // Channel onBufferOverflow 参数：缓冲溢出策略，默认为 SUSPEND 所有的数据最终都必须被处理; DROP_OLDEST 溢出会丢弃队首; DROP_LATEST 溢出会丢弃新元素
    // Channel onUndeliveredElement 参数：未交接的元素，定义回调处理已发送但最终被丢弃而不是被接受的数据；比如处理文件流回调
//    val channel = Channel<List<Contributor>>(8, BufferOverflow.DROP_OLDEST)
//    val channel = Channel<List<Contributor>>(1, BufferOverflow.DROP_OLDEST)
    val channel = Channel<List<Contributor>>(CONFLATED) // 特殊模式 = Channel(1, onBufferOverflow = BufferOverflow. DROP_LATEST)
    scope.launch {
        channel.send(gitHub.contributors("square", "retrofit"))
        channel.close()
        channel.close(IllegalStateException("Data error!"))
        channel.receive()
        channel.receive()
        channel.send(gitHub.contributors("square", "retrofit"))
        channel.trySend(gitHub.contributors("square", "retrofit"))
        channel.tryReceive()
    }
    launch {
        // 实现了 iterator() 支持 for 循环，挂起式 => public operator fun iterator(): ChannelIterator<E>
        for (data in channel) {
            println("Contributors: $data")
        }
//        while (isActive) {
//          val contributors = channel.receive()
//          println("Contributors: $contributors")
//        }
    }
    delay(1000)
    channel.cancel()
    delay(10000)
}