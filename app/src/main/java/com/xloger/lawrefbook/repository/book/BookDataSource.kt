package com.xloger.lawrefbook.repository.book

import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.repository.book.entity.menu.Doc
import com.xloger.lawrefbook.repository.book.entity.menu.LawRefContainer

/**
 * Created on 2022/3/26 20:59.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
interface BookDataSource {

    fun getLawRefContainer() : LawRefContainer

    fun getLaw(doc: Doc): Law

    fun getOriginText(doc: Doc): String
}