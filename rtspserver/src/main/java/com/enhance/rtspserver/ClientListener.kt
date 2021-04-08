package com.enhance.rtspserver

interface ClientListener {
  fun onDisconnected(client: ServerClient)
}