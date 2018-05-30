package com.pre.client

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_new_group.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.intentFor
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class NewGroupActivity : AppCompatActivity() {

    private var admin_phone = "333666999"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)

        // get user from database
    }

    fun createGroup(view: View) {
        async(UI) {
            val gname = group_name.text
            bg {
                val params = "admin-ph=$admin_phone&gname=$gname"
                val url = URL("http://$host:8080/new-group")
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "PUT"
                    doOutput = true
                    OutputStreamWriter(outputStream).apply {
                        write(params)
                        close()
                    }
                    inputStream
                }
            }
        }
        startActivity(intentFor<ChatsActivity>().clearTop())
    }
}
