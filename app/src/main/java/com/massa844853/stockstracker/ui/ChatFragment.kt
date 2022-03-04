package com.massa844853.stockstracker.ui
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.adapter.ChatAdapter
import com.massa844853.stockstracker.models.Message
import com.massa844853.stockstracker.models.User
import java.util.*
class ChatFragment : Fragment() {
    private var auth: FirebaseAuth? = null
    private var databaseReference: DatabaseReference? = null
    private var user: User? = null
    private var messages: MutableMap<String, Message?>? = null
    private var messagesList: MutableList<Message>? = null
    private var newMessages: MutableList<Message>? = null
    private var updateRecyclerView = false
    private var chatAdapter: ChatAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        messagesList = ArrayList()
        user = (activity as MainActivity?)?.user
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("chat")
        messages = HashMap()
        updateRecyclerView = false
        val editTextMessage = view.findViewById<EditText>(R.id.inputMessage)
        val buttonSend = view.findViewById<ImageView>(R.id.buttonSend)
        val recyclerView: RecyclerView = view.findViewById(R.id.chatRecyclerView)
        chatAdapter = ChatAdapter(messagesList as ArrayList<Message>)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = chatAdapter
        if (auth!!.currentUser != null) {
            buttonSend.setOnClickListener {
                val textMessage = editTextMessage.text.toString()
                editTextMessage.setText("")
                if (!TextUtils.isEmpty(textMessage)) {
                    val message = Message(user!!.username, System.currentTimeMillis(), textMessage)
                    databaseReference!!.parent!!.child("chat").child(auth!!.currentUser!!.uid + "-" + user!!.nextMessage).setValue(message)
                    user!!.nextMessage = user!!.nextMessage + 1
                    databaseReference!!.parent!!.child("users").child(auth!!.currentUser!!.uid).setValue(user)
                }
            }
            databaseReference = FirebaseDatabase.getInstance().reference.child("chat")
            val chatListener: ValueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val typeIndicator: GenericTypeIndicator<Map<String?, Message?>?> = object : GenericTypeIndicator<Map<String?, Message?>?>() {}
                    val messagesSnap = dataSnapshot.getValue(typeIndicator)
                    newMessages = ArrayList()
                    if (messagesSnap != null) {
                        for ((key, value) in messagesSnap) {
                            if (value != null) {
                                if ((messages as HashMap<String, Message?>).get(key) == null && value.sendDate!! > user!!.loginDateTime!!) {
                                    if (value != null) {
                                        if (value.username == user!!.username) {
                                            value.typeMessage = 1
                                        } else {
                                            value.typeMessage = 0
                                        }
                                    }
                                    messages!!.put(key!!, value)
                                    if (value != null) {
                                        newMessages!!.add(value)
                                    }
                                    updateRecyclerView = true
                                }
                            }
                        }
                        if (updateRecyclerView) {
                            (messagesList as ArrayList<Message>).addAll(newMessages as ArrayList<Message>)
                            chatAdapter!!.notifyDataSetChanged()
                            updateRecyclerView = false
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(context, "Error. " + databaseError.message, Toast.LENGTH_LONG).show()
                }
            }
            databaseReference!!.addValueEventListener(chatListener)
        }
        return view
    }
}

