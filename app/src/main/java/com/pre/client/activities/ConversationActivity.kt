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
import com.pre.client.utils.read
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import nics.crypto.proxy.afgh.AFGHProxyReEncryption
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.startActivity
import java.net.URL

class ConversationActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var id: String
    private var messages = arrayListOf<Message>(Message("hola", "Pedro"))

    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        name = read(this, getString(R.string.user_name), "")

        layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ConversationAdapter(name, messages)

        id = intent.getStringExtra(getString(R.string.selected_chat))
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

        async(UI) {
            val pubKeyStr = bg {
                URL("http://$host:8080/public-key?phone=$phone").readText()
            }
            val pk = Base64.decode(pubKeyStr.await(), Base64.DEFAULT)

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
