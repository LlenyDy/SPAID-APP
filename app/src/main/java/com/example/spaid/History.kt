package com.example.spaid

//imported modules
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager

import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.spaid.databinding.ActivityHistoryBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class History : AppCompatActivity() {

    //declaring variables for firebase and etc..
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setting up binding
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.april.setOnClickListener {
            startActivity(Intent(this@History, April::class.java))
        }
        binding.may.setOnClickListener {
            startActivity(Intent(this@History, May::class.java))
        }
        auth = Firebase.auth //initializing firebase authentication
        val user = auth.currentUser

        //referencing in database
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("vibration status")
        val refCount = database.getReference("vibration count")

        refCount.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Read the value of "alert_message" from the snapshot
                val vibrationCount = snapshot.getValue(Integer::class.java)
                if (vibrationCount != null) {
                    //checking if vibration count exceeds 2 or reach 2
                    if (vibrationCount >= 2 ) {
                        showMedicalDialog()
                        var builder = NotificationCompat.Builder(this@History, CHANNEL_ID)
                        builder.setSmallIcon(R.drawable.o2)
                            .setContentTitle("Emergency")
                            .setContentText("Seek Medical Attention")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        with(NotificationManagerCompat.from(this@History)){
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

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Read the value of "alert_message" from the snapshot
                val vibration = snapshot.getValue(String::class.java)
                //checking if vibration motor is on
                if (vibration == "On") {
                    showAssistDialog()
                    Log.d("vibrate", "Yes Off Sya len: $vibration")
                    val currentDate = Calendar.getInstance().time
                    val dateFormat = SimpleDateFormat("MMM, dd, yyyy", Locale.US)
                    val formattedDate = dateFormat.format(currentDate)
                    val calendar = Calendar.getInstance()
                    val timeFormat = SimpleDateFormat("h:mm a", Locale.US)
                    val formattedTime = timeFormat.format(calendar.time)

                    val checkMonthFormat = SimpleDateFormat("MMM", Locale.US)
                    val formattedCheckMonthFormat = checkMonthFormat.format(currentDate)

                    val month = formattedCheckMonthFormat.toString()
                    /*
                    * this part of the code is where the readings from sensors when sleep paralysis
                    * indicator triggers will be recorded in the designated month when it triggers
                    * as well as its specific date and time
                    *
                    * note that other months are to be updated since there is no way to test since
                    * we cannot skip or its not appropriate to skip the month just to test if the
                    * code works
                    */
                    if (month == "Jan"){
                        Log.d("datenilen", "jan ni len")
                    }
                    if (month == "Feb"){
                        Log.d("datenilen", "Feb ni len")
                    }
                    if (month == "Mar"){
                        Log.d("datenilen", "Mar ni len")
                    }

                    //making history recording for month of april
                    if (month == "Apr"){
                        databaseReference = FirebaseDatabase.getInstance().reference
                        databaseReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val alertText = snapshot.child("alert_message").value.toString()
                                val fingerText = snapshot.child("finger_detected").value.toString()
                                val heartText = snapshot.child("heart_rate").value.toString()
                                val oxygenText = snapshot.child("oxygen_level").value.toString()
                                val stressText = snapshot.child("stress_level").value.toString()
                                val vibrateText = snapshot.child("vibration count").value.toString()
//                                val aprDatabaseReference = FirebaseDatabase.getInstance().reference.child("Apr")
//                                val x = aprDatabaseReference.getChildrenCount()
                                if (user != null) {
                                    val vitalsData = VitalsData(
                                        time = formattedTime,
                                        alert = alertText,
                                        finger = fingerText,
                                        heart = heartText,
                                        oxygen = oxygenText,
                                        stress = stressText,
                                        vibrate = vibrateText,
                                        date = formattedDate
                                    )
                                    databaseReference.child("data").child(user.uid).child(month).child(formattedTime).setValue(vitalsData)
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("TAG", "Failed to read value.", error.toException())
                            }
                        })
                    }
                    //making history recording for month of may
                    if (month == "May"){
                        databaseReference = FirebaseDatabase.getInstance().reference
                        databaseReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val alertText = snapshot.child("alert_message").value.toString()
                                val fingerText = snapshot.child("finger_detected").value.toString()
                                val heartText = snapshot.child("heart_rate").value.toString()
                                val oxygenText = snapshot.child("oxygen_level").value.toString()
                                val stressText = snapshot.child("stress_level").value.toString()
                                val vibrateText = snapshot.child("vibration count").value.toString()
//                                val aprDatabaseReference = FirebaseDatabase.getInstance().reference.child("Apr")
//                                val x = aprDatabaseReference.getChildrenCount()
                                if (user != null) {
                                    val vitalsData = VitalsData(
                                        time = formattedTime,
                                        alert = alertText,
                                        finger = fingerText,
                                        heart = heartText,
                                        oxygen = oxygenText,
                                        stress = stressText,
                                        vibrate = vibrateText,
                                        date = formattedDate
                                    )
                                    databaseReference.child("data").child(user.uid).child(month).child(formattedTime).setValue(vitalsData)
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("TAG", "Failed to read value.", error.toException())
                            }
                        })
                    }
                    if (month == "Jun"){
                        Log.d("datenilen", "Jun ni len")
                    }
                    if (month == "Jul"){
                        Log.d("datenilen", "Jul ni len")
                    }
                    if (month == "Aug"){
                        Log.d("datenilen", "Aug ni len")
                    }
                    if (month == "Sep"){
                        Log.d("datenilen", "Sep ni len")
                    }
                    if (month == "Oct"){
                        Log.d("datenilen", "Oct ni len")
                    }
                    if (month == "Nov"){
                        Log.d("datenilen", "Nov ni len")
                    }
                    if (month == "Dec"){
                        Log.d("datenilen", "Dec ni len")
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle errors
                Log.e("TAG", "Failed to read value.", error.toException())
            }
        })
    }
    //function for assist dialog box
    private fun showAssistDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Emergency")
        builder.setMessage("The Patience Needs Assistance")
        builder.setPositiveButton("Treated") { dialog, which ->
        }
        builder.show()
    }
    //function for emergency dialog box
    private fun showMedicalDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Emergency")
        builder.setMessage("Seek Medical Help")
        builder.setPositiveButton("Treated") { dialog, which ->
        }
        builder.show()
    }
}