package com.xloger.lawrefbook

import android.app.Application
import android.content.Context
import com.xloger.lawrefbook.repository.BookRepository
import com.xloger.lawrefbook.ui.lawreader.LawReaderViewModel
import com.xloger.lawrefbook.ui.preview.PreviewViewModel
import com.xloger.lawrefbook.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
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
        single { BookRepository(get<Context>().assets) }
        viewModel { LawReaderViewModel(get()) }
        viewModel { PreviewViewModel(get()) }
        viewModel { SearchViewModel(get()) }

    }
}