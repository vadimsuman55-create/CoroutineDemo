package com.shumtech.coroutinedemo

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import com.shumtech.coroutinedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var count: Int = 1
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar?, progress: Int, fromUser: Boolean) {
                count = if (progress < 1) 1 else progress
                binding.countText.text = "$count coroutines"
            }
            override fun onStartTrackingTouch(seek: SeekBar?) {}
            override fun onStopTrackingTouch(seek: SeekBar?) {}
        })
    }

    suspend fun performTask(taskNumber: Int): Deferred<String> =
        coroutineScope.async(Dispatchers.Main) {
            delay(5_000)
            "Finished Coroutine $taskNumber"
        }

    fun launchCoroutines(view: View) {
        for (i in 1..count) {
            binding.statusText.text = "Started Coroutine $i"
            coroutineScope.launch(Dispatchers.Main) {
                val result = performTask(i).await()
                binding.statusText.text = result
            }
        }
    }
}