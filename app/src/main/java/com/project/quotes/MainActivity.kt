package com.project.quotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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

    //뒤로가기 한번 더 눌렀을 때 종료하는 타이머
    private var time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realmManager = RealmManager(this@MainActivity)

        /*//Realm 사용
        Realm.init(this@MainActivity)
        val config: RealmConfiguration = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
        realmManager = RealmManager()*/


        //RecyclerView 어댑터 생성
        val layoutInflater = LayoutInflater.from(this@MainActivity)
        val adapter = MyRecyclerViewAdapter(
                layoutInflater,
                realmManager.findAll(),
                activity = this@MainActivity,
                realmManager
        )
        recycler_view.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.addItemDecoration(
                DividerItemDecoration(
                        recycler_view.context,
                        RecyclerView.VERTICAL
                )
        )


        /*//intent로 보냈던 데이터들 다시 받는 옛날방식
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                //새로운 quote를 만들었을 때
                if (result.resultCode == Activity.RESULT_OK) {
                    val intentData = result.data

                    val newFolder = intentData?.getStringExtra("SavedFolder").toString()
                    val newSentence = intentData?.getStringExtra("SavedSentence").toString()
                    val newSource = intentData?.getStringExtra("SavedSource").toString()
                    val newDescription = intentData?.getStringExtra("SavedDescription").toString()
                    realmManager.createOnRealm(
                        Item(
                            newFolder,
                            newSentence,
                            newSource,
                            newDescription
                        )
                    )
                }
                if (result.resultCode == 200) {
                    val intentData = result.data

                    var position = intentData?.getIntExtra("Index of edited quote (Edit->Main)", 0)
                    if(position == null)
                        return@registerForActivityResult

                    val editedFolder = intentData?.getStringExtra("EditedFolder").toString()
                    val editedSentence = intentData?.getStringExtra("EditedSentence").toString()
                    val editedSource = intentData?.getStringExtra("EditedSource").toString()
                    val editedDescription =
                        intentData?.getStringExtra("EditedDescription").toString()

                    realmManager.editOnRealm(Item(editedFolder, editedSentence, editedSource, editedDescription), position)
                }
            }*/


        //RecyclerView의 최신화를 위해 추가했을 때 그 아이템의 position을 가져다 주고 최신화함.
        val resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    //새로운 quote를 만들었을 때
                    if (result.resultCode == Activity.RESULT_OK) {
                        val intentData = result.data
                        if (intentData != null)
                            adapter.notifyItemInserted(adapter.itemCount)
                    }
                }


        //add_quote_btn 버튼 눌렀을 때
        add_quote_btn.setOnClickListener {


            //다른 인텐트로 폴더 리스트 정보 담아서 보내기
            //Realm에서 폴더 리스트 정보를 불러와서 putExtra로 보내기
            val intent = Intent(this@MainActivity, AddQuoteActivity::class.java)
            intent.putExtra("folders", realmManager.findFolders())
            resultLauncher.launch(intent)
        }
    }


    override fun onRestart() {
        //이 Activity 새로고침
        finish()
        overridePendingTransition(0, 0)
        startActivity(intent)
        overridePendingTransition(0, 0)
        super.onRestart()
    }


    //뒤로가기 한번 더 누르면 종료
    override fun onBackPressed() {

        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            Toast.makeText(this@MainActivity, "한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() - time < 2000) {
            finish()
        }
    }
}