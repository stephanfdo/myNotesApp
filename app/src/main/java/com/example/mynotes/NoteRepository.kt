package com.example.mynotes

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class NoteRepository {
    private val db = FirebaseFirestore.getInstance()
    private val notesCollection = db.collection("notes")

    fun getNotes(userId: String) = notesCollection
        .whereEqualTo("userId", userId)
        .orderBy("timestamp", Query.Direction.DESCENDING)

    suspend fun addNote(note: Note) = withContext(Dispatchers.IO) {
        // When adding a new note, let Firestore generate the document ID
        val docRef = notesCollection.add(note).await()
        // Return the new document ID
        docRef.id
    }

    suspend fun updateNote(note: Note) = withContext(Dispatchers.IO) {
        // Make sure to use the correct document ID
        notesCollection.document(note.id).set(note).await()
    }

    suspend fun deleteNote(noteId: String) = withContext(Dispatchers.IO) {
        // Make sure noteId is a valid document ID
        notesCollection.document(noteId).delete().await()
    }
}