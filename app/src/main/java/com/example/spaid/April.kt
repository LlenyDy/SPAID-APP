package com.example.spaid

//imported modules
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import com.example.spaid.databinding.ActivityAprilBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.log

class April : AppCompatActivity() {
    //declaring variables
    private lateinit var binding: ActivityAprilBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setting up binding
        binding = ActivityAprilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageLayout = binding.imagelayout
        val linearLayout = binding.april

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("vibration status")
        val refCount = database.getReference("vibration count")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Read the value of "alert_message" from the snapshot
                val vibration = snapshot.getValue(String::class.java)
                //checking if vibration motor is on
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
                    //checking if vibration count reach or exceeds 2
                    if (vibrationCount >= 2 ) {
                        showMedicalDialog()
                        var builder = NotificationCompat.Builder(this@April , CHANNEL_ID)
                        builder.setSmallIcon(R.drawable.o2)
                            .setContentTitle("Emergency")
                            .setContentText("Seek Medical Attention")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                        with(NotificationManagerCompat.from(this@April)){
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

        auth = Firebase.auth // initializing firebase authentication
        val user = auth.currentUser
        val calendarApril = Calendar.getInstance()
        val timeFormatApril = SimpleDateFormat("h:mm a", Locale.US)
        val formattedTimeApril = timeFormatApril.format(calendarApril.time)
        databaseReference = FirebaseDatabase.getInstance().getReference("data")

        //checking if user is logged in as authenticated account and created account
        if (user != null) {
            databaseReference = databaseReference.child(user.uid).child("Apr")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        val heartValue = childSnapshot.child("heart").value
                        val oxygenValue = childSnapshot.child("oxygen").value
                        val stressValue = childSnapshot.child("stress").value
                        val dateValue = childSnapshot.child("date").value
                        val timeValue = childSnapshot.key

                        val dateView = TextView(this@April)
                        val timeView = TextView(this@April)
                        val stressView = TextView(this@April)
                        val heartView = TextView(this@April)
                        val oxygenView = TextView(this@April)
                        val oxygenImageView = ImageView(this@April)
                        val bpmImageView = ImageView(this@April)
                        val dpSize = resources.getDimensionPixelSize(R.dimen.image_size_dp)

                        dateView.text = "Date: $dateValue"
                        stressView.text = "Stress Level: $stressValue"
                        heartView.text = "BPM: $heartValue"
                        oxygenView.text = "Oxygen Level: $oxygenValue"
                        timeView.text = "TIME:: $timeValue"
                        bpmImageView.setImageResource(R.drawable.aw)
                        oxygenImageView.setImageResource(R.drawable.water)

                        /*
                        * at this part of the code is where we record and write the vitals each time
                        * the trigger for sleep paralysis turns on to the database to be then displayed
                        * in the page
                        */
                        val heartparams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        heartparams.setMargins(0, 0, 600,50)

                        val stressparams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                        )
                        stressparams.setMargins(0, 0, 400,150)

                        val oxygenparams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                        )
                        oxygenparams.setMargins(0, 0, 500,50)

                        val tiparams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        tiparams.setMargins(0, 0, 0,0)
                        val dateparams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        dateparams.setMargins(0, 0, 0,0)

                        val imgBPparam = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        imgBPparam.setMargins(0, 90, 0,10)
                        val imgOxparam = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        imgOxparam.setMargins(0, 0, 0,250)



                        dateView.gravity = Gravity.CENTER
                        stressView.gravity = Gravity.CENTER
                        timeView.gravity = Gravity.CENTER
                        heartView.gravity = Gravity.CENTER
                        oxygenView.gravity = Gravity.CENTER

                        dateView.layoutParams = dateparams
                        timeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50.toFloat())

                        timeView.layoutParams = tiparams
                        timeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50.toFloat())

                        heartView.layoutParams = heartparams

                        stressView.layoutParams = stressparams

                        oxygenView.layoutParams = oxygenparams

                        bpmImageView.layoutParams = imgBPparam
                        oxygenImageView.layoutParams = imgOxparam

                        linearLayout.addView(dateView)
                        linearLayout.addView(timeView)
                        linearLayout.addView(heartView)
                        linearLayout.addView(oxygenView)
                        linearLayout.addView(stressView)
                        imageLayout.addView(bpmImageView)
                        imageLayout.addView(oxygenImageView)
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase", "Error reading data: ${error.message}")
                }
            })
        }
    }
    private fun showAssistDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Emergency")
        builder.setMessage("The Patience Needs Assistance")
        builder.setPositiveButton("Treated") { dialog, which ->
        }
        builder.show()
    }
    private fun showMedicalDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Emergency")
        builder.setMessage("Seek Medical Help")
        builder.setPositiveButton("Treated") { dialog, which ->
        }
        builder.show()
    }
}
