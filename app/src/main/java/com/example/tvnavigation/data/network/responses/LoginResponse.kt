package com.example.tvnavigation.data.network.responses

data class LoginResponse(
   val locationName: String,
   val message: String,
   val playerID: String,
   val token: String
)