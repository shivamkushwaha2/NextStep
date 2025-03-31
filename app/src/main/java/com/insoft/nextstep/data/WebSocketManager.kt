package com.insoft.nextstep.data

import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object WebSocketManager {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .build()

    private var listener: WebSocketListener? = null

    fun connectWebSocket(url: String, webSocketListener: WebSocketListener) {
        listener = webSocketListener
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, webSocketListener)
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    fun closeWebSocket() {
        webSocket?.close(1000, "User closed connection")
        webSocket = null
    }
}
