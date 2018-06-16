package com.pre.client.utils

import com.pre.client.activities.host
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

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

fun get(url: String, f: (it: String) -> Unit) {
    async(UI) {
        val response = bg {
            URL(url).readText()
        }
        f(response.await())
    }
}