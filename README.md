______________________________________ 

Module name: Open-Source Coding (Introduction)  

Module code: OPSC7311  

Assessment Type: POE Part 3 

Group Members: 

Nathan Nayager: ST10039749 

Bianca Marcell Munsami: ST10069986 

Bai Hong He (Jackie):ST10030735 

Cristina Rodrigues:ST10049126 

Uzair:ST10045844 

Andrew Maswanganye: ST10090907 

 ______________________________________ 
 

GitHub link:  

Part 2 video: https://youtu.be/pRF3AFuXuZk?si=8V4FlNN-5O0rJU5y 

Part 3 video:  https://youtu.be/74TGXTLd_ZI?si=NveuFWa5i5F9GbsM 

Introduction 

CHRONOLOG is a time-tracking application, which allows users to create time sheet entries. The application gives users options in customizable functions in the app such as setting dates, start and end times, writing descriptions, creating categories, and entering tasks in created categories, for the time sheets. A distinct feature is that the user records data by starting and stopping a timer which saves the time collected as data that can analyzed. The application uses passwords as an encrypted method. The user can specify daily goals in hours worked and specify the maximum and minimum hours that need to be reached. Chronolog simplifies the user experience by using graphs to display data such as total hours that are worked each day with the addition of minimum and maximum goals displayed all together. User social features include sending messages, participating on a leaderboard of all users, and providing referring options to friends. 

Distinctive features 

Advanced Visualization Tools allow for a variety of charts (pie charts and line charts), graphs (bar graphs), and dashboards to be used with distinctive visualizations for viewing. From interactive charts to 3D visual displays users to view their data with effective analysis. 

Offline data analysis enables users to work with their data when not connected to the internet. This allows users to make use of the application in internet-unavailable environments such as aircraft or make changes that are needed as soon as possible. Thus, fostering trust that the application can be relied upon when needed to be used no matter the conditions the user is in. 

User interface  

New features  

Our app now lets users track their work hours through a visual dashboard. We've connected the Firebase Realtime Database to the login and signup activities. Users can see a graph that displays their total work hours, along with their maximum and minimum goals. This graph is powered by MPhillycharts, which ensures their real data is accurately reflected. Look at the three screenshots below to view the changes made: 
 
Database 

The database used in the provided implementation is Firebase Realtime Database. Firebase Realtime Database is a cloud-hosted NoSQL database provided by Google as part of the Firebase platform. It allows you to store and sync data between users in real-time. Data is stored as JSON and synchronized in real-time to every connected client. 

The usage of firebase real time database allows us to satisfy some of our requirements detailed in the planning document, these include:  

Real-Time Synchronization: 

Changes to data are synchronized in real-time across all clients, making it ideal for applications that require live updates (e.g., chat applications, collaborative tools). 

Offline Capabilities: 

Firebase Realtime Database supports offline access. Data is cached locally, and any changes made while offline are synchronized when the device comes back online. 

Ease of Use: 

Firebase offers a straightforward API and integrates well with Firebase Authentication, making user authentication and data security simpler to implement. 

Scalability: 

 Automatically scales with your application. Google's infrastructure ensures the database can handle many simultaneous connections and high traffic. 

Cross-Platform Support: 

 Firebase Realtime Database supports multiple platforms, including iOS, Android, and web, allowing you to build cross-platform applications easily. 

Security: 

 Provides robust security features, including authentication and rules for validating data and user access, ensuring that data is secure. 

Screenshots of our running Database 

Figure 1: Firebase showing Chronlog Application connected. 

Figure 2: Firebase showing Chronlog Application usage throughout testing. 

Figure 3: Firebase console showing Chronlog Application database tables. 

Figure 4: Firebase console showing categories table. 

Figure 5: Firebase console showing daily goals table. 

Figure 6: Firebase console showing images stored in Realtime database. 

Figure 7: Firebase console showing tasks table Realtime database. 

Figure 8: Firebase console showing users table Realtime database. 

Evidence of connection established in app 

Figure 9: Firebase SDK in our build. Gradle file. 

Figure 10: Firebase connected. 

To use the application, users need to create an account and log in. This usually involves storing user information in a database. Below are snippets of database usage, please use the rest of the project for usage. This is merely a demonstration:  

JSON File 

Reference List___________________________________________________________ 

Android Knowledge. n.d. Login and Signup using Firebase Realtime Database in Android Studio | Kotlin [Online]. Available at: https://www.youtube.com/watch?v=MhLkezKsHbY&ab_channel=AndroidKnowledge 

[Accessed On 8 June 2024]. 

BK, Bernard. 2022. TimeTrackerApp_Android. 

[Online]. Available at: https://github.com/bernardkaminskiBK/TimeTrackerApp_Android/tree/MAIN 

[Accessed On 8 June 2024]. 

Browser stacks. 2023. What are different Android UI Layouts? 10 March 2023. 

[Online]. Available at: https://www.browserstack.com/guide/android-ui-layout 

[Accessed On 8 June 2024]. 

Cherednichenko, S. 2019. How to Quickly Implement Beautiful Charts in Your Android App, 1 March 2019. [Online]. Available at: https://medium.com/@mobindustry/how-to-quickly-implement-beautiful-charts-in-your-android-app-cf4caf050772 

[Accessed On 8 June 2024]. 

Firebase Docs. n.d. Get Started with Firebase Authentication on Android. 

