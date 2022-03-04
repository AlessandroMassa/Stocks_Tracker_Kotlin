package com.massa844853.stockstracker.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.models.User

class RegisterFragment : Fragment() {
    private var auth: FirebaseAuth? = null
    private var database: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val editTextEmail = view.findViewById<EditText>(R.id.email)
        val editTextPassword = view.findViewById<EditText>(R.id.password)
        val editTextUsername = view.findViewById<EditText>(R.id.username)
        val buttonRegisterEmail = view.findViewById<Button>(R.id.register_button)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarRegister)
        progressBar.visibility = View.INVISIBLE
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        buttonRegisterEmail.setOnClickListener(View.OnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val username = editTextUsername.text.toString()
            if (TextUtils.isEmpty(email)) {
                editTextEmail.error = "Email is required."
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                editTextPassword.error = "Password is required."
                return@OnClickListener
            }
            if (TextUtils.isEmpty(username)) {
                editTextUsername.error = "Username is required."
                return@OnClickListener
            }
            progressBar.visibility = View.VISIBLE
            auth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uId = auth!!.currentUser!!.uid
                    val newUser = User(System.currentTimeMillis(), 0, username)
                    database!!.child("users").child(uId).setValue(newUser)
                    startActivity(Intent(context, MainActivity::class.java))
                    requireActivity().finish()
                } else {
                    Toast.makeText(context, "Error. " + task.exception!!.message, Toast.LENGTH_LONG).show()
                }
                progressBar.visibility = View.INVISIBLE
            }
        })
        return view
    }
}