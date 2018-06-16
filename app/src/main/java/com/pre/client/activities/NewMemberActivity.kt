package com.pre.client.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import com.pre.client.R
import com.pre.client.utils.get
import com.pre.client.utils.read
import com.pre.client.utils.send
import kotlinx.android.synthetic.main.activity_new_member.*
import nics.crypto.proxy.afgh.AFGHProxyReEncryption
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor
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

        get("http://$host:8080/public-key/$uphone") {
            val pk = Base64.decode(it, Base64.DEFAULT)
            val sk = getSecretKey()
            val rk = AFGHProxyReEncryption.generateReEncryptionKey(pk, sk, global)
            val rk64 = Base64.encodeToString(rk, Base64.DEFAULT)

            val charset = "UTF-8"
            val params = "phone=${URLEncoder.encode(uphone, charset)}&rk=${URLEncoder.encode(rk64, charset)}"
            send("POST", "/add-user/$idChat", params)
        }
        startActivity(intentFor<ConversationActivity>().clearTop())
    }

    fun getSecretKey(): ByteArray {
        val sk64 = read(this, getString(R.string.user_secret), "")
        return Base64.decode(sk64, Base64.DEFAULT)
    }
}
