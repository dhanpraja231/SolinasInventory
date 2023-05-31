package com.fyp.solinosinventory.data


import android.util.Log
import androidx.annotation.RestrictTo.Scope
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import com.fyp.solinosinventory.screens.PARTITION
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

const val APP_ID = "solinasinventory-zgrvd"

object RealmDAOInstance{

    private val app = App.create(APP_ID)

    private lateinit var realm: Realm

    init {
        runBlocking{
            configureTheRealm()

        }
    }

    suspend fun configureTheRealm() {
        val credentials: Credentials = Credentials.anonymous()
        app.login(credentials)
        val user = app.currentUser
        Log.d("user",user.toString())
        if (user != null) {
            val config = SyncConfiguration.Builder(
                user, partitionValue = PARTITION,
                setOf(Product::class, SubProduct::class, Component::class, Vendor::class)
            ).build()
            realm = Realm.open(config)
        }

    }


    suspend fun getAllProducts(): List<Product> {
        return realm.query<Product>().find().toList()
    }

    suspend fun getAllSubProducts(parent: String): List<SubProduct>{
        return realm.query<SubProduct>(query = "parentName = $0",parent).find().toList()
    }

    suspend fun getAllComponents(parent: String): List<Component>{
        return realm.query<Component>(query = "parentName = $0",parent).find().toList()
    }

    suspend fun getAllVendors(): List<Vendor> {
        return realm.query<Vendor>().find().toList()
    }

    suspend fun addProduct(name: String, description: String){ //verify email and add log
        realm.write {
            this.copyToRealm(Product().apply {
                _id = name
                this.description = description
            })
        }
    }

    suspend fun addSubProduct(name: String, quantity: Int, parentName: String){ //verify email and add log

    realm.write {
        this.copyToRealm(SubProduct().apply {
            _id = name
            this.quantity = quantity
            this.parentName = parentName
        })
    }
    }
//
//    // ! components are stored in the db as
    suspend fun addComponent(name: String, quantity: Int, parentName: String){ //verify email and add log
        realm.write {
            this.copyToRealm(Component().apply {
                _id = name
                this.quantity = quantity
                this.parentName = parentName
            })
        }
    }

    suspend fun addVendor(_id: String, vendorType:String,
                          parts: String, contactNumber:String,
                          emailID:String, avgLeadTime:String,
                          forProduct: String,status:String
    ){
        realm.write {
            this.copyToRealm(Vendor().apply {
                this._id = _id
                this.vendorType = vendorType
                this.avgLeadTime = avgLeadTime
                this.contactNumber = contactNumber
                this.emailID = emailID
                this.forProduct = forProduct
                this.parts = parts
                this.status = status
            })
        }
    }

    suspend fun deleteProduct(name:String){
        realm.write {
            val components = this.query<Component>(query = "parentName ==$0",name).find()
            components.forEach {
                GlobalScope.launch {
                    deleteSubProduct(it._id)
                }
            }
            val product: Product =
                this.query<Product>("_id == $0", name).find().first()
            delete(product)
        }
    }

    suspend fun deleteSubProduct(name: String){


        realm.write {

            val subProduct: SubProduct =
                this.query<SubProduct>("_id == $0", name).find().first()
            delete(subProduct)

        }

    }

    suspend fun deleteComponent(name: String){
        realm.write {
            val component: Component =
                this.query<Component>("_id == $0", name).find().first()
            delete(component)
        }
    }

    suspend fun deleteVendor(name: String){
        realm.write {
            val vendor: Vendor =
                this.query<Vendor>("_id == $0",name).find().first()
            delete(vendor)
        }
    }

    suspend fun updateStatus(name:String, status: String){
        realm.write {
            val vendor: Vendor? =
                this.query<Vendor>("_id == $0", name).first().find()
            if (vendor != null) {
                vendor.status = status
            }
        }
    }

    suspend fun incrementSubProduct(name: String){

    realm.write {
        val subProduct: SubProduct? =
            this.query<SubProduct>("_id == $0", name).first().find()
        if (subProduct != null) {
            subProduct.quantity = subProduct.quantity+1
        }
    }
    }

    suspend fun decrementSubProduct(name: String){

        realm.write {
        val subProduct: SubProduct? =
            this.query<SubProduct>("_id == $0", name).first().find()
        if (subProduct != null) {
            subProduct.quantity = subProduct.quantity-1
        }
        }
    }

    suspend fun incrementComponent(name: String, parentName: String){

    realm.write {
        val component: Component? =
            this.query<Component>("_id == $0", name).first().find()
        if (component != null) {
            component.quantity = component.quantity+1
        }
    }
    }

    suspend fun decrementComponent(name: String, parentName: String){

        realm.write {
            val component: Component? =
                this.query<Component>("_id == $0", name).first().find()
            if (component != null) {
                component.quantity = component.quantity-1
            }
        }
    }

    //add vendor


    //get all vendors
    //delete vendor
    // update vendor status


}