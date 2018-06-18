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

/**
 * Adapter for the view that presents a conversations messages.
 */
class ConversationAdapter(private val name: String, private val messages: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Depending on the sender of the message change
     * the item type.
     */
    override fun getItemViewType(position: Int): Int {
        val sender = messages[position].author
        return if (sender == name) {
            USER_MESSAGE
        } else {
            OTHER_MESSAGE
        }
    }

    /**
     * Inflate the items depending on its type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = when (viewType) {
            USER_MESSAGE -> UserHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_message, parent, false))
            else         -> OtherHolder(LayoutInflater.from(parent.context).inflate(R.layout.other_message, parent, false))
        }
        return holder as RecyclerView.ViewHolder
    }

    /**
     * Set the item count.
     */
    override fun getItemCount() = messages.size

    /**
     * Bind each message to a different holder depending
     * on its type.
     */
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

    /**
     * Holder for the users messages.
     */
    class UserHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private val view = v

        /**
         * Do nothing when clicking an item.
         */
        override fun onClick(v: View?) {}

        /**
         * Bind the message to its view.
         */
        fun bindUser(message: Message) {
            view.user_author.text  = message.author
            view.user_message.text = message.text
        }
    }

    /**
     * Holder for other users messages.
     */
    class OtherHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private val view = v

        /**
         * Do nothing when clicking an item.
         */
        override fun onClick(v: View?) {}

        /**
         * Bind the message to its view.
         */
        fun bindOther(message: Message) {
            view.other_author.text  = message.author
            view.other_message.text = message.text
        }
    }
}