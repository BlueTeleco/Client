package com.pre.client.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import com.pre.client.R
import kotlinx.android.synthetic.main.activity_new_member.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import nics.crypto.proxy.afgh.AFGHProxyReEncryption
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.intentFor
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class NewMemberActivity : AppCompatActivity() {

    private lateinit var idChat: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_member)

        idChat = intent.getStringExtra(getString(R.string.chat_id))
    }

    fun addMember(view: View) {
        val uphone = new_member.text.toString()
        async(UI) {
            val pubKeyStr = bg {
                URL("http://$host:8080/public-key?phone=$uphone").readText()
            }
            val pk = Base64.decode(pubKeyStr.await(), Base64.DEFAULT)
            val sk = getSecretKey()
            val rk = AFGHProxyReEncryption.generateReEncryptionKey(pk, sk, global)
            val rk64 = Base64.encodeToString(rk, Base64.DEFAULT)

            bg {
                val charset = "UTF-8"
                val params = "phone=${URLEncoder.encode(uphone, charset)}&rk=${URLEncoder.encode(rk64, charset)}"
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


    fun getSecretKey(): ByteArray {
        val sk64 = getSharedPreferences(getString(R.string.parameters_file), Context.MODE_PRIVATE)
                 .getString(getString(R.string.user_secret), "")
        return Base64.decode(sk64, Base64.DEFAULT)
    }
}
