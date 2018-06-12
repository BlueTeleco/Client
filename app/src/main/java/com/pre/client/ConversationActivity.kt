package com.pre.client

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_conversation.*
import org.jetbrains.anko.startActivity

const val EXTRA_MESSAGE = "com.pre.client.MESSAGE"

class ConversationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        idView.text = intent.getStringExtra(SELECTED_CHAT)
    }

    fun sendMessage(view: View) {
        val editText = findViewById<EditText>(R.id.editText)
        var message = editText.text.toString()
        startActivity<DisplayMessageActivity>(EXTRA_MESSAGE to message)
    }
}
