package com.example.noteappwithfirebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.noteappwithfirebase.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccountActivity : AppCompatActivity() {
    private var binding:ActivityCreateAccountBinding?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding!!.btnCreateAccount.setOnClickListener {
            //Create account handle
            val fullName = binding!!.edtFullName.text.toString()
            val email = binding!!.edtEmailAddress.text.toString()
            val password = binding!!.edtPassword.text.toString()

            if(fullName.isNotEmpty()&&email.isNotEmpty()&&password.isNotEmpty()){
                createAccount(fullName,email,password)
            }
            else{
                Toast.makeText(this, "Data is not empty!${fullName}${email}${password}", Toast.LENGTH_SHORT).show()
            }
        }


        binding!!.loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createAccount(fullName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val user = auth.currentUser

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap["userId"] = email
                    hashMap["fullName"] = fullName
                    hashMap["imageUrl"] = ""

                    databaseReference.setValue(hashMap)
                        .addOnCompleteListener {
                            auth.signOut()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                }
            }
    }
}