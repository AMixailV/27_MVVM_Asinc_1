package ru.mixail_akulov.a27_mvvm_async_1.foundation.views

import ru.mixail_akulov.a27_mvvm_async_1.foundation.ActivityScopeViewModel

/**
 * Реализуйте этот интерфейс в activity.
 */
interface FragmentsHolder {

    /**
     * Вызывается, когда представления действий должны быть перерисованы.
     */
    fun notifyScreenUpdates()

    /**
     * Получите текущие реализации зависимостей из области активности виртуальной машины.
     */
    fun getActivityScopeViewModel(): ActivityScopeViewModel

}