package com.project.quotes

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewAdapter(
    val layoutInflater: LayoutInflater,
    val items: MutableList<Item>,
    val activity: Activity,
    val realmManager: RealmManager
) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var h_folder = view.findViewById<TextView>(R.id.folder)
        var h_sentence = view.findViewById<TextView>(R.id.sentence)
        var h_source = view.findViewById<TextView>(R.id.source)

        init{

            /*val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intentData = result.data

                    val newFolder = intentData?.getStringExtra("SavedFolder").toString()
                    val newSentence = intentData?.getStringExtra("SavedSentence").toString()
                    val newSource = intentData?.getStringExtra("SavedSource").toString()
                    val newDescription = intentData?.getStringExtra("SavedDescription").toString()

                }
            }*/

            view.setOnLongClickListener {

                val inflater = LayoutInflater.from(layoutInflater.context)
                val quote_longclick = inflater.inflate(R.layout.quote_longclick, null)
                val longclickMessage = AlertDialog.Builder(layoutInflater.context).setView(quote_longclick)
                        .setTitle("")
                        .show()

                val longclick_delete = quote_longclick.findViewById<Button>(R.id.longclick_delete)
                longclick_delete.setOnClickListener {

                    val alertDialog = AlertDialog.Builder(
                            layoutInflater.context,
                            android.R.style.Theme_DeviceDefault_Light_Dialog
                    ).setTitle("삭제").setMessage("해당 파일을 삭제하시겠습니까?")
                            .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(layoutInflater.context, "삭제되었습니다", Toast.LENGTH_LONG).show()
                                realmManager.deleteFromRealm(adapterPosition)

                                //새로고침 (notifyDataSetChanged()가 잘 안먹음)
                                activity.finish()
                                activity.overridePendingTransition(0, 0)
                                activity.startActivity(activity.intent)
                                activity.overridePendingTransition(0,0)

                                longclickMessage.dismiss()
                                dialog.dismiss()
                            })
                            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                                dialog.dismiss()
                                longclickMessage.dismiss()
                            })
                            .show()

                }

                return@setOnLongClickListener true
            }



            view.setOnClickListener {
                val intent = Intent(activity, QuoteDetailActivity::class.java)
                intent.putExtra("Index (main->detail)", adapterPosition)
                /*intent.putExtra("folders (main->detail)", realmManager.findFolders())
                intent.putExtra("Quote_Detail_Folder (main->detail)", items[adapterPosition].folder)
                intent.putExtra("Quote_Detail_Sentence (main->detail)", items[adapterPosition].sentence)
                intent.putExtra("Quote_Detail_Source (main->detail)", items[adapterPosition].source)
                intent.putExtra("Quote_Detail_Description (main->detail)", items[adapterPosition].description)*/
                activity.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.h_folder.setText(items.get(position).folder)
        holder.h_sentence.setText(items.get(position).sentence)
        if(items.get(position).source != " ")
            holder.h_source.setText("- " + items.get(position).source + " ")
        else
            holder.h_source.setText(" ")
    }

    override fun getItemCount(): Int {
        return items.size
    }
}