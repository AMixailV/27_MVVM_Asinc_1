package ru.mixail_akulov.a27_mvvm_async_1.foundation

import ru.mixail_akulov.a27_mvvm_async_1.foundation.model.Repository

/**
 * Implement this interface in your Application class.
 * Do not forget to add the application class into the AndroidManifest.xml file.
 */
interface BaseApplication {

    /**
     * The list of repositories that can be added to the fragment view-model constructors.
     */
    val repositories: List<Repository>

}