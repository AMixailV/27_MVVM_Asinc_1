package ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.views.currentcolor

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mixail_akulov.a27_mvvm_async_1.R
import ru.mixail_akulov.a27_mvvm_async_1.foundation.model.ErrorResult
import ru.mixail_akulov.a27_mvvm_async_1.foundation.model.PendingResult
import ru.mixail_akulov.a27_mvvm_async_1.foundation.model.SuccessResult
import ru.mixail_akulov.a27_mvvm_async_1.foundation.model.takeSuccess
import ru.mixail_akulov.a27_mvvm_async_1.foundation.navigator.Navigator
import ru.mixail_akulov.a27_mvvm_async_1.foundation.uiactions.UiActions
import ru.mixail_akulov.a27_mvvm_async_1.foundation.views.BaseViewModel
import ru.mixail_akulov.a27_mvvm_async_1.foundation.views.LiveResult
import ru.mixail_akulov.a27_mvvm_async_1.foundation.views.MutableLiveResult
import ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.model.colors.ColorListener
import ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.model.colors.ColorsRepository
import ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.model.colors.NamedColor
import ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.views.changecolor.ChangeColorFragment
import java.lang.RuntimeException

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository
) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResult(it))
    }

    // --- пример результатов прослушивания через модельный слой

    init {
        // todo: имитация длительной загрузки контента для просмотра
        viewModelScope.launch {
            delay(2000)
            colorsRepository.addListener(colorListener)
        }
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- пример прослушивания результатов прямо с экрана

    override fun onResult(result: Any) {
        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    // ---

    fun changeColor() {
        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    fun tryAgain() {
        // todo: имитация долгой перезагрузки для view
        viewModelScope.launch {
            _currentColor.postValue(PendingResult())
            delay(2000)
            colorsRepository.addListener(colorListener)
        }
    }

}