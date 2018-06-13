package com.pre.client.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pre.client.R
import kotlinx.android.synthetic.main.activity_new_member.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.intentFor
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class NewMemberActivity : AppCompatActivity() {

    private lateinit var idChat: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_member)

        idChat = intent.getStringExtra(getString(R.string.chat_id))
    }

    fun addMember(view: View) {
        async(UI) {
            val uphone = new_member.text
            bg {
                val params = "phone=$uphone"
                val url = URL("http://$host:8080/add-user/$idChat")
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "POST"
                    doOutput = true
                    OutputStreamWriter(outputStream).apply {
                        write(params)
                        close()
                    }
                    inputStream
                }
            }
        }
        startActivity(intentFor<ConversationActivity>().clearTop())
    }
}
