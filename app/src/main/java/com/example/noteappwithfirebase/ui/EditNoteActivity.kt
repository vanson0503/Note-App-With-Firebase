package com.example.noteappwithfirebase.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import com.example.noteappwithfirebase.R
import com.example.noteappwithfirebase.databinding.ActivityEditNoteBinding
import com.example.noteappwithfirebase.databinding.ActivityMainBinding
import com.example.noteappwithfirebase.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditNoteActivity : AppCompatActivity() {
    private var binding: ActivityEditNoteBinding?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        auth = FirebaseAuth.getInstance()

        binding!!.btnBack.setOnClickListener {
            finish()
        }

        val noteId = intent.getStringExtra("noteId")

        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(auth.currentUser!!.uid).child(noteId!!)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val note = snapshot.getValue(Note::class.java)
                    if (note != null) {
                        binding!!.edtNoteTitle.setText(note.title)
                        binding!!.edtNoteContent.setText(note.content)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditNoteActivity, "Failed to load note details: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        binding!!.menu.setOnClickListener { view ->
            val popupMenu = PopupMenu(this, view)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.save -> {
                        val title = binding!!.edtNoteTitle.text.toString()
                        val content = binding!!.edtNoteContent.text.toString()

                        if (title.isNotEmpty() && content.isNotEmpty()) {
                            saveUpdatedNote(noteId, title, content)
                        } else {
                            Toast.makeText(this, "Data is not empty!", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }
                    R.id.delete -> {
                        deleteNote()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }



    }

    private fun saveUpdatedNote(noteId:String,title: String, content: String) {
        val updatedNote = Note(noteId, title, content)
        databaseReference.setValue(updatedNote)
            .addOnSuccessListener {
                Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update note. ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun deleteNote() {
        databaseReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete note. ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }



}