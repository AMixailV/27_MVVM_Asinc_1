package ru.mixail_akulov.a27_mvvm_async_1

import android.app.Application
import ru.mixail_akulov.a27_mvvm_async_1.foundation.BaseApplication
import ru.mixail_akulov.a27_mvvm_async_1.foundation.model.Repository
import ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.model.colors.InMemoryColorsRepository

/**
 * Здесь мы храним экземпляры классов слоя модели.
 */
class App : Application(), BaseApplication {

    /**
     * Размещайте здесь свои репозитории, сейчас у нас всего 1 репозиторий
     */
    override val repositories: List<Repository> = listOf<Repository>(
        InMemoryColorsRepository()
    )

}