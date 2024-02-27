package com.venkatesh.sneakership.data

import com.venkatesh.sneakership.data.model.OrderItem
import com.venkatesh.sneakership.data.remote.SneakerDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(private val sneakerDataSource: SneakerDataSource) {

    fun getAllSneakers(): Flow<List<OrderItem>>  = sneakerDataSource.getAllSneakers()

    fun getOrderSneakerById(sneakersId: String): Flow<OrderItem> = sneakerDataSource.getSneakerById(sneakersId)

    fun updateOrderSneaker(sneakersId: String, count: Int): Flow<Boolean>  = sneakerDataSource.updateOrderSneaker(sneakersId,count)

    fun getAddedOrderSneakers(): Flow<List<OrderItem>> = sneakerDataSource.getAddedOrderSneakers()

    fun searchSneakers(query: String): Flow<List<OrderItem>> = sneakerDataSource.searchSneakers(query)

}