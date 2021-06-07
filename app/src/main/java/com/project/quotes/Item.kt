package com.project.quotes

import io.realm.RealmObject

open class Item(
        var folder: String = "folder",
        var sentence: String = "sentence",
        var source: String = "source",
        var description: String = "description"

) : RealmObject() {}