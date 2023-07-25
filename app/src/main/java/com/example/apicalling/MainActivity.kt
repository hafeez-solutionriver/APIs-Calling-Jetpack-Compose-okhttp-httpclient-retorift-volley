package com.example.apicalling

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                verticalArrangement = Arrangement.SpaceBetween
            )
            {

                APiCallingButton(btnText = "Http Client") {

                    httpclient("https://api.coingecko.com/api/v3/simple/price?ids=binancecoin&vs_currencies=usd")
                }
                APiCallingButton(btnText = "Ok HttP") {
                    okhttp("https://api.coingecko.com/api/v3/simple/price?ids=binancecoin&vs_currencies=usd")
                }
                APiCallingButton(btnText = "Volley") {
                    volley("https://api.coingecko.com/api/v3/simple/price?ids=binancecoin&vs_currencies=usd")

                }
                APiCallingButton(btnText = "Retrofit") {
                    retrofit("https://api.coingecko.com/api/v3/simple/price?ids=binancecoin&vs_currencies=usd")

                }

            }


//            bottomBar()
        }
    }


    fun httpclient(url: String) {


        try {
            Thread(Runnable {

                //Step1
                val url_string = url

                val url = URL(url_string)
                //Step2
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

                //Now connect Step3
                connection.connect()

                val reader = BufferedReader(InputStreamReader(connection.inputStream))

                var line: String? = reader.readLine()
                var data = ""

                while (line != null) {
                    data += line
                    line = reader.readLine()
                }


                // Toast the data
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(applicationContext, "Http Client ->  $data", Toast.LENGTH_SHORT)
                        .show()
                }


                connection.disconnect()


            }).start()


        } catch (e: Exception) {

        }


    }
    class OkHttpRequestDemo(private val client: OkHttpClient) {

        fun GET(url: String, callback: Callback): Call {
            val request = Request.Builder()
                .url(url)
                .build()

            val call = client.newCall(request)
            call.enqueue(callback)
            return call
        }


    }

    fun okhttp(url: String) {
        val client = OkHttpClient()
        val request = OkHttpRequestDemo(client)

        request.GET(url, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure here
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                try {
                    val json = JSONObject(responseData)
                    Handler(Looper.getMainLooper()).post {

                        Toast.makeText(applicationContext, "okhttp -> ${json.toString()}", Toast.LENGTH_SHORT)
                            .show()

                    }
                } catch (e: JSONException) {
                    // Handle JSON parsing error here
                }
            }
        })
    }









    fun volley(url: String) {
        Toast.makeText(applicationContext, "Volley:$url", Toast.LENGTH_SHORT).show()
    }

    fun retrofit(url: String) {
        Toast.makeText(applicationContext, "Retrofit:$url", Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun APiCallingButton(btnText: String, clickAction: () -> Unit) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Surface(
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colors.primary,
                elevation = 80.dp
            ) {
                Button(onClick = { clickAction() }) {
                    Text(
                        text = btnText
                    )

                }
            }
        }
    }

}