package com.pre.client.utils

import com.pre.client.activities.host
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * Send a PUT or POST request to a specified resource with
 * the specified params.
 */
fun send(method: String, resource: String, params: String) {
    async(UI) {
        bg {
            val url = URL("http://$host:8080$resource")
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = method
                doOutput = true
                OutputStreamWriter(outputStream).apply {
                    write(params)
                    close()
                }
                inputStream
            }
        }
    }
}

/**
 * Send a GET request and execute the specified
 * function with the response.
 */
fun get(url: String, f: (it: String) -> Unit) {
    async(UI) {
        val response = bg {
            URL(url).readText()
        }
        f(response.await())
    }
}