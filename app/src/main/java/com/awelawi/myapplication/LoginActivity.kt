package com.awelawi.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Check if user is logged in
        //If true, take them to MainActivity
        val currentUser = ParseUser.getCurrentUser()
        ParseUser.logOut()
        if(ParseUser.getCurrentUser() != null){
            goToMainActivity()
        }
        findViewById<Button>(R.id.LoginButton).setOnClickListener {
            val username = findViewById<EditText>(R.id.etUserName).text.toString()
            val password = findViewById<EditText>(R.id.etPassWord).text.toString()
            loginUser(username, password)
        }
        findViewById<Button>(R.id.signUp).setOnClickListener {
            val username = findViewById<EditText>(R.id.etUserName).text.toString()
            val password = findViewById<EditText>(R.id.etPassWord).text.toString()
            signUpUser(username, password)
        }
    }
    private fun signUpUser(username: String, password: String){
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // Hooray! Let them use the app now.
                //Navigate the user to main activity
                Toast.makeText(this, "successfu lly login the user", Toast.LENGTH_SHORT).show()
                goToMainActivity()

                //Make a toast to show the user signup successfully
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                //Show a toast to the user to tell them failed sign up
                    e.printStackTrace()
            }
        }
    }
    private fun loginUser(username: String, password: String){
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                // Hooray!  The user is logged in.
                Log.i(TAG, "Succesfully logged user")
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
        finish()

    }
    companion object{
        const val TAG = "LoginActivity"
    }
}