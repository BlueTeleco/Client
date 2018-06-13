package com.pre.client.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.pre.client.R
import com.pre.client.adapters.ConversationAdapter
import com.pre.client.adapters.SELECTED_CHAT
import com.pre.client.model.Message
import kotlinx.android.synthetic.main.activity_conversation.*
import org.jetbrains.anko.startActivity

const val EXTRA_MESSAGE = "com.pre.client.MESSAGE"

class ConversationActivity : AppCompatActivity() {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var id: String
    private var messages = arrayListOf<Message>(Message("hola", "Pedro"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        val name = getSharedPreferences(getString(R.string.parameters_file), Context.MODE_PRIVATE)
                    .getString(getString(R.string.user_name), "")

        messages.add(Message("Hi", name))
        messages.add(Message("Hi", "Paco"))
        messages.add(Message("Lol", name))
        messages.add(Message("Lel", "Paco"))
        messages.add(Message("Lal", name))
        messages.add(Message("Lul", "Paco"))

        layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = ConversationAdapter(name, messages)

        id = intent.getStringExtra(SELECTED_CHAT)
    }

    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        var message = editText.text.toString()
        startActivity<DisplayMessageActivity>(EXTRA_MESSAGE to message)
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
