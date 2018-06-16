package com.pre.client.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.pre.client.R
import com.pre.client.adapters.ChatsAdapter
import com.pre.client.model.Chat
import com.pre.client.utils.get
import com.pre.client.utils.read
import kotlinx.android.synthetic.main.activity_chats.*
import org.jetbrains.anko.startActivity
import nics.crypto.proxy.afgh.AFGHGlobalParameters

class ChatsActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private var chats = arrayListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ChatsAdapter(chats)

        getChats()
        global = global ?: globalParameters()
    }

    fun globalParameters(): AFGHGlobalParameters {
        val globalStr = read(this, getString(R.string.global_string), "")
        return AFGHGlobalParameters(globalStr)
    }

    fun getChats() {
        val phone = read(this, getString(R.string.user_phone), "")

        get("http://$host:8080/chats/$phone") {
            val chatsStr = it.split("\n")

            chatsStr.forEach {
                val (id, name) = it.split(":")
                chats.add(Chat(id, name))
                recyclerView.adapter.notifyDataSetChanged()
            }

        }
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
}
