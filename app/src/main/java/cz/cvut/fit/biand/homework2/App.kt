package cz.cvut.fit.biand.homework2

import android.app.Application
import cz.cvut.fit.biand.homework2.core.di.coreModule
import cz.cvut.fit.biand.homework2.features.characters.di.characterModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(coreModule, characterModule)
        }
    }

}