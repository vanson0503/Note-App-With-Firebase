package com.example.noteappwithfirebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.noteappwithfirebase.R
import com.example.noteappwithfirebase.databinding.ActivityEditNoteBinding
import com.example.noteappwithfirebase.databinding.ActivityProfileBinding
import com.example.noteappwithfirebase.model.Note
import com.example.noteappwithfirebase.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private var binding: ActivityProfileBinding?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.currentUser!!.uid)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        binding!!.txtEmail.text = user.email
                        binding!!.txtFullName.text = user.fullName
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Failed to load user details: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })

        binding!!.btnBack.setOnClickListener {
            finish()
        }

        binding!!.groupLogout.setOnClickListener{
            auth.signOut()
            val intent = Intent(this,GetStartedActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}