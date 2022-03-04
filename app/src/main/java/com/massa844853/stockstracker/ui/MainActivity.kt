package com.massa844853.stockstracker.ui
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.models.User
import com.massa844853.stockstracker.viewmodels.NewsPricesViewModel
class MainActivity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    var user: User? = null
        private set
    private var databaseReference: DatabaseReference? = null
    var newsPricesViewModel: NewsPricesViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        newsPricesViewModel = ViewModelProvider(this).get(NewsPricesViewModel::class.java)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val imageButtonLogin = toolbar.findViewById<ImageView>(R.id.buttonLogin)
        if (auth!!.currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(auth!!.currentUser!!.uid)
            val postListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    user = dataSnapshot.getValue(User::class.java)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext, "Error. " + databaseError.message, Toast.LENGTH_LONG).show()
                }
            }
            databaseReference!!.addValueEventListener(postListener)
        }
        imageButtonLogin.setOnClickListener {
            if (auth!!.currentUser != null) auth!!.signOut()
            startActivity(Intent(applicationContext, AuthActivity::class.java))
            finish()
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment? //container per i fragment
        val navContoller = navHostFragment!!.navController //componente che controlla la navigazione all'interno del grafo definito
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view) //riferimento al bottomnavigationview
        NavigationUI.setupWithNavController(bottomNavigationView, navContoller) //associa il navcontroller al bottomnavigationview
    }
}