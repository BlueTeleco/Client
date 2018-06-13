package com.pre.client.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.pre.client.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

import nics.crypto.proxy.afgh.AFGHGlobalParameters
import nics.crypto.proxy.afgh.AFGHProxyReEncryption
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.startActivity
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

const val host = "10.0.2.2"
var global: AFGHGlobalParameters? = null

class MainActivity : AppCompatActivity() {

    private lateinit var globalString: String
    private var registered = false

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

    fun register(view: View) {
        val preferences = getPreferences(Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putBoolean(getString(R.string.user_registered), true)
            commit()
        }

        global = AFGHGlobalParameters(globalString)
        async(UI) {
            val sk = AFGHProxyReEncryption.generateSecretKey(global)

            val uname = name_input.text
            val phone = telefono_input.text
            val pk = AFGHProxyReEncryption.generatePublicKey(sk, global)

            val pks = String(pk.toBytes())

            Log.e("pks ---> ", pks)
            Log.e("Converted ---> ", pks)

            save(uname.toString(), phone.toString())

            bg {
                val params = "uname=$uname&phone=$phone&pk=$pks"
                val url = URL("http://$host:8080/new-user")
                with(url.openConnection() as HttpURLConnection) {
                    requestMethod = "PUT"
                    doOutput = true
                    OutputStreamWriter(outputStream).apply {
                        write(params)
                        close()
                    }
                    inputStream
                }
            }
        }
        startActivity<ChatsActivity>()
    }

    fun getGlobal() {
        async(UI) {
            val globalStr = bg {
                URL("http://$host:8080/").readText()
            }
            globalString = globalStr.await()
            val preferences = getSharedPreferences(getString(R.string.parameters_file), Context.MODE_PRIVATE)
            with(preferences.edit()) {
                putString(getString(R.string.global_string), globalString)
                commit()
            }
        }
    }

    fun save(user: String, phone: String) {
        val preferences = getSharedPreferences(getString(R.string.parameters_file), Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putString(getString(R.string.user_phone), phone)
            putString(getString(R.string.user_name), user)
            commit()
        }
    }
}
