package com.pre.client.utils

import android.util.Base64
import java.net.URLEncoder

fun encode(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.DEFAULT)

fun decode(text: String): ByteArray = Base64.decode(text, Base64.DEFAULT)

fun encodeURL(param: String): String = URLEncoder.encode(param, "UTF-8")

