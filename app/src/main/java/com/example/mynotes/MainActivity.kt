package com.example.mynotes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.databinding.ActivityMainBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NotesAdapter.OnNoteClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NoteViewModel
    private var adapter: NotesAdapter? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "My Notes"

        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        setupRecyclerView()

        // Check if you have a FAB in your layout
        // If not, you may need to add it or comment this out
        binding.fabAddNote?.setOnClickListener {
            startActivity(Intent(this, NoteActivity::class.java))
        }

        // Alternative: If your FAB has a different ID, use the correct ID
        // findViewById<FloatingActionButton>(R.id.fab_add_note)?.setOnClickListener {
        //     startActivity(Intent(this, NoteActivity::class.java))
        // }
    }

    private fun setupRecyclerView() {
        val query = viewModel.getNotesQuery()

        // Create FirestoreRecyclerOptions
        val options = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(query) { snapshot ->
                // Map Firestore document to Note object with correct ID
                val note = snapshot.toObject(Note::class.java) ?: Note()
                note.copy(id = snapshot.id)
            }
            .setLifecycleOwner(this)
            .build()

        // Create and set up adapter
        adapter = NotesAdapter(options, this)

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // Set up search functionality
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { performSearch(it) }
                return true
            }
        })

        return true
    }

    private fun performSearch(query: String) {
        // Clean up existing adapter
        adapter?.stopListening()

        // Create new options with filtered query
        val searchQuery = viewModel.getFilteredQuery(query)
        val options = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(searchQuery) { snapshot ->
                val note = snapshot.toObject(Note::class.java) ?: Note()
                note.copy(id = snapshot.id)
            }
            .setLifecycleOwner(this)
            .build()

        // Create and set up new adapter with filtered data
        adapter = NotesAdapter(options, this)
        binding.rvNotes.adapter = adapter
        adapter?.startListening()
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra("note", note)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                // Make sure LoginActivity exists or replace with appropriate activity
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
                true
            }
            R.id.action_dark_mode -> {
                toggleDarkMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleDarkMode() {
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }
}