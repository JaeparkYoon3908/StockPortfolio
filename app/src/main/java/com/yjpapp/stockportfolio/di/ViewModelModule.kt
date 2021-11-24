package com.yjpapp.stockportfolio.di

import com.yjpapp.stockportfolio.repository.IncomeNoteRepository
import com.yjpapp.stockportfolio.function.incomenote.IncomeNoteViewModel
import com.yjpapp.stockportfolio.function.login.LoginViewModel
import com.yjpapp.stockportfolio.function.my.MyViewModel
import com.yjpapp.stockportfolio.function.mystock.MyStockViewModel
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.localdb.room.MyRoomDatabase
import com.yjpapp.stockportfolio.repository.MyRepository
import com.yjpapp.stockportfolio.repository.MyStockRepository
import com.yjpapp.stockportfolio.repository.UserRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * ViewModel
 */
val loginViewModel = module {
    single { UserRepository() }
    viewModel {
        LoginViewModel(
            androidApplication(),
            PreferenceController.getInstance(androidContext()),
            get()
        )
    }
}
val myStockViewModel = module {
    single { MyRoomDatabase.getInstance(androidContext()).myStockDao() }
    single { MyStockRepository(get()) }
    viewModel {
        MyStockViewModel(
            androidApplication(),
            get()
        )
    }
}
val myViewModel = module {
    single { MyRepository() }
    viewModel {
        MyViewModel(
            androidApplication(),
            PreferenceController.getInstance(androidContext()),
            get()
        )
    }
}
val incomeNoteViewModel = module {
    single { IncomeNoteRepository() }
    viewModel {
        IncomeNoteViewModel(
            get()
        )
    }
}
/**
 * Common
 */
val preferenceController = module {
    single { PreferenceController.getInstance(androidContext()) }
}
