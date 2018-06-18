package com.pre.client.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pre.client.R
import com.pre.client.utils.encodeURL
import com.pre.client.utils.read
import com.pre.client.utils.send
import kotlinx.android.synthetic.main.activity_new_group.*
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

/**
 * Activity to add chats. The user that creates the
 * chat will be the admin.
 */
class NewGroupActivity : AppCompatActivity() {

    /**
     * Create the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_group)
    }

    /**
     * Create a group in the server from the
     * group name and user phone.
     */
    fun createGroup(view: View) {
        val gname = encodeURL(group_name.text.toString())
        val phone = encodeURL(read(this, getString(R.string.user_phone), "error"))

        send("PUT", "/new-group", "admin-ph=$phone&gname=$gname")
        startActivity(intentFor<ChatsActivity>().clearTop())
    }
}
