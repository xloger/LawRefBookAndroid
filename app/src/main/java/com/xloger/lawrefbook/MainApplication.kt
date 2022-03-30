package com.xloger.lawrefbook

import android.app.Application
import androidx.room.Room
import com.xloger.lawrefbook.repository.book.AssetsDataSource
import com.xloger.lawrefbook.repository.book.BookDataSource
import com.xloger.lawrefbook.repository.book.BookRepository
import com.xloger.lawrefbook.repository.book.parser.LawRegexHelper
import com.xloger.lawrefbook.repository.favorites.FavLocalDataSource
import com.xloger.lawrefbook.repository.favorites.FavoritesRepository
import com.xloger.lawrefbook.repository.favorites.database.AppDatabase
import com.xloger.lawrefbook.ui.favorites.FavoritesViewModel
import com.xloger.lawrefbook.ui.lawreader.LawReaderViewModel
import com.xloger.lawrefbook.ui.preview.PreviewViewModel
import com.xloger.lawrefbook.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Created on 2022/3/26 19:46.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    private val appModule = module {
        single { LawRegexHelper() }
        single { AssetsDataSource(get(), get()) } bind BookDataSource::class
        single { BookRepository(get()) }
        single { Room.databaseBuilder(get(), AppDatabase::class.java, "app-database").build() }
        single { FavLocalDataSource(get()) } bind FavLocalDataSource::class
        single { FavoritesRepository(get()) }
        viewModel { LawReaderViewModel(get(), get()) }
        viewModel { PreviewViewModel(get()) }
        viewModel { SearchViewModel(get(), get()) }
        viewModel { FavoritesViewModel(get(), get(), get()) }

    }
}