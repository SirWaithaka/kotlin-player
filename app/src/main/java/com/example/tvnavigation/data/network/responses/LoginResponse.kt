package com.example.tvnavigation.data.network.responses

data class LoginResponse(
   val locationName: String,
   val message: String,
   val playerId: String,
   val token: String
)