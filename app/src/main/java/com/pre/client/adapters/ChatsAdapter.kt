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

/**
 * Adapter for the view that presents user chats.
 */
class ChatsAdapter(private val chats: ArrayList<Chat>) : RecyclerView.Adapter<ChatsAdapter.TextHolder>() {

    /**
     * Inflate items and associate them to the holder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextHolder {
        val inflated = LayoutInflater.from(parent.context).inflate(R.layout.conversation_item, parent, false)
        return TextHolder(inflated)
    }

    /**
     * Set the number of items.
     */
    override fun getItemCount() = chats.size

    /**
     * Bind items to their views.
     */
    override fun onBindViewHolder(holder: TextHolder, position: Int) {
        holder.bindText(chats[position])
    }

    /**
     * Holder for a simple text view.
     */
    class TextHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private val view = v
        private lateinit var id: String

        /**
         * Initialize the holder.
         * Set up the click listener.
         */
        init {
            v.setOnClickListener(this)
        }

        /**
         * When clicking an item change to the
         * selected conversation.
         */
        override fun onClick(v: View?) {
            val context = itemView.context
            context.startActivity<ConversationActivity>(context.getString(R.string.selected_chat) to id)
        }

        /**
         * Bind a chat to its item.
         */
        fun bindText(chat: Chat) {
            id = chat.id
            view.title.text = chat.name
        }
    }
}
