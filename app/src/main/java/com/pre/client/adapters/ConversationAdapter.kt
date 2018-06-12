package com.pre.client.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pre.client.R
import kotlinx.android.synthetic.main.conversation_item.view.title

class ConversationAdapter(private val messages: ArrayList<String>) : RecyclerView.Adapter<ConversationAdapter.MessageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val inflated = LayoutInflater.from(parent.context).inflate(R.layout.conversation_item, parent, false)
        return MessageHolder(inflated)
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bindText(messages[position])
    }

    inner class MessageHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private val view = v

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {}

        fun bindText(text: String) {
            view.title.text = text
        }
    }
}