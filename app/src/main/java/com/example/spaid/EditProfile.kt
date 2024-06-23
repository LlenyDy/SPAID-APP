package com.example.spaid

//imported modules
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.spaid.databinding.ActivityEditProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.provider.MediaStore
import android.util.Log
import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class EditProfile : AppCompatActivity() {
    //declaring variables
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setting up binding
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@EditProfile, Home::class.java))
            finish()
        }
        auth = Firebase.auth //initializing firebase authentication
        val user = auth.currentUser
        //checking if the user is logged in with authenticated and created account
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("users")
            databaseReference.child(user.uid).get().addOnSuccessListener {
                val name = it.child("name").value.toString()
                val sex = it.child("sex").value.toString()
                val address = it.child("address").value.toString()
                val numInfo = it.child("numInfo").value.toString()
                val contactPerson = it.child("contactPerson").value.toString()
                val contactPersonInfo = it.child("contactPersonInfo").value.toString()
                binding.editNameDisplay.setText(name)
                binding.editGenderDisplay.setText(sex)
                binding.editAddressDisplay.setText(address)
                binding.editPersonNumDisplay.setText(numInfo)
                binding.editContactPersonDisplay.setText(contactPerson)
                binding.editContactPersonNumInfoDisplay.setText(contactPersonInfo)
            }
        }

        binding.saveButton.setOnClickListener {
            val editedName = binding.editNameDisplay.text.toString()
            val editedGender = binding.editGenderDisplay.text.toString()
            val editedAddress = binding.editAddressDisplay.text.toString()
            val editedNumber = binding.editPersonNumDisplay.text.toString()
            val editedContactPerson = binding.editContactPersonDisplay.text.toString()
            val editedContactPersonNumber = binding.editContactPersonNumInfoDisplay.text.toString()
            if (user != null) {
                //updating values of data from user
                val userData = UserData(
                    name = editedName,
                    sex = editedGender,
                    address = editedAddress,
                    numInfo = editedNumber,
                    contactPerson = editedContactPerson,
                    contactPersonInfo = editedContactPersonNumber
                )
                databaseReference.child(user.uid).setValue(userData)
                startActivity(Intent(this@EditProfile, Home::class.java))
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