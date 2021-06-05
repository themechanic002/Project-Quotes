package com.project.quotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var realmManager: RealmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Quotes 개별 아이템들을 담을 리스트 생성
        val items = mutableListOf<Item>()


        //Realm 사용
        Realm.init(this@MainActivity)
        val config: RealmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
        val realm = Realm.getDefaultInstance()
        realmManager = RealmManager(realm)



        //add_quote_btn 버튼 눌렀을 때
        add_quote_btn.setOnClickListener {

            val example_item = Item("음악", "제발 단 한 번이라도 너를 볼 수 있다면", "G-Dragon 무제", "지드래곤의 가사 중 한 구절")
            realmManager.createOnRealm(example_item)



            val intent = Intent(this@MainActivity, AddQuoteActivity::class.java)

            /*//putExtra로 보낼 수 있도록 HashMap이었던 폴더 리스트를 ArrayList로 변환
            val folders = ArrayList<String>()
            for (i in 0 until folderList.size) {
                folders.add(folderList.keys.elementAt(i))
            }*/


            //다른 인텐트로 폴더 리스트 정보 담아서 보내기
            //Realm에서 폴더 리스트 정보를 불러와서 putExtra로 보내기
            intent.putExtra("folders", realmManager.findFolders())
            startActivity(intent)
        }


        //RecyclerView 어댑터 생성
        val layoutInflater = LayoutInflater.from(this@MainActivity)
        val adapter = MyRecyclerViewAdapter(layoutInflater, realmManager.findAll())
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this@MainActivity)
        recycler_view.addItemDecoration(
                DividerItemDecoration(
                        recycler_view.context,
                        RecyclerView.VERTICAL
                )
        )

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val newFolder = data?.getStringExtra("SavedFolder").toString()
        val newSentence = data?.getStringExtra("SavedSentence").toString()
        val newSource = data?.getStringExtra("SavedSource").toString()
        val newDescription = data?.getStringExtra("SavedDescription").toString()
        realmManager.createOnRealm(Item(newFolder, newSentence, newSource, newDescription))
        data?.getStringExtra("SavedDescription")
        super.onActivityResult(requestCode, resultCode, data)
    }
}