package com.example.sharing_food.Activity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharing_food.Activity.data.model.Snack
import com.example.sharing_food.R

class SnackBarAdapter(private val snackList: List<Snack>) :
    RecyclerView.Adapter<SnackBarAdapter.SnackViewHolder>() {

    inner class SnackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvSnackName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_snackbar, parent, false)
        return SnackViewHolder(view)
    }

    override fun onBindViewHolder(holder: SnackViewHolder, position: Int) {
        val snack = snackList[position]
        holder.tvName.text = snack.name
    }

    override fun getItemCount(): Int = snackList.size
}

