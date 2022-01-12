package com.yjpapp.stockportfolio.di

import com.yjpapp.stockportfolio.function.incomenote.IncomeNoteViewModel
import com.yjpapp.stockportfolio.function.login.LoginViewModel
import com.yjpapp.stockportfolio.repository.MemoListRepository
import com.yjpapp.stockportfolio.function.memo.MemoListViewModel
import com.yjpapp.stockportfolio.repository.MemoReadWriteRepository
import com.yjpapp.stockportfolio.function.memo.detail.MemoReadWriteViewModel
import com.yjpapp.stockportfolio.repository.MyRepository
import com.yjpapp.stockportfolio.function.my.MyViewModel
import com.yjpapp.stockportfolio.function.mystock.MyStockViewModel
import com.yjpapp.stockportfolio.function.splash.SplashViewModel
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.localdb.room.MyRoomDatabase
import com.yjpapp.stockportfolio.repository.IncomeNoteRepository
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
    single { UserRepository(PreferenceController.getInstance(androidContext())) }
    viewModel {
        LoginViewModel(
            androidApplication(),
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
    viewModel {
        MyViewModel(
            UserRepository(PreferenceController.getInstance(androidContext())),
            MyRepository(PreferenceController.getInstance(androidContext()))
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
val memoReadWriteViewModel = module {
    single { MemoReadWriteRepository(MyRoomDatabase.getInstance(androidContext()).memoListDao()) }
    viewModel {
        MemoReadWriteViewModel(get())
    }
}
val memoListViewModel = module {
    single { MemoListRepository(
        MyRoomDatabase.getInstance(androidContext()).memoListDao(),
        PreferenceController.getInstance(androidContext())
    )}
    viewModel {
        MemoListViewModel(get())
    }
}
val splashViewModel = module {
    viewModel {
        SplashViewModel(
            MyRepository(PreferenceController.getInstance(androidContext()))
        )
    }
}
/**
 * Common
 */
val preferenceController = module {
    single { PreferenceController.getInstance(androidContext()) }
}
