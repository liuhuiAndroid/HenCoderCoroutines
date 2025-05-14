package com.rengwuxian.coursecoroutines._1_basics

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.rengwuxian.coursecoroutines.R
import com.rengwuxian.coursecoroutines.common.gitHub
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 标题：回到线程世界：runBlocking()
 * runBlocking 可以提供协程环境
 * 特点 1. 不需要 coroutineScope；因为既不需要上下文，又不需要被取消
 * 特点 2. 阻塞当前线程并开始运行，直到自己执行完毕
 * 作用 1：把协程格式的代码块封装起来，变成阻塞式，让传统的线程写法的 API 使用
 * 作用 2：写测试代码
 *
 * 启动协程的函数：launch   async   runBlocking
 */
fun main() = runBlocking<Unit> {
    val contributors = gitHub.contributors("square", "retrofit")
    launch {

    }
}

class RunBlockingActivity : ComponentActivity() {
    private lateinit var infoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_1)
        infoTextView = findViewById(R.id.infoTextView)

        lifecycleScope.launch(Dispatchers.Main.immediate) {

        }
        println()
        lifecycleScope.async { }
        blockingContributors()
        println()
    }

    private fun blockingContributors() = runBlocking {
        gitHub.contributors("square", "retrofit")
    }

}