package com.pre.client.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.pre.client.R
import com.pre.client.adapters.SELECTED_CHAT
import org.jetbrains.anko.startActivity

const val EXTRA_MESSAGE = "com.pre.client.MESSAGE"

class ConversationActivity : AppCompatActivity() {

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        id = intent.getStringExtra(SELECTED_CHAT)
    }

    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        var message = editText.text.toString()
        startActivity<DisplayMessageActivity>(EXTRA_MESSAGE to message)
    }
}
