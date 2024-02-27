package com.venkatesh.sneakership.data.remote

import com.venkatesh.sneakership.data.model.OrderItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SneakerDataSource {

    private val orderSneakers = mutableListOf<OrderItem>()

    init {
        if (orderSneakers.isEmpty()){
            SneakerData.dummySneakers.forEach{
                orderSneakers.add(OrderItem(it,0))
            }
        }
    }

     fun getAllSneakers(): Flow<List<OrderItem>> {
        return flowOf(orderSneakers)
    }

    fun getSneakerById(sneakersId: String): Flow<OrderItem> {
        return flowOf(
            orderSneakers.first{
                it.sneakers.id == sneakersId
            }
        )
    }

    fun updateOrderSneaker(sneakersId: String, newCountValue: Int): Flow<Boolean> {
        val index = orderSneakers.indexOfFirst { it.sneakers.id == sneakersId }
        val result = if(index >= 0) {
            val orderSneaker = orderSneakers[index]
            orderSneakers[index] =
                orderSneaker.copy(sneakers = orderSneaker.sneakers, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getAddedOrderSneakers(): Flow<List<OrderItem>> {
        return getAllSneakers()
            .map { orderSneakers ->
                orderSneakers.filter { orderSneaker ->
                    orderSneaker.count != 0
                }
            }
    }

    fun searchSneakers(query: String): Flow<List<OrderItem>>{
        return flow {
            emit(orderSneakers.filter {
                it.sneakers.title.contains(query, ignoreCase = true)
            })
        }
    }


}