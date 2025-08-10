package com.chslcompany.sharedNotes.domain.model

data class SharedNote(
    val id : String,
    val email : String,
    val title : String,
    val content : String,
    val imageUrl : String,
    val shared : Boolean,
    val timeStamp : String = System.currentTimeMillis().toString()
){
    constructor():this("","","","","",false)
}
