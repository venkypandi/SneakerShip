package com.venkatesh.sneakership.ui.screen.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.venkatesh.sneakership.data.model.CartState
import com.venkatesh.sneakership.ui.screen.sneakerdetails.OrderButton
import com.venkatesh.sneakership.utils.Resource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.venkatesh.sneakership.R
import com.venkatesh.sneakership.ui.components.ProductCounter
import com.venkatesh.sneakership.ui.theme.SneakerShipTheme

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onOrderButtonClicked: (String) -> Unit,
) {
    viewModel.uiState.collectAsState(initial = Resource.Loading).value.let { uiState ->
        when (uiState) {
            is Resource.Loading -> {
                viewModel.getAddedOrderSneakers()
            }
            is Resource.Success -> {
                CartContent(
                    uiState.data,
                    onProductCountChanged = { sneakersId, count ->
                        viewModel.updateOrderReward(sneakersId, count)
                    },
                    onOrderButtonClicked = onOrderButtonClicked
                )
            }
            is Resource.Error -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    state: CartState,
    onProductCountChanged: (id: String, count: Int) -> Unit,
    onOrderButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Checkout",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        content = {innerPadding ->
            Column(
                modifier = modifier.fillMaxSize()
            ) {

                LazyColumn(
                    modifier= Modifier
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.orderSneakers, key = {it.sneakers.id}) { item ->
                        CartItem(
                            sneakerId = item.sneakers.id,
                            image = item.sneakers.image,
                            title = item.sneakers.title,
                            totalPrice = item.sneakers.price * item.count,
                            count = item.count,
                            onProductCountChanged = onProductCountChanged,
                        )
                        Divider()
                    }
                }

                OrderButton(
                    text = "Checkout ₹${state.totalPrice}",
                    enabled = state.orderSneakers.isNotEmpty(),
                    onClick = {

                    },
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    )
}

@Composable
fun CartItem(
    sneakerId: String,
    image: Int,
    title: String,
    totalPrice: Int,
    count: Int,
    onProductCountChanged: (id: String, count: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(MaterialTheme.shapes.small)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1.0f)
        ) {
            Text(
                text = title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            Text(
                text = "₹$totalPrice",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
            )
        }
        ProductCounter(
            orderId = sneakerId,
            orderCount = count,
            onProductIncreased = { onProductCountChanged(sneakerId, count + 1) },
            onProductDecreased = { onProductCountChanged(sneakerId, count - 1) },
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CartItemPreview() {
    SneakerShipTheme {
        CartItem(
            "4", R.drawable.sneakers_1, "Air Jordan Low SE", 125, 0,
            onProductCountChanged = { _, _ -> },
        )
    }
}