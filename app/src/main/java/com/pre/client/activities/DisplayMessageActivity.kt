package com.pre.client.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.pre.client.R

import nics.crypto.proxy.afgh.AFGHProxyReEncryption

class DisplayMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_message)

        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val sk = AFGHProxyReEncryption.generateSecretKey(global)
        val pk = AFGHProxyReEncryption.generatePublicKey(sk, global)

        val m = AFGHProxyReEncryption.stringToElement(message, global!!.g2)
        val c = AFGHProxyReEncryption.firstLevelEncryption(m, pk, global)
        val mm = AFGHProxyReEncryption.firstLevelDecryption(c, sk, global)

        val textView = findViewById<TextView>(R.id.textView).apply {
            text = message
        }
        val encrypted = findViewById<TextView>(R.id.end).apply {
            text = String(mm.toBytes())
        }
    }
}
