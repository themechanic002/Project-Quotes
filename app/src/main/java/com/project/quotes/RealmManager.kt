package com.project.quotes

import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RealmManager(val realm: Realm) {


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
    fun findFolders(): ArrayList<String>{

        val items: List<Item> = realm.where(Item::class.java).findAll()
        val folders = ArrayList<String>()
        for(i in 0 until items.size){
            if(!folders.contains(items[i].folder))
                folders.add(items[i].folder)
        }

        return folders
    }

}