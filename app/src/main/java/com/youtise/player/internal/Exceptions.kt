package com.youtise.player.internal

import java.io.IOException

class NoConnectivityException : IOException()
class ServerErrorException(message: String): IOException(message)
class ClientErrorException(message: String): IOException(message)