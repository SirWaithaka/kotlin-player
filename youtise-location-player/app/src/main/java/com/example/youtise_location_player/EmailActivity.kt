package com.example.youtise_location_player

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView


class EmailActivity : Activity() {

    lateinit var emailMessages: TextView
    lateinit var email: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        init()
    }

    private fun init() {
        email = findViewById(R.id.email)
        emailMessages = findViewById(R.id.emailMessages)
        email.on(EditorInfo.IME_ACTION_DONE, { })

    }

    fun EditText.on(actionId: Int, func: () -> Unit) {
        setOnEditorActionListener { _, receivedActionId, _ ->

            if (actionId == receivedActionId) {
                emailMessages.text=email.text
                emailMessages.setTextColor(Color.RED)
                true
            }
            false
        }
    }

}