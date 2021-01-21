package pe.com.starcode.testnapoleonsystem.base.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Post {

    @PrimaryKey
    var id: Int =0
    var body:String =""
    var title :String =""
    var userId:Int =0
    var state:Boolean =false
    var favorite: Boolean =false
    var nameUser :String =""
    var emailUser :String =""
}