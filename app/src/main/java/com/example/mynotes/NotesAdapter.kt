package com.example.mynotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.databinding.ItemNoteBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.SimpleDateFormat
import java.util.Locale

class NotesAdapter(
    options: FirestoreRecyclerOptions<Note>,
    private val listener: OnNoteClickListener
) : FirestoreRecyclerAdapter<Note, NotesAdapter.ViewHolder>(options) {

    // Interface for click listener
    interface OnNoteClickListener {
        fun onNoteClick(note: Note)
    }

    class ViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Note) {
        // Get the actual document ID from Firestore
        val documentId = snapshots.getSnapshot(position).id

        // Create a copy of the model with the correct document ID
        val noteWithId = model.copy(id = documentId)

        with(holder.binding) {
            tvTitle.text = noteWithId.title
            tvContent.text = noteWithId.content
            tvDate.text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                .format(noteWithId.timestamp)
        }

        holder.itemView.setOnClickListener { listener.onNoteClick(noteWithId) }
    }
}