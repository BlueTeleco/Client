package com.pre.client

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_chats.*
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.startActivity
import nics.crypto.proxy.afgh.AFGHGlobalParameters
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.coroutines.experimental.bg
import java.net.URL

class ChatsActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private var chats = arrayListOf<String>()
    private var chatIds = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ConversationAdapter(chats, chatIds)

        getChats()

        global = global ?: globalParameters()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chats_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.new_group -> {
                startActivity<NewGroupActivity>()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun globalParameters(): AFGHGlobalParameters {
        val globalStr = getSharedPreferences(getString(R.string.parameters_file), Context.MODE_PRIVATE)
                        .getString(getString(R.string.global_string), "")

        return AFGHGlobalParameters(globalStr)
    }

    fun getChats() {
        val phone = getSharedPreferences(getString(R.string.parameters_file), Context.MODE_PRIVATE)
                .getString(getString(R.string.user_phone), "")

        async(UI) {
            val chatsDef = bg {
                URL("http://$host:8080/chats?phone=$phone").readText()
            }

            val chatsStr = chatsDef.await().split("-")
            chats.clear()
            chatIds.clear()
            chatsStr.forEach { chat ->
                val elements = chat.split(":")
                chatIds.add(elements[0])
                chats.add(elements[1])
                recyclerView.adapter.notifyDataSetChanged()
            }
        }
    }
}
