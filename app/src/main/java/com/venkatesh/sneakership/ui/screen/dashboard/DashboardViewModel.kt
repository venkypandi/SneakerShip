package com.venkatesh.sneakership.ui.screen.dashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venkatesh.sneakership.data.Repository
import com.venkatesh.sneakership.data.model.OrderItem
import com.venkatesh.sneakership.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor (val repository:Repository):ViewModel(){
    private val _state: MutableStateFlow<Resource<List<OrderItem>>> = MutableStateFlow(Resource.Loading)
    val state: StateFlow<Resource<List<OrderItem>>>
        get() = _state

    fun getAllSneakers() {
        viewModelScope.launch {
            repository.getAllSneakers()
                .catch {
                    _state.value = Resource.Error(it.message.toString())
                }
                .collect { orderSneakers ->
                    _state.value = Resource.Success(orderSneakers)
                }
        }
    }

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) {
        viewModelScope.launch{
            _query.value = newQuery
            repository.searchSneakers(_query.value).collect{
                _state.value = Resource.Success(it)
            }
        }
    }
}