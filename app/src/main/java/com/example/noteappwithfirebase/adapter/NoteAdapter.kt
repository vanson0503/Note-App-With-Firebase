package com.example.noteappwithfirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappwithfirebase.R
import com.example.noteappwithfirebase.model.Note

class NoteAdapter(
    private val notes:List<Note>,
    private val itemOnClick : (Note) -> Unit
):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    class NoteViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val item:LinearLayout = itemView.findViewById(R.id.itemView)
        val title:TextView = itemView.findViewById(R.id.txtTitle)
        val content:TextView = itemView.findViewById(R.id.txtContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
       return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false))
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.apply {
            title.text = note.title
            content.text = note.content
            item.setOnClickListener {
                itemOnClick(note)
            }
        }
    }
}