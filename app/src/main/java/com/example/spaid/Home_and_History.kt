package com.example.spaid

//imported modules
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.spaid.databinding.ActivityHomeAndHistoryBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

const val CHANNEL_ID = "channelId" //initializing chennel name
//declaring variables for firebase and etc...
class Home_and_History : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var buttonHome:Button
    private lateinit var buttonHistory:Button
    private lateinit var binding: ActivityHomeAndHistoryBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mediaPlayer: MediaPlayer

    //checking for build version
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAndHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth //initializing firebase authentication

        //declaring other variables
        val user = auth.currentUser
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("vibration status")
        val refCount = database.getReference("vibration count")

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        //binding the buttons
        buttonHome = binding.homeButton
        buttonHistory = binding.historyButton


        buttonHome.setOnClickListener {
            val intent = Intent(this@Home_and_History,Home::class.java)
            startActivity(intent)
        }


        buttonHistory.setOnClickListener {
            val intent = Intent(this@Home_and_History, History::class.java)
            startActivity(intent)
        }
        onBackPressedDispatcher.addCallback(this, callback) //on back press handling

        //checking if vibration status is 'On' and sending alert dialogbox if its on
        ref.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
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
        refCount.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Read the value of "alert_message" from the snapshot
                val vibrationCount = snapshot.getValue(Integer::class.java)
                if (vibrationCount != null) {
                    if (vibrationCount >= 2 ) {
                        showMedicalDialog()
                        createNotificationChannel()
                        var mediaPlayer = MediaPlayer.create(this@Home_and_History, R.raw.emsound)
                        mediaPlayer.start()
                        var builder = NotificationCompat.Builder(this@Home_and_History, CHANNEL_ID)
                        builder.setSmallIcon(R.drawable.o2)
                            .setContentTitle("Emergency")
                            .setContentText("Seek Medical Attention")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        with(NotificationManagerCompat.from(this@Home_and_History)){
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
                    //checking vibration count to be over or equal 2
                    if (vibrationCount >= 2 ) {
                        showMedicalDialog()
                        createNotificationChannel()
                        var mediaPlayer = MediaPlayer.create(this@Home_and_History, R.raw.emsound)
                        mediaPlayer.start()
                        var builder = NotificationCompat.Builder(this@Home_and_History, CHANNEL_ID)
                        builder.setSmallIcon(R.drawable.o2)
                            .setContentTitle("Emergency")
                            .setContentText("Seek Medical Attention")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        with(NotificationManagerCompat.from(this@Home_and_History)){
                            if (ActivityCompat.checkSelfPermission(
                                    applicationContext,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return
                            }
                            notify(1, builder.build())
                            mediaPlayer.stop()
                            mediaPlayer.release()
                        }

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Failed to read value.", error.toException())
            }
        })
        //database referencing
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val fingerText = snapshot.child("finger_detected").value.toString()
                val heartText = snapshot.child("heart_rate").value.toString()
                val oxygenText = snapshot.child("oxygen_level").value.toString()
                val stressText = snapshot.child("stress_level").value.toString()
                val musc = snapshot.child("Muscle_Status").value.toString()
                binding.heartval.text = "Heart Rate: $heartText bpm"
                binding.stress.text = "Stress level: $stressText"
                binding.oxygen.text = "Oxygen Level: $oxygenText"
                binding.musc.text = "Muscle Status: $musc"
                binding.fing.text = "Finger Detected:  $fingerText"
                }
            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "Failed to read value.", error.toException())
            }
        })
    }
    //function for exit dialog box
    private fun showExitDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logging out")
        builder.setMessage("Going back will log you out, are you sure you want to log out?")
        builder.setPositiveButton("Yes") { dialog, which ->
            startActivity(Intent(this@Home_and_History, MainActivity::class.java))
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            // Do nothing, stay in the current activity
        }
        builder.show()
    }
    //function for assist dialog box
    private fun showAssistDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Emergency")
        builder.setMessage("The Patience Needs Assistance")
        builder.setPositiveButton("Ok") { dialog, which ->
        }
        builder.show()
    }
    //function for emergency dialog box
    private fun showMedicalDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Emergency")
        builder.setMessage("Seek Medical Help")
        builder.setPositiveButton("Ok") { dialog, which ->
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
