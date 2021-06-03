package com.project.quotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewAdapter(
    val layoutInflater: LayoutInflater,
    val items: MutableList<Item>
) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var h_folder = view.findViewById<TextView>(R.id.folder)
        var h_sentence = view.findViewById<TextView>(R.id.sentence)
        var h_source = view.findViewById<TextView>(R.id.source)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.h_folder.setText(items.get(position).folder)
        holder.h_sentence.setText(items.get(position).sentence)
        holder.h_source.setText("- " + items.get(position).source + " 中 ")
    }

    override fun getItemCount(): Int {
        return items.size
    }
}