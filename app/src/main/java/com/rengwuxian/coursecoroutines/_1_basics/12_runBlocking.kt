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