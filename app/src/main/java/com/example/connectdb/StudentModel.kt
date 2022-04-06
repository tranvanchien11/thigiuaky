package com.example.connectdb
import java.util.*
data class StudentModel(
    var id : Int = getAutoId(),
    var name: String = "",
    var email: String = "",
    var contact: String = "",
    var address: String = ""
) {
    companion object{
        fun getAutoId(): Int {
            var random = Random();
            return random.nextInt(1)
        }
    }
}


