package com.awelawi.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.LoginButton).setOnClickListener {
            val username = findViewById<EditText>(R.id.etUserName).text.toString()
            val password = findViewById<EditText>(R.id.etPassWord).text.toString()
            loginUser(username, password)
        }
    }
    private fun loginUser(username: String, password: String){
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                // Hooray!  The user is logged in.
                Log.i(TAG, "succesfully logged in user")
            } else {
                // Signup failed.  Look at the ParseException to see what happened.
                Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
            }})
        )

    }
    companion object{
        const val TAG = "LoginActivity"
    }
}