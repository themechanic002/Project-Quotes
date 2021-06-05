package com.project.quotes

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
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

            withContext(Main){
                realm.beginTransaction()
                val realm_item = realm.createObject(Item::class.java)
                realm_item.folder = item.folder
                realm_item.sentence = item.sentence
                realm_item.source = item.source
                realm.commitTransaction()
            }

        }
    }

}