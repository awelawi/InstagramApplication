package com.awelawi.myapplication

import android.content.Intent
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
                Toast.makeText(this, "succesfully logged in user", Toast.LENGTH_SHORT).show()
                goToMainActivity()
            } else {
                // Signup failed.  Look at the ParseException to see what happened.
                    e.printStackTrace()
                    Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
            }})
        )

    }

    private fun goToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }
    companion object{
        const val TAG = "LoginActivity"
    }
}