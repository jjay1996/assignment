package a13341.egco428.com.a13341

import a13341.egco428.com.a13341.Model.Cookie

object CookiePovider {
    private val data = ArrayList<CookieMessage>()
    fun getData(): ArrayList<CookieMessage>{
        return data
    }

    fun addMeesage(message:String,type: String,date:String){
        data.add(CookieMessage(message,type,date))
    }
}