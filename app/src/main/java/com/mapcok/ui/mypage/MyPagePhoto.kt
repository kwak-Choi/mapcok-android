package com.mapcok.ui.mypage

data class MyPagePhoto(var location : String, var date : Long, var src: String) {
    var num = -1

    constructor(_num:Int, location: String, date: Long, src: String): this(location,date,src){
        num = _num
        // 내 이미지에 맞게 변수 수정
    }
}
