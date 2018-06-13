package com.pre.client.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pre.client.R
import com.pre.client.model.Message
import kotlinx.android.synthetic.main.other_message.view.*
import kotlinx.android.synthetic.main.user_message.view.*

const val USER_MESSAGE = 0
const val OTHER_MESSAGE = 1

class ConversationAdapter(private val name: String, private val messages: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        val sender = messages[position].author
        return if (sender == name) {
            USER_MESSAGE
        } else {
            OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = when (viewType) {
            USER_MESSAGE -> UserHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_message, parent, false))
            else         -> OtherHolder(LayoutInflater.from(parent.context).inflate(R.layout.other_message, parent, false))
        }
        return holder as RecyclerView.ViewHolder
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            USER_MESSAGE -> {
                val userHolder = holder as UserHolder
                userHolder.bindUser(messages[position])
            }
            else -> {
                val otherHolder = holder as OtherHolder
                otherHolder.bindOther(messages[position])
            }
        }
    }

    inner class UserHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private val view = v

        override fun onClick(v: View?) {}

        fun bindUser(message: Message) {
            view.user_author.text  = message.author
            view.user_message.text = message.text
        }
    }

    inner class OtherHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private val view = v

        override fun onClick(v: View?) {}

        fun bindOther(message: Message) {
            view.other_author.text  = message.author
            view.other_message.text = message.text
        }
    }
}