package com.pre.client.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.pre.client.R
import com.pre.client.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import nics.crypto.proxy.afgh.AFGHGlobalParameters
import nics.crypto.proxy.afgh.AFGHProxyReEncryption
import org.jetbrains.anko.startActivity

const val host = "192.168.1.44"
var global: AFGHGlobalParameters? = null

/**
 * Main activity. In this screen the user
 * registers the first time they open the
 * app.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var globalString: String
    private var registered = false

    /**
     * If the user is already registered change to
     * the ChatsActivity. If not, get the global
     * parameters and allow the user to register.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registered = getPreferences(Context.MODE_PRIVATE)
                    .getBoolean(getString(R.string.user_registered), false)

        if (registered) {
            startActivity<ChatsActivity>()
        } else {
            getGlobal()
        }
    }

    /**
     * Generate public and secret keys and register
     * with a username and phone on the server. Then
     * change to the ChatsActivity.
     */
    fun register(view: View) {
        global   = AFGHGlobalParameters(globalString)
        val sk   = AFGHProxyReEncryption.generateSecretKey(global)
        val sk64 = encode(sk.toBytes())

        val pk   = AFGHProxyReEncryption.generatePublicKey(sk, global)
        val pk64 = encode(pk.toBytes())

        val uname   = encodeURL(name_input.text.toString())
        val phone   = encodeURL(telefono_input.text.toString())
        val pks     = encodeURL(pk64)

        save(uname, phone, sk64)
        send("PUT", "/new-user", "uname=$uname&phone=$phone&pk=$pks")

        val preferences = getPreferences(Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putBoolean(getString(R.string.user_registered), true)
            commit()
        }
        startActivity<ChatsActivity>()
    }

    /**
     * Get global parameters from the
     * server and store them.
     */
    private fun getGlobal() {
        get("http://$host:8080/") {
            globalString = it
            shareString(this@MainActivity, getString(R.string.global_string), it)
        }
    }

    /**
     * Save the phone, username and the secret key from the
     * user.
     */
    private fun save(user: String, phone: String, sks: String) {
        shareString(this, getString(R.string.user_phone), phone)
        shareString(this, getString(R.string.user_name), user)
        shareString(this, getString(R.string.user_secret), sks)
    }
}
