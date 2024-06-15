package ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.model.colors

import ru.mixail_akulov.a27_mvvm_async_1.foundation.model.Repository

typealias ColorListener = (NamedColor) -> Unit

/**
 * Пример интерфейса репозитория.
 *
 * Предоставляет доступ к доступным цветам и текущему выбранному цвету.
 */
interface ColorsRepository : Repository {

    var currentColor: NamedColor

    /**
     * Получить список всех доступных цветов, которые может выбрать пользователь.
     */
    fun getAvailableColors(): List<NamedColor>

    /**
     * Get the color content by its ID
     */
    fun getById(id: Long): NamedColor

    /**
     * Слушайте текущие изменения цвета.
     * Слушатель запускается немедленно с текущим значением при вызове этого метода.
     */
    fun addListener(listener: ColorListener)

    /**
     * Перестаньте слушать текущие изменения цвета
     */
    fun removeListener(listener: ColorListener)
}