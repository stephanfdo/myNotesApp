MyNotes - Firebase Note Taking Application
Overview
MyNotes is a modern Android application built with Kotlin that allows users to create, read, update, and delete notes. The app uses Firebase Authentication for user management and Firestore for storing and synchronizing notes across devices in real-time.
Features
User Authentication

Secure login and registration using Firebase Authentication
Persistent user sessions
Logout functionality

Note Management

Create new notes with title and content
View a list of all notes with titles, previews, and timestamps
Open and edit existing notes
Delete notes with swipe gestures and undo functionality
Real-time synchronization across multiple devices

User Interface

Clean and intuitive material design interface
RecyclerView with efficient data loading
Search functionality to filter notes
Responsive layout that works on different screen sizes

Data Persistence

Firestore cloud storage for cross-device access
Offline capabilities for creating and editing notes without internet connection
Automatic data synchronization when connectivity is restored

Technical Architecture
Project Structure

Model: Contains the Note data class
View: Activities and UI components
ViewModel: Handles data processing and business logic
Repository: Manages data operations with Firestore

Key Components
Activities

MainActivity: Displays the list of notes and handles navigation
AuthActivity: Manages user authentication
NoteActivity: Used for creating and editing notes

Adapters

NotesAdapter: FirestoreRecyclerAdapter that manages note display in RecyclerView

ViewModels

NoteViewModel: Handles data operations and state management

Repository

NoteRepository: Abstracts Firestore operations for notes

Setup Instructions
Prerequisites

Android Studio Flamingo (2023.2.1) or newer
JDK 17 or newer
Firebase account
Google Play services on the test device

Firebase Setup

Create a new Firebase project at Firebase Console
Add an Android app to your Firebase project
Download the google-services.json file and place it in the app module directory
Enable Firebase Authentication and Firestore Database in Firebase Console

Project Configuration

Clone the repository:
Copygit clone https://github.com/stephanfdo/myNotesApp.git

Open the project in Android Studio
Sync the project with Gradle files
Make sure the Firebase configuration is properly set up

Running the Application

Connect an Android device or use an emulator
Build and run the application from Android Studio
Register a new account or login with existing credentials
Start creating and managing your notes

Dependencies
Firebase
gradleCopyimplementation("com.google.firebase:firebase-auth:23.2.0")
implementation("com.google.firebase:firebase-firestore:25.1.2")
implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
AndroidX and Lifecycle
gradleCopyimplementation("androidx.core:core-ktx:1.9.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

UI Components

implementation("com.google.android.material:material:1.12.0")


implementation("androidx.credentials:credentials-play-services-auth:1.3.0")


Screenshots 

![p1](https://github.com/user-attachments/assets/6dfc657f-fcd2-4369-b177-552f55c9b551)
![p2](https://github.com/user-attachments/assets/0f94cd1e-6688-49c6-a413-d5bd189662e8)
![p4](https://github.com/user-attachments/assets/bc140002-f21c-460a-b4d2-a2914882ab5f)

![p5](https://github.com/user-attachments/assets/5b3c7be9-e422-4493-b424-478e4a46cb19)
![p6](https://github.com/user-attachments/assets/ef5afb6e-6487-4a39-80d2-62a537339757)
![p7](https://github.com/user-attachments/assets/83d743c5-cc84-4fc6-aa4a-eb429103b5d6)
![p8](https://github.com/user-attachments/assets/8cf71973-840c-48de-b451-23b4caf3eb5b)

