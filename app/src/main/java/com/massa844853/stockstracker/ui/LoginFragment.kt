package com.massa844853.stockstracker.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.massa844853.stockstracker.R

class LoginFragment : Fragment() {
    private var auth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val editTextEmail = view.findViewById<EditText>(R.id.username)
        val editTextPassword = view.findViewById<EditText>(R.id.password)
        val buttonLoginEmail = view.findViewById<Button>(R.id.login_button)
        val buttonSkip = view.findViewById<TextView>(R.id.skip_action)
        val createAccount = view.findViewById<TextView>(R.id.textview_create_account)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBarLogin)
        progressBar.visibility = View.INVISIBLE
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        buttonLoginEmail.setOnClickListener(View.OnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (TextUtils.isEmpty(email)) {
                editTextEmail.error = "Email is required."
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                editTextPassword.error = "Password is required."
                return@OnClickListener
            }
            progressBar.visibility = View.VISIBLE
            auth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    databaseReference!!.child("users").child(auth!!.currentUser!!.uid).child("loginDateTime").setValue(System.currentTimeMillis())
                    startActivity(Intent(context, MainActivity::class.java))
                    requireActivity().finish()
                } else {
                    Toast.makeText(context, "Error. " + task.exception!!.message, Toast.LENGTH_LONG).show()
                }
                progressBar.visibility = View.INVISIBLE
            }
        })
        createAccount.setOnClickListener { v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment) }
        buttonSkip.setOnClickListener {
            startActivity(Intent(context, MainActivity::class.java))
            requireActivity().finish()
        }
        return view
    }
}