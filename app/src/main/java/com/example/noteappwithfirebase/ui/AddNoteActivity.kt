package com.example.noteappwithfirebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.noteappwithfirebase.databinding.ActivityAddNoteBinding
import com.example.noteappwithfirebase.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNoteActivity : AppCompatActivity() {
    private var binding:ActivityAddNoteBinding?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        auth = FirebaseAuth.getInstance()

        binding!!.btnBack.setOnClickListener {
            finish()
        }

        binding!!.btnSaveNote.setOnClickListener {
            //Save note handle
            val title = binding!!.edtNoteTitle.text.toString()
            val content = binding!!.edtNoteContent.text.toString()

            if(title.isNotEmpty()&&content.isNotEmpty()){
                addNote(title,content)
            }
            else{
                Toast.makeText(this, "Data is not empty!", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun addNote(title: String, content: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(auth.currentUser!!.uid)

        val noteId = databaseReference.push().key

        val note = Note(noteId!!, title, content)

        databaseReference.child(noteId).setValue(note)
            .addOnSuccessListener {
                // Note added successfully
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                // Handle the error if the note could not be added
                Toast.makeText(this, "Failed to add note. ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}