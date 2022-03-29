package com.xloger.lawrefbook.repository.favorites

import com.xloger.lawrefbook.repository.favorites.entity.FavoritesLawItem


/**
 * Created on 2022/3/29 11:56.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class FavoritesRepository(
    private val localDataSource: FavLocalDataSource
) {
    fun getAllFavItem() = localDataSource.getAllFavItem()

    fun addFavItem(item: FavoritesLawItem) = localDataSource.addFavItem(item)

    fun removeFavItem(item: FavoritesLawItem) = localDataSource.removeFavItem(item)
}