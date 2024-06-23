package com.example.spaid

//imported modules
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.spaid.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    //declaring variables for firebase, etc...
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setting up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth //initializing firebase authentication

        //setting back button pressed function
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        //referencing in database
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")


        binding.loginButton.setOnClickListener{
            val email = binding.emailSignin.text.toString()
            val password = binding.Textpassword.text.toString()

            //checking if email and password views are not empty
            if (email.isNotEmpty() && password.isNotEmpty()){
                loginUser(email, password)
            } else {
                Toast.makeText(this@MainActivity, "username or password missing", Toast.LENGTH_SHORT).show()
            }
        }
        binding.signupButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignUp::class.java))
            finish()
        }
    }

    //declaring loginuser function that logs user if username and password exist and correct in the database
    private fun loginUser(username: String, password: String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(
                        this@MainActivity,
                        "Login Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@MainActivity, Home_and_History::class.java))
                    if (user != null) {
                        val userEmail = user.email
                        if (userEmail != null) {
                            databaseReference.orderByChild("email").equalTo(userEmail)
                                .addListenerForSingleValueEvent(
                                    object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            for (dataSnapshot in snapshot.children) {
                                                val userData =
                                                    dataSnapshot.getValue(UserData::class.java)
                                                if (userData != null) {
                                                    // Access the user's information here
                                                    val userName = userData.name
                                                    val userSex = userData.sex
                                                    val userAddress = userData.address
                                                    val userNumber = userData.numInfo
                                                    val userContactPerson = userData.contactPerson
                                                    val userContactPersonInfo =
                                                        userData.contactPersonInfo
                                                    finish()
                                                    // Use the user's information as needed
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Database query canceled: ${error.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                        }
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Email or Password Incorrect.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    // function of exitdialog box(upon pressing back button)
    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit App")
        builder.setMessage("Are you sure you want to exit?")
        builder.setPositiveButton("Yes") { dialog, which ->
            finishAffinity()
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            // Do nothing, stay in the current activity
        }
        builder.show()
    }
}
