package com.example.youtise_location_player

import android.os.Bundle
import android.app.Activity
import android.graphics.Color
import android.view.KeyEvent
import android.widget.EditText

import kotlinx.android.synthetic.main.activity_password.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView


class Password : Activity() {
    lateinit var passwordMessages: TextView
    lateinit var inputPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        init()
    }

    private fun init() {
        inputPassword = findViewById(R.id.enterPassword)
        passwordMessages = findViewById(R.id.passwordMessages)
        inputPassword.on(EditorInfo.IME_ACTION_DONE, { })

    }

    fun EditText.on(actionId: Int, func: () -> Unit) {
        setOnEditorActionListener { _, receivedActionId, _ ->

            if (actionId == receivedActionId) {
                passwordMessages.text=inputPassword.text
                passwordMessages.setTextColor(Color.RED)
                   true
            }
             false
        }
    }

}
