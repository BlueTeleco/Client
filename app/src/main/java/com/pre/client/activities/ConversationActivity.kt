package com.pre.client.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pre.client.R
import com.pre.client.adapters.ConversationAdapter
import com.pre.client.model.Message
import com.pre.client.utils.get
import com.pre.client.utils.read
import com.pre.client.utils.send
import kotlinx.android.synthetic.main.activity_conversation.*
import nics.crypto.proxy.afgh.AFGHProxyReEncryption
import org.jetbrains.anko.startActivity
import java.net.URLEncoder

class ConversationActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var id: String
    private var messages = arrayListOf<Message>(Message("hola", "Pedro"))

    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        name = read(this, getString(R.string.user_name), "")
        id = intent.getStringExtra(getString(R.string.selected_chat))
        sendReKey()

        layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ConversationAdapter(name, messages)

        // sincronize
    }

    fun sendReKey() {
        val phone = read(this, getString(R.string.user_phone), "")
        get("http://$host:8080/rencryption-key/$id/$phone") {
            if (it == "") {
                get("http://$host:8080/pubkey-admin/$id") {
                    val pk = Base64.decode(it, Base64.DEFAULT)
                    val sk = getSecretKey()
                    val rk = AFGHProxyReEncryption.generateReEncryptionKey(pk, sk, global)
                    val rk64 = Base64.encodeToString(rk, Base64.DEFAULT)

                    val charset = "UTF-8"
                    val params = "phone=${URLEncoder.encode(phone, charset)}&chat=$id&rk=${URLEncoder.encode(rk64, charset)}"
                    send("PUT", "/add-rekey", params)
                }
            }
        }
    }

    fun getSecretKey(): ByteArray {
        val sk64 = read(this, getString(R.string.user_secret), "")
        return Base64.decode(sk64, Base64.DEFAULT)
    }

    fun sendMessage(view: View) {
        val message = editText.text.toString()

        sendToServer(message)

        messages.add(Message(message, name))
        recyclerView.adapter.notifyDataSetChanged()
        editText.text.clear()
    }

    fun sendToServer(message: String) {
        val phone = read(this, getString(R.string.user_phone), "")

        get("http://$host:8080/public-key/$phone") {
            val pk = Base64.decode(it, Base64.DEFAULT)
            val encrypted = AFGHProxyReEncryption.secondLevelEncryption(message.toByteArray(), pk, global)
            val cryptoStr = Base64.encodeToString(encrypted, Base64.DEFAULT)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.conversation_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.new_member -> {
                startActivity<NewMemberActivity>(getString(R.string.chat_id) to id)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
