package com.pre.client.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.pre.client.R
import com.pre.client.adapters.ConversationAdapter
import com.pre.client.model.Message
import com.pre.client.utils.*
import kotlinx.android.synthetic.main.activity_conversation.*
import nics.crypto.proxy.afgh.AFGHProxyReEncryption
import org.jetbrains.anko.startActivity

/**
 * Activity to show a conversation and send messages.
 */
class ConversationActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var id: String
    private var messages = arrayListOf<Message>()

    private lateinit var name: String
    private lateinit var phone: String

    /**
     * When creating the activity set up the activity,
     * create the reencryption key if necessary and
     * set up the list of messages.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        id = intent.getStringExtra(getString(R.string.selected_chat))
        name = read(this, getString(R.string.user_name), "")
        phone = read(this, getString(R.string.user_phone), "")
        sendReKey()

        layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ConversationAdapter(name, messages)

        getMessages()
    }

    /**
     * If there is not a reencryption key from the user to the
     * admin, create it and send it to the server.
     */
    private fun sendReKey() {
        get("http://$host:8080/rencryption-key/$id/$phone") {
            if (it == "") {
                get("http://$host:8080/pubkey-admin/$id") {
                    val pk = decode(it)
                    val sk = getSecretKey()
                    val rk = AFGHProxyReEncryption.generateReEncryptionKey(pk, sk, global)

                    val params = "phone=${encodeURL(phone)}&chat=$id&rk=${encodeURL(encode(rk))}"
                    send("PUT", "/new-rekey", params)
                }
            }
        }
    }

    /**
     * Get messages from the chat, decrypt them and add them to the
     * ListView.
     */
    private fun getMessages() {
        get("http://$host:8080/sincronize/$id/$phone?order=0") {
            val messagesStr = it.split("<--->")
            val admin = messagesStr[0]
            val rest = messagesStr.subList(1, messagesStr.lastIndex)

            rest.forEach {
                val (author, text) = it.split(":")

                val decrypted = if (name == admin) {
                    AFGHProxyReEncryption.secondLevelDecryption(decode(text), getSecretKey(), global)
                } else {
                    AFGHProxyReEncryption.firstLevelDecryption(decode(text), getSecretKey(), global)
                }

                val asElement = AFGHProxyReEncryption.bytesToElement(decrypted, global!!.g2)
                val asText = AFGHProxyReEncryption.elementToString(asElement)
                messages.add(Message(asText, author))
                recyclerView.adapter.notifyDataSetChanged()
            }
        }
    }

    /**
     * Get stored secret key as a ByteArray.
     */
    private fun getSecretKey(): ByteArray = decode(read(this, getString(R.string.user_secret), ""))

    /**
     * Add the message to be sent to the ListView and
     * send it to the server.
     */
    fun sendMessage(view: View) {
        val message = editText.text.toString()

        sendToServer(message)

        messages.add(Message(message, name))
        recyclerView.adapter.notifyDataSetChanged()
        editText.text.clear()
    }

    /**
     * Encrypt and send message to the server.
     */
    private fun sendToServer(message: String) {
        get("http://$host:8080/public-key/$phone") {
            val pk = decode(it)
            val encrypted = AFGHProxyReEncryption.secondLevelEncryption(message.toByteArray(), pk, global)
            val cryptoStr = encodeURL(encode(encrypted))
            send("POST", "/send/$id", "text=$cryptoStr&phone=$phone")
        }
    }

    /**
     * Inflate the options menu.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.conversation_options, menu)
        return true
    }

    /**
     * When selecting the appropiate item change to the NewMemberActivity
     * to add new members to the chat.
     */
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
