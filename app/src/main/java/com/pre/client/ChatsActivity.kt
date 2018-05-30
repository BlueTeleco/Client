package com.pre.client

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_chats.*
import org.jetbrains.anko.startActivity
import nics.crypto.proxy.afgh.AFGHGlobalParameters

class ChatsActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private val sentences = arrayListOf("Conversacion 1", "Conver 2", "Grupo 3", "hola", "que tal", "patata")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ConversationAdapter(sentences)

        global = global ?: globalParameters()

        // descargar los chats y asignar sus titulos a sentences
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
}
