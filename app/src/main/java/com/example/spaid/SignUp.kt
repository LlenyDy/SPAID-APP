package com.example.spaid

// imported modules
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.spaid.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    //declaring variables for firebase and etc...
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setting up binding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@SignUp, MainActivity::class.java))
            }
        }

        //handling back pressed button
        onBackPressedDispatcher.addCallback(this, callback)

        auth = Firebase.auth // initializing firebase authentication

        //referencing from firebase database (realtie)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        //setting up more variables (mostly connected in the front end)
        binding.createAccount.setOnClickListener {
            val emailAccount = binding.emailAccount.text.toString()
            val emailPassword = binding.emailPassword.text.toString()

            val nameText = binding.Name.text.toString()
            val sexText = binding.Sex.text.toString()
            val addressText = binding.Address.text.toString()
            val numberText = binding.numberInfo.text.toString()
            val contactpersonText = binding.contactPerson.text.toString()
            val contactpersonInfoText = binding.contactPersonInfo.text.toString()

            //checkign if emailaccoung and emailpassword views are not empty
            if (emailAccount.isNotEmpty() && emailPassword.isNotEmpty()){
                auth.createUserWithEmailAndPassword(emailAccount, emailPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            Toast.makeText(this@SignUp, "SignUp Successful", Toast.LENGTH_SHORT).show()
                            if(user != null) {
                                val userData = UserData(
                                    name = nameText,
                                    sex = sexText,
                                    address = addressText,
                                    numInfo = numberText,
                                    contactPerson = contactpersonText,
                                    contactPersonInfo = contactpersonInfoText
                                )
                                databaseReference.child(user.uid).setValue(userData)
                                startActivity(Intent(this@SignUp, Home_and_History::class.java))
                                Log.i("id", "inside: $userData")
                            }
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this@SignUp, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //declaring function that signs up new user and saves new data to the database
    private fun signUpUser(username: String, password: String) {
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this@SignUp, "SignUp Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}