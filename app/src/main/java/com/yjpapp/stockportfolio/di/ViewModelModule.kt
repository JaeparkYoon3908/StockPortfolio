//package com.yjpapp.stockportfolio.di
//
//import com.yjpapp.stockportfolio.function.login.LoginViewModel
//import com.yjpapp.stockportfolio.repository.MemoRepository
//import com.yjpapp.stockportfolio.function.memo.MemoListViewModel
//import com.yjpapp.stockportfolio.function.memo.detail.MemoReadWriteViewModel
//import com.yjpapp.stockportfolio.repository.MyRepository
//import com.yjpapp.stockportfolio.function.my.MyViewModel
//import com.yjpapp.stockportfolio.function.mystock.MyStockViewModel
//import com.yjpapp.stockportfolio.function.splash.SplashViewModel
//import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
//import com.yjpapp.stockportfolio.localdb.room.MyRoomDatabase
//import com.yjpapp.stockportfolio.repository.MyStockRepository
//import com.yjpapp.stockportfolio.repository.UserRepository
//import org.koin.android.ext.koin.androidContext
//import org.koin.android.viewmodel.dsl.viewModel
//import org.koin.dsl.module
//
///**
// * ViewModel
// */
//val loginViewModel = module {
//    single { UserRepository(PreferenceController.getInstance(androidContext())) }
//    viewModel {
//        LoginViewModel(
//            get()
//        )
//    }
//}
//val myStockViewModel = module {
//    single { MyRoomDatabase.getInstance(androidContext()).myStockDao() }
//    single { MyStockRepository(get()) }
//    viewModel {
//        MyStockViewModel(
//            get()
//        )
//    }
//}
//val myViewModel = module {
//    viewModel {
//        MyViewModel(
//            UserRepository(PreferenceController.getInstance(androidContext())),
//            MyRepository(PreferenceController.getInstance(androidContext()))
//        )
//    }
//}
//
//val memoListViewModel = module {
//    single { MemoRepository(
//        MyRoomDatabase.getInstance(androidContext()).memoListDao(),
//        PreferenceController.getInstance(androidContext())
//    )}
//    viewModel {
//        MemoListViewModel(get())
//    }
//}
//val splashViewModel = module {
//    viewModel {
//        SplashViewModel(
//            MyRepository(PreferenceController.getInstance(androidContext()))
//        )
//    }
//}
///**
// * Common
// */
//val preferenceController = module {
//    single { PreferenceController.getInstance(androidContext()) }
//}
