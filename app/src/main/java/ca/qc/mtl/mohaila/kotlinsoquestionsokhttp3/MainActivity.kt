package ca.qc.mtl.mohaila.kotlinsoquestionsokhttp3

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = Adapter()
        recyclerview.setHasFixedSize(true)
    }

    override fun onResume() {
        super.onResume()
        getQuestions()
    }

    private fun getQuestions() {
        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()
            val request = Request.Builder().url(BASE_URL).build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val stream = response.body()!!.charStream()
                    val reader = BufferedReader(stream)
                    val questions = Gson().fromJson(reader, Questions::class.java)
                    reader.close()

                    CoroutineScope(Dispatchers.Main).launch {
                        val adapter = recyclerview.adapter as Adapter
                        adapter.setQuestions(questions.items)
                    }
                }
            })
        }
    }

    companion object {
        const val BASE_URL =
            "https://api.stackexchange.com/2.1/questions?order=desc&sort=creation&site=stackoverflow&tagged=kotlin"
    }
}