[Online]. Available at: https://firebase.google.com/docs/auth/android/start 

[Accessed On 8 June 2024]. 

Firebase Docs. N.d. Add Firebase to your Android project. 

[Online]. Available at: https://firebase.google.com/docs/android/setup 

[Accessed On 8 June 2024]. 


GeeksforGeeks. 2024. Adding Firebase to Android App, 02 June 2024. 

[Online]. Available at: https://www.geeksforgeeks.org/adding-firebase-to-android-app/ 

[Accessed On 8 June 2024]. 

 
Nyakundi, H.2021.How to Write a Good README File for Your GitHub Project, 8 December 2021. [Online]. Available at: https://www.freecodecamp.org/news/how-to-write-a-good-readme-file/ [Accessed On 8 June 2024]. 


package com.example.chronolog_app 
 
import android.content.Intent 
import android.os.Bundle 
import android.widget.Button 
import android.widget.EditText 
import android.widget.TextView 
import android.widget.Toast 
import androidx.appcompat.app.AppCompatActivity 
import com.google.firebase.database.DataSnapshot 
import com.google.firebase.database.DatabaseError 
import com.google.firebase.database.DatabaseReference 
import com.google.firebase.database.FirebaseDatabase 
import com.google.firebase.database.ValueEventListener 
 
class SignupActivity : AppCompatActivity() { 
 
    private lateinit var fullName: EditText 
    private lateinit var email: EditText 
    private lateinit var phoneNumber: EditText 
    private lateinit var signupUsername: EditText 
    private lateinit var signupPassword: EditText 
    private lateinit var signupConfirmPassword: EditText 
    private lateinit var btnSignUp: Button 
 
    private lateinit var firebaseDatabase: FirebaseDatabase 
    private lateinit var databaseReference: DatabaseReference 
 
    override fun onCreate(savedInstanceState: Bundle?) { 
        super.onCreate(savedInstanceState) 
        setContentView(R.layout.activity_sign_up) 
 
        firebaseDatabase = FirebaseDatabase.getInstance() 
        databaseReference = firebaseDatabase.reference.child("users") 
 
        fullName = findViewById(R.id.fullName) 
        email = findViewById(R.id.email) 
        phoneNumber = findViewById(R.id.phoneNumber) 
        signupUsername = findViewById(R.id.signupUsername) 
        signupPassword = findViewById(R.id.signupPassword) 
        signupConfirmPassword = findViewById(R.id.signupConfirmPassword) 
        btnSignUp = findViewById(R.id.btnsignup) 
 
        btnSignUp.setOnClickListener { 
            val name = fullName.text.toString().trim() 
            val userEmail = email.text.toString().trim() 
            val phone = phoneNumber.text.toString().trim() 
            val username = signupUsername.text.toString().trim() 
            val password = signupPassword.text.toString() 
            val confirmPassword = signupConfirmPassword.text.toString() 
 
            if (name.isNotEmpty() && userEmail.isNotEmpty() && phone.isNotEmpty() && 
                username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) { 
 
                if (password == confirmPassword) { 
                    signupUser(name, userEmail, phone, username, password) 
                } else { 
                    Toast.makeText(this@SignupActivity, "Passwords do not match", Toast.LENGTH_SHORT).show() 
                } 
            } else { 
                Toast.makeText(this@SignupActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show() 
            } 
        } 
 
        // Handle "Already a user? Login" click 
        val tvAlreadyAUser = findViewById<TextView>(R.id.tvAlreadyAUser) 
        tvAlreadyAUser.setOnClickListener { 
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java)) 
        } 
    } 
 
    private fun signupUser(name: String, email: String, phone: String, username: String, password: String) { 
        databaseReference.orderByChild("username").equalTo(username) 
            .addListenerForSingleValueEvent(object : ValueEventListener { 
                override fun onDataChange(dataSnapshot: DataSnapshot) { 
                    if (!dataSnapshot.exists()) { 
                        val id = databaseReference.push().key 
                        val userData = UserData(id, name, email, phone, username, password) 
                        databaseReference.child(id!!).setValue(userData) 
                        Toast.makeText(this@SignupActivity, "Signup Successful", Toast.LENGTH_SHORT).show() 
                        startActivity(Intent(this@SignupActivity, LoginActivity::class.java)) 
                        finish() 
                    } else { 
                        Toast.makeText(this@SignupActivity, "User already exists", Toast.LENGTH_SHORT).show() 
                    } 
                } 
 
                override fun onCancelled(databaseError: DatabaseError) { 
                    Toast.makeText(this@SignupActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show() 
                } 
            }) 
    } 
} 
 

 

{ 
  "project_info": { 
    "project_number": "94578506128", 
    "project_id": "chronolog-ca658", 
    "storage_bucket": "chronolog-ca658.appspot.com" 
  }, 
  "client": [ 
    { 
      "client_info": { 
        "mobilesdk_app_id": "1:94578506128:android:38c20d786c3c6be7481b15", 
        "android_client_info": { 
          "package_name": "com.example.chronolog_app" 
        } 
      }, 
      "oauth_client": [], 
      "api_key": [ 
        { 
          "current_key": "AIzaSyCWoTBEDipW4HcJ45G4i39pA3hPTo13Fm4" 
        } 
      ], 
      "services": { 
        "appinvite_service": { 
          "other_platform_oauth_client": [] 
        } 
      } 
    } 
  ], 
  "configuration_version": "1" 
} 

 
