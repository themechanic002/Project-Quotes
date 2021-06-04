package com.project.quotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //Quotes 개별 아이템들을 담을 리스트 생성
        val items = mutableListOf<Item>()
        items.add(Item("음악", "제발 단 한 번이라도 너를 볼 수 있다면", "G-Dragon 무제"))
        items.add(Item("음악", "온통 너의 생각 뿐이야 나도 미치겠어", "브레이브걸스 Rollin'"))

        //폴더를 따로 모아놓은 HashMap
        val folderList = HashMap<String, Int>()
        for(i in 0 until items.size)
            folderList.put(items.get(i).folder, i)
        
        //add_quote 버튼 눌렀을 때
        add_quote.setOnClickListener {
            val intent = Intent(this@MainActivity, AddQuoteActivity::class.java)

            //putExtra로 보낼 수 있도록 HashMap이었던 폴더 리스트를 ArrayList로 변환
            val folders = ArrayList<String>()
            for(i in 0 until folderList.size){
                folders.add(folderList.keys.elementAt(i))
            }
            //다른 인텐트로 폴더 리스트 정보 담아서 보내기
            intent.putExtra("folders", folders)
            startActivity(intent)
        }


        //RecyclerView 어댑터 생성
        val layoutInflater = LayoutInflater.from(this@MainActivity)
        val adapter = MyRecyclerViewAdapter(layoutInflater, items)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this@MainActivity)
        recycler_view.addItemDecoration(DividerItemDecoration(recycler_view.context, RecyclerView.VERTICAL))

    }
}