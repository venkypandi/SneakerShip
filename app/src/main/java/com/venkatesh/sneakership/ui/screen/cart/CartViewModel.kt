package com.venkatesh.sneakership.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.venkatesh.sneakership.data.Repository
import com.venkatesh.sneakership.data.model.CartState
import com.venkatesh.sneakership.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(val repository: Repository):ViewModel() {
    private val _uiState: MutableStateFlow<Resource<CartState>> = MutableStateFlow(Resource.Loading)
    val uiState: StateFlow<Resource<CartState>>
        get() = _uiState

    fun getAddedOrderSneakers() {
        viewModelScope.launch {
            _uiState.value = Resource.Loading
            repository.getAddedOrderSneakers()
                .collect { orderSneakers ->
                    val totalPrice =
                        orderSneakers.sumOf { it.sneakers.price * it.count }
                    _uiState.value = Resource.Success(CartState(orderSneakers, totalPrice))
                }
        }
    }

    fun updateOrderReward(sneakersId: String, count: Int) {
        viewModelScope.launch {
            repository.updateOrderSneaker(sneakersId, count)
                .collect { isUpdated ->
                    if (isUpdated) {
                        getAddedOrderSneakers()
                    }
                }
        }
    }
}