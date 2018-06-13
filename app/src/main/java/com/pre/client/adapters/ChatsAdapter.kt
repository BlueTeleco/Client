package com.pre.client.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pre.client.activities.ConversationActivity
import com.pre.client.R
import com.pre.client.model.Chat
import kotlinx.android.synthetic.main.conversation_item.view.title
import org.jetbrains.anko.startActivity

const val SELECTED_CHAT = "com.pre.client.ID"

class ChatsAdapter(private val chats: ArrayList<Chat>) : RecyclerView.Adapter<ChatsAdapter.TextHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextHolder {
        val inflated = LayoutInflater.from(parent.context).inflate(R.layout.conversation_item, parent, false)
        return TextHolder(inflated)
    }

    override fun getItemCount() = chats.size

    override fun onBindViewHolder(holder: TextHolder, position: Int) {
        holder.bindText(chats[position])
    }

    class TextHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private val view = v
        private lateinit var id: String

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = itemView.context
            context.startActivity<ConversationActivity>(SELECTED_CHAT to id)
        }

        fun bindText(chat: Chat) {
            id = chat.id
            view.title.text = chat.name
        }
    }
}
