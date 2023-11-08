package com.example.noteappwithfirebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteappwithfirebase.R
import com.example.noteappwithfirebase.adapter.NoteAdapter
import com.example.noteappwithfirebase.databinding.ActivityMainBinding
import com.example.noteappwithfirebase.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val noteList = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        auth = FirebaseAuth.getInstance()

        binding!!.btnInfor.setOnClickListener {
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }
        binding!!.btnCreateNote.setOnClickListener {
            val intent = Intent(this,AddNoteActivity::class.java)
            startActivity(intent)
            finish()
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(auth.currentUser!!.uid)

        val recyclerView = findViewById<RecyclerView>(R.id.rcvNotes)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        val adapter = NoteAdapter(noteList){
            val intent = Intent(this,EditNoteActivity::class.java)
                .putExtra("noteId",it.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                noteList.clear() // Xóa danh sách ghi chú cũ
                for (childSnapshot in snapshot.children) {
                    val note = childSnapshot.getValue(Note::class.java)
                    noteList.add(note!!)
                }
                adapter.notifyDataSetChanged()
                if(noteList.size==0){
                    binding!!.rcvNotes.visibility = View.GONE
                    binding!!.emptyList.visibility = View.VISIBLE
                }
                else{
                    binding!!.rcvNotes.visibility = View.VISIBLE
                    binding!!.emptyList.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load notes: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })



    }
}