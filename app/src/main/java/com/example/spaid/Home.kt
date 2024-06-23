package com.example.spaid

//imported modules
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.spaid.databinding.ActivityHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

//declaring variables for firebase and etc..
class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("vibration status")
    val refCount = database.getReference("vibration count")

    // setting up binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@Home, Home_and_History::class.java))
            }
        }
        onBackPressedDispatcher.addCallback(this, callback) //handling on back pressed

        auth = Firebase.auth
        // checking if vibration status is on
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Read the value of "alert_message" from the snapshot
                val vibration = snapshot.getValue(String::class.java)
                if (vibration == "On") {
                    showAssistDialog()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Failed to read value.", error.toException())
            }
        })
        //checking if vibration count over or equal 2
        refCount.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Read the value of "alert_message" from the snapshot
                val vibrationCount = snapshot.getValue(Integer::class.java)
                if (vibrationCount != null) {
                    if (vibrationCount >= 2 ) {
                        showMedicalDialog()
                        var builder = NotificationCompat.Builder(this@Home, CHANNEL_ID)
                        builder.setSmallIcon(R.drawable.o2)
                            .setContentTitle("Emergency")
                            .setContentText("Seek Medical Attention")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        with(NotificationManagerCompat.from(this@Home)){
                            if (ActivityCompat.checkSelfPermission(
                                    applicationContext,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return
                            }
                            notify(1, builder.build())
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Failed to read value.", error.toException())
            }
        })
        binding.EditHome.setOnClickListener {
            startActivity(Intent(this@Home, EditProfile::class.java))
        }
        val user = auth.currentUser // initializing firebase authentication

        //checking if current user is in the users database reference
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("users")
            databaseReference.child(user.uid).get().addOnSuccessListener {
                if (it.exists()){
                    val name = it.child("name").value
                    val sex = it.child("sex").value
                    val address = it.child("address").value
                    val numInfo = it.child("numInfo").value
                    val contactPerson = it.child("contactPerson").value
                    val contactPersonInfo = it.child("contactPersonInfo").value
                    binding.nameDisplay.text = name.toString()
                    binding.genderDisplay.text = sex.toString()
                    binding.addressDisplay.text = address.toString()
                    binding.personNumDisplay.text = numInfo.toString()
                    binding.contactPersonDisplay.text = contactPerson.toString()
                    binding.contactPersonNumInfoDisplay.text = contactPersonInfo.toString()
                }else{
                    Toast.makeText(this, "notfound", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //function for assist dialog
    private fun showAssistDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Emergency")
        builder.setMessage("The Patience Needs Assistance")
        builder.setPositiveButton("Treated") { dialog, which ->
        }
        builder.show()
    }
    //function for emergency dialog
    private fun showMedicalDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Emergency")
        builder.setMessage("Seek Medical Help")
        builder.setPositiveButton("Treated") { dialog, which ->
        }
        builder.show()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
//        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.0){
        val channel = NotificationChannel(CHANNEL_ID, "First Channel",
            NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "TRY LANG"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }
}