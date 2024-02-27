package com.venkatesh.sneakership.ui.screen.sneakerdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venkatesh.sneakership.data.Repository
import com.venkatesh.sneakership.data.model.OrderItem
import com.venkatesh.sneakership.data.model.SneakerItem
import com.venkatesh.sneakership.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(val repository:Repository):ViewModel() {
    private val _uiState: MutableStateFlow<Resource<OrderItem>> =
        MutableStateFlow(Resource.Loading)
    val uiState: StateFlow<Resource<OrderItem>>
        get() = _uiState

    fun getSneakersById(sneakersId: String) {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            repository.getOrderSneakerById(sneakersId).collect{
                _uiState.value = Resource.Success(it)
            }
        }
    }

    fun addToCart(sneakers: SneakerItem, count: Int) {
        viewModelScope.launch {
            repository.updateOrderSneaker(sneakers.id, count)
        }
    }
}