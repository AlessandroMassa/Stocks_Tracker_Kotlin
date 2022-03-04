package com.massa844853.stockstracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.massa844853.stockstracker.R
import com.massa844853.stockstracker.models.Message

class ChatAdapter(private val messagges: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //ritorno il tipo di messaggio
    override fun getItemViewType(position: Int): Int {
        return messagges[position].typeMessage
    }

    //creo l'holder in base al tipo
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        when (viewType) {
            0 -> {
                view = layoutInflater.inflate(R.layout.item_container_received_message, parent, false)
                return ViewHolder0(view)
            }
            1 -> {
                view = layoutInflater.inflate(R.layout.item_container_sent_message, parent, false)
                return ViewHolder1(view)
            }
        }

        return ViewHolder0(layoutInflater.inflate(R.layout.item_container_received_message, parent, false))
    }

    // associo i dati all'holder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (messagges[position].typeMessage) {
            0 -> {
                val viewHolder0 = holder as ViewHolder0
                viewHolder0.textViewUsername.text = messagges[position].username
                viewHolder0.textViewMessage.text = messagges[position].message
            }
            1 -> {
                val viewHolder1 = holder as ViewHolder1
                viewHolder1.textViewMessage.text = messagges[position].message
            }
        }
    }

    fun addItem(message: Message) {
        messagges.add(message)
    }

    override fun getItemCount(): Int {
        return messagges.size
    }

    //2 classi di viewholder per i 2 tipi di messaggio
    internal inner class ViewHolder0(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewUsername: TextView
        var textViewMessage: TextView

        init {
            textViewUsername = itemView.findViewById(R.id.textUsername)
            textViewMessage = itemView.findViewById(R.id.textMessage)
        }
    }

    internal inner class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewMessage: TextView

        init {
            textViewMessage = itemView.findViewById(R.id.textMessage)
        }
    }
}