package com.example.mynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mynotes.databinding.ActivityNoteBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.Date

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding
    private val viewModel: NoteViewModel by viewModels()
    private var currentNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Note"

        // Get the note if we're editing
        currentNote = intent.getParcelableExtra("note")
        currentNote?.let {
            binding.etTitle.setText(it.title)
            binding.etContent.setText(it.content)
            supportActionBar?.title = "Edit Note"
        }

        binding.btnSave.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()

        if (title.isBlank() || content.isBlank()) {
            Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // If we have a current note, update it, otherwise create a new one
        if (currentNote != null) {
            val updatedNote = currentNote!!.copy(
                title = title,
                content = content,
                timestamp = Date()
            )
            viewModel.updateNote(updatedNote)
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.addNote(title, content)
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_delete -> {
                currentNote?.let {
                    viewModel.deleteNote(it.id)
                    Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Only show delete option if editing an existing note
        if (currentNote != null) {
            menuInflater.inflate(R.menu.note_menu, menu)
        }
        return true
    }
}