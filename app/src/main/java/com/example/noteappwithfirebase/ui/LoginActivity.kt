package com.example.noteappwithfirebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.noteappwithfirebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class LoginActivity : AppCompatActivity() {
    private var binding:ActivityLoginBinding?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding!!.btnLogin.setOnClickListener {
            //Login handle
            val email = binding!!.edtEmailAddress.text.toString()
            val password = binding!!.edtPassword.text.toString()
           if(email.isNotEmpty()&&password.isNotEmpty()){
               auth.signInWithEmailAndPassword(email,password)
                   .addOnCompleteListener {
                       if(it.isSuccessful){
                           val intent = Intent(this, MainActivity::class.java)
                           startActivity(intent)
                           finish()
                       }
                   }
                   .addOnFailureListener {
                       Toast.makeText(this, "Email or password is not correct!", Toast.LENGTH_SHORT).show()
                   }
           }
           else{
               Toast.makeText(this, "Data is not empty!", Toast.LENGTH_SHORT).show()
           }

        }

        binding!!.registeredLink.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}