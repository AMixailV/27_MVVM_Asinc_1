package ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.views.changecolor

import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mixail_akulov.a27_mvvm_async_1.R
import ru.mixail_akulov.a27_mvvm_async_1.foundation.model.PendingResult
import ru.mixail_akulov.a27_mvvm_async_1.foundation.model.SuccessResult
import ru.mixail_akulov.a27_mvvm_async_1.foundation.navigator.Navigator
import ru.mixail_akulov.a27_mvvm_async_1.foundation.uiactions.UiActions
import ru.mixail_akulov.a27_mvvm_async_1.foundation.views.BaseViewModel
import ru.mixail_akulov.a27_mvvm_async_1.foundation.views.LiveResult
import ru.mixail_akulov.a27_mvvm_async_1.foundation.views.MediatorLiveResult
import ru.mixail_akulov.a27_mvvm_async_1.foundation.views.MutableLiveResult
import ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.model.colors.ColorsRepository
import ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.model.colors.NamedColor
import ru.mixail_akulov.a27_mvvm_async_1.simplemvvm.views.changecolor.ChangeColorFragment.Screen

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors = MutableLiveResult<List<NamedColor>>(PendingResult())
    private val _currentColorId = savedStateHandle.getLiveData("currentColorId", screen.currentColorId)
    private val _saveInProgress = MutableLiveData(false)

    // основной пункт назначения (содержит объединенные значения from _availableColors & _currentColorId)
    private val _viewState = MediatorLiveResult<ViewState>()
    val viewState: LiveResult<ViewState> = _viewState

    // побочное назначение, также тот же результат может быть достигнут с использованием Transformations.map() function.
    val screenTitle: LiveData<String> = Transformations.map(viewState) { result ->
        if (result is SuccessResult) {
            val currentColor = result.data.colorsList.first { it.selected }
            uiActions.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
        } else {
            uiActions.getString(R.string.change_color_screen_title_simple)
        }
    }

    private var mockError = true

    init {
        viewModelScope.launch {
            delay(2000)
            _availableColors.value = SuccessResult(colorsRepository.getAvailableColors())
        }

        // initializing MediatorLiveData
        _viewState.addSource(_availableColors) { mergeSources() }
        _viewState.addSource(_currentColorId) { mergeSources() }
        _viewState.addSource(_saveInProgress) { mergeSources() }
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if (_saveInProgress.value == true) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() {
        // todo: mocking long-running save operation for view
        viewModelScope.launch {
            _saveInProgress.postValue(true)
            delay(1000)
            if (mockError) {
                _saveInProgress.postValue(false)
                uiActions.toast(uiActions.getString(R.string.error_happened))
                mockError = false
            } else {
                val currentColorId = _currentColorId.value ?: return@launch
                val currentColor = colorsRepository.getById(currentColorId)
                colorsRepository.currentColor = currentColor
                navigator.goBack(result = currentColor)
            }
        }
    }

    fun onCancelPressed() {
        navigator.goBack()
    }

    fun tryAgain() {
        // todo: mocking long-running reload operation for view
        viewModelScope.launch {
            _availableColors.postValue(PendingResult())
            delay(2000)
            _availableColors.postValue(SuccessResult(colorsRepository.getAvailableColors()))
        }
    }

    /**
     * [MediatorLiveData] может прослушивать другие экземпляры LiveData (даже более 1) и комбинировать их значения.
     *
     * Здесь мы слушаем список доступных цветов ([_availableColors] live-data) + current color id
     * ([_currentColorId] live-data), затем мы используем оба этих значения для создания списка
     * [NamedColorListItem], это список, который будет отображаться в RecyclerView.
     */
    private fun mergeSources() {
        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return
        val saveInProgress = _saveInProgress.value ?: return

        // map Result<List<NamedColor>> to Result<ViewState>
        _viewState.value = colors.map { colorsList ->
            ViewState(
                // map List<NamedColor> to List<NamedColorListItem>
                colorsList = colorsList.map { NamedColorListItem(it, currentColorId == it.id) },
                showSaveButton = !saveInProgress,
                showCancelButton = !saveInProgress,
                showSaveProgressBar = saveInProgress
            )
        }
    }

    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean
    )
}