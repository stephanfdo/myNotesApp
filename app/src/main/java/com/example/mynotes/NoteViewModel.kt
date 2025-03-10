package com.example.mynotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import java.util.Date

class NoteViewModel : ViewModel() {
    private val repository = NoteRepository()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val currentUserId = auth.currentUser?.uid ?: ""

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    private var firestoreListener: ListenerRegistration? = null

    fun getNotesQuery(): Query {
        return db.collection("notes")
            .whereEqualTo("userId", currentUserId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun addNote(title: String, content: String) {
        val newNote = Note(
            title = title,
            content = content,
            userId = currentUserId,
            timestamp = Date()
        )
        viewModelScope.launch {
            repository.addNote(newNote)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
        }
    }

    // Improved search functionality
    fun getFilteredQuery(query: String): Query {
        return if (query.isBlank()) {
            // Return all notes if query is empty
            db.collection("notes")
                .whereEqualTo("userId", currentUserId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
        } else {
            // This approach works better for prefix searches
            db.collection("notes")
                .whereEqualTo("userId", currentUserId)
                .orderBy("title")
                .startAt(query)
                .endAt(query + '\uf8ff')
        }
    }

    // Add a method for content-based search
    fun getContentFilteredQuery(query: String): Query {
        return db.collection("notes")
            .whereEqualTo("userId", currentUserId)
            .orderBy("content")
            .startAt(query)
            .endAt(query + '\uf8ff')
    }

    override fun onCleared() {
        super.onCleared()
        firestoreListener?.remove()
    }
}