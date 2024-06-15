package ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.views.changecolor

import ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.model.colors.NamedColor

/**
 * Представляет элемент списка для цвета; его можно выбрать или нет
 */
data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)