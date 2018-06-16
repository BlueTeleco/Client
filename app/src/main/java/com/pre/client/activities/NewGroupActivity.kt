package com.pre.client.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pre.client.R
import com.pre.client.utils.read
import com.pre.client.utils.send
import kotlinx.android.synthetic.main.activity_new_group.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

class NewGroupActivity : AppCompatActivity() {

    private lateinit var admin_phone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)

        admin_phone = read(this, getString(R.string.user_phone), "error")
    }

    fun createGroup(view: View) {
        val gname = group_name.text
        send("PUT", "/new-group", "admin-ph=$admin_phone&gname=$gname")
        startActivity(intentFor<ChatsActivity>().clearTop())
    }
}
