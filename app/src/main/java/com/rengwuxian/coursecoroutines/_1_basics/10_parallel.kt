package com.rengwuxian.coursecoroutines._1_basics

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.rengwuxian.coursecoroutines.R
import com.rengwuxian.coursecoroutines.common.Contributor
import com.rengwuxian.coursecoroutines.common.gitHub
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * 标题：并行协程的启动和交互
 * async 有返回值可以进行并行协程之间的交互
 */
class ParallelActivity : ComponentActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var infoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_1)
        infoTextView = findViewById(R.id.infoTextView)

        lifecycleScope.launch {
            coroutineScope {
                // 并行
                val deferred1 = async { gitHub.contributors("square", "retrofit") }
                val deferred2 = async { gitHub.contributors("square", "okhttp") }
                showContributors(deferred1.await() + deferred2.await())
            }
        }
        lifecycleScope.launch {
            val initJob = launch {
//                init()
            }
            val contributors1 = gitHub.contributors("square", "retrofit")
            // launch 的返回值Job 调用 join，但是会挂起函数。Job 的 join 类似于 Thread 的 join
            initJob.join()
//            processData()
        }
    }

    private fun coroutinesStyle() = lifecycleScope.launch {
        // 串行，前一个线程的生产结果可以交给后一个线程使用
        val contributors1 = gitHub.contributors("square", "retrofit")
        val contributors2 = gitHub.contributors("square", "okhttp")
        showContributors(contributors1 + contributors2)
    }

    /**
     * Java 8 实现
     */
    private fun completableFutureStyleMerge() {
        val future1 = gitHub.contributorsFuture("square", "retrofit")
        val future2 = gitHub.contributorsFuture("square", "okhttp")
        future1.thenCombine(future2) { contributors1, contributors2 ->
            contributors1 + contributors2
        }.thenAccept { mergedContributors ->
            handler.post {
                showContributors(mergedContributors)
            }
        }
    }

    private fun showContributors(contributors: List<Contributor>) =
        contributors.map { "${it.login} (${it.contributions})" }.reduce { acc, s -> "$acc\n$s" }
            .let { infoTextView.text = it }
}