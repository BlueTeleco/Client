package com.pre.client.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.pre.client.R
import com.pre.client.utils.*
import kotlinx.android.synthetic.main.activity_new_member.*
import nics.crypto.proxy.afgh.AFGHProxyReEncryption
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

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
            val pk = decode(it)
            val sk = getSecretKey()
            val rk = AFGHProxyReEncryption.generateReEncryptionKey(pk, sk, global)

            val params = "phone=${encodeURL(uphone)}&rk=${encodeURL(encode(rk))}"
            send("POST", "/add-user/$idChat", params)
        }
        startActivity(intentFor<ConversationActivity>().clearTop())
    }

    private fun getSecretKey(): ByteArray = decode(read(this, getString(R.string.user_secret), ""))
}
