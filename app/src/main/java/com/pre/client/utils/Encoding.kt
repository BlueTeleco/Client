package com.pre.client.utils

import android.util.Base64
import java.net.URLEncoder

/**
 * Encode a ByteArray in Base64 encoding.
 */
fun encode(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.NO_WRAP)

/**
 * Decode a Base64 string to a ByteArray.
 */
fun decode(text: String): ByteArray = Base64.decode(text, Base64.NO_WRAP)

/**
 * Encode a string to send in a HTTP request.
 */
fun encodeURL(param: String): String = URLEncoder.encode(param, "UTF-8")

