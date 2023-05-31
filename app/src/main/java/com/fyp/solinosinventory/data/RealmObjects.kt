package com.fyp.solinosinventory.data


//import io.realm.RealmObject
//import io.realm.kotlin.types.BaseRealmObject
//import io.realm.kotlin.types.RealmObject

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Product(): RealmObject {
    @PrimaryKey
    var _id: String = ""
    var description: String = ""
    var quantity: Int = 0
    constructor(name: String, description: String) : this() {
        this._id = name
        this.description = description
    }

}
class SubProduct(): RealmObject {
    @PrimaryKey
    var _id: String = ""
    var quantity: Int = 0
    var parentName:String =""
    constructor(name: String, quantity: Int, parentName:String) : this() {
        this._id = name
        this.quantity = quantity
        this.parentName = parentName
    }
}
class Component(): RealmObject{
    @PrimaryKey
    var _id: String = ""
    var quantity: Int = 0
    var parentName:String =""
    //var EOQ: Int = 5
    constructor(name: String, quantity: Int, parentName:String) : this() {
        this._id = name
        this.quantity = quantity
        this.parentName = parentName
    }
}

class Vendor(): RealmObject{
    @PrimaryKey
    var _id: String = ""
    var vendorType: String = ""
    var parts: String = ""
    var contactNumber:String = ""
    var emailID = ""
    var avgLeadTime = ""
    var forProduct = ""
    var status:String = ""
}

//class Component(val name: String = "", val quantity: Int = 0, val parentName:String) : RealmObject()