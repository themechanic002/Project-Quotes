package com.project.quotes

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RealmManager(context: Context) {

    val realm: Realm

    init{
        //Realm 사용
        Realm.init(context)
        val config: RealmConfiguration = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
    }



    //Realm에 추가
    fun createOnRealm(item: Item) {

        //이제 Realm이 MainThread에서 안된다고 해서 코루틴으로 비동기 처리
        CoroutineScope(Dispatchers.IO).launch {

            withContext(Main) {
                realm.beginTransaction()
                val realm_item = realm.createObject(Item::class.java)
                realm_item.folder = item.folder
                realm_item.sentence = item.sentence
                realm_item.source = item.source
                realm_item.description = item.description
                realm.commitTransaction()
            }

        }
    }


    //Realm에서 모두 불러오기
    fun findAll(): MutableList<Item> {
        return realm.where(Item::class.java).findAll()
    }


    //Realm에서 folder들만 모두 불러오기
    fun findFolders(): ArrayList<String> {

        val items: List<Item> = realm.where(Item::class.java).findAll()
        val folders = ArrayList<String>()
        for (i in 0 until items.size) {
            if (!folders.contains(items[i].folder))
                folders.add(items[i].folder)
        }

        return folders
    }


    //Realm에서 수정하기
    fun editOnRealm(newItem: Item, position: Int) {

        //이제 Realm이 MainThread에서 안된다고 해서 코루틴으로 비동기 처리
        CoroutineScope(Dispatchers.IO).launch {

            withContext(Main) {
                realm.beginTransaction()
                val oldItem = findAll().get(position)
                oldItem.folder = newItem.folder
                oldItem.sentence = newItem.sentence
                oldItem.source = newItem.source
                oldItem.description = newItem.description
                realm.commitTransaction()
            }
        }


    }


    fun deleteFromRealm(position: Int) {

        //이제 Realm이 MainThread에서 안된다고 해서 코루틴으로 비동기 처리
        CoroutineScope(Dispatchers.IO).launch {

            withContext(Main) {
                realm.beginTransaction()
                val realm_item = findAll().get(position)
                realm_item.deleteFromRealm()
                realm.commitTransaction()
            }
        }
    }

    fun getFolder(position: Int): String {
        return findAll().get(position).folder
    }

    fun getSentence(position: Int): String {
        return findAll().get(position).sentence
    }

    fun getSource(position: Int): String {
        return findAll().get(position).source
    }

    fun getDescription(position: Int): String {
        return findAll().get(position).description
    }

}