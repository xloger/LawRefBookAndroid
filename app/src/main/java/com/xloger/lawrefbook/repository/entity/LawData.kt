package com.xloger.lawrefbook.repository.entity


import androidx.annotation.Keep

class LawData : ArrayList<LawData.LawDataItem>(){
    @Keep
    data class LawDataItem(
        val category: String,
        val folder: String,
        val id: String,
        val laws: List<Law>,
        val links: List<String>?
    ) {
        @Keep
        data class Law(
            val id: String,
            val level: String,
            val name: String,
            val filename: String?,
            val links: List<String>?
        )
    }
}