package com.pre.client

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.conversation_item.view.title
import org.jetbrains.anko.startActivity

class ConversationAdapter(private val sentences: ArrayList<String>) : RecyclerView.Adapter<ConversationAdapter.TextHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationAdapter.TextHolder {
        val inflated = LayoutInflater.from(parent.context).inflate(R.layout.conversation_item, parent, false)
        return TextHolder(inflated)
    }

    override fun getItemCount() = sentences.size

    override fun onBindViewHolder(holder: ConversationAdapter.TextHolder, position: Int) {
        holder.bindText(sentences[position])
    }

    class TextHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private val view = v
        private var text = "Este texto"

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = itemView.context
            context.startActivity<ConversationActivity>()
        }

        fun bindText(text: String) {
            this.text = text
            view.title.text = text
        }
    }
}