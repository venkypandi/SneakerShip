package com.venkatesh.sneakership.ui.screen.sneakerdetails

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.venkatesh.sneakership.utils.Resource
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.sp
import com.venkatesh.sneakership.R
import com.venkatesh.sneakership.ui.components.ProductCounter
import com.venkatesh.sneakership.ui.components.StarRatingBar
import com.venkatesh.sneakership.ui.theme.Orange80
import com.venkatesh.sneakership.ui.theme.Purple80
import com.venkatesh.sneakership.ui.theme.SneakerShipTheme


@Composable
fun DetailScreen(
    sneakersId: String,
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit
) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    viewModel.uiState.collectAsState(initial = Resource.Loading).value.let { state ->
        when (state) {
            is Resource.Loading -> {
                viewModel.getSneakersById(sneakersId)
            }
            is Resource.Success -> {
                val data = state.data
                DetailContent(
                    data.sneakers.image,
                    data.sneakers.title,
                    data.sneakers.price,
                    data.sneakers.description,
                    data.sneakers.rating,
                    data.count,
                    onBackClick = navigateBack,
                    onAddToCart = { count ->
                        viewModel.addToCart(data.sneakers, count)
                        navigateToCart()
                    }
                )
            }
            is Resource.Error -> {}
        }
    }
}

@Composable
fun DetailContent(
    @DrawableRes image: Int,
    title: String,
    price: Int,
    description : String,
    rating:Float,
    count: Int,
    onBackClick: () -> Unit,
    onAddToCart: (count: Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    var totalPoint by rememberSaveable { mutableStateOf(0) }
    var orderCount by rememberSaveable { mutableStateOf(count) }

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Button",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onBackClick() }
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(16.dp),
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
                StarRatingBar(rating)
                Text(
                    text = "₹$price",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 35.sp,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                )
                Text(
                    text = "Color",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp, 20.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Orange80)
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(20.dp, 20.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Purple80)
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(20.dp, 20.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Blue)
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(20.dp, 20.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Green)
                    )
                }
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color.LightGray))
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductCounter(
                "1",
                orderCount,
                onProductIncreased = { orderCount++ },
                onProductDecreased = { if (orderCount > 0) orderCount-- },

            )
            totalPoint = price * orderCount
            OrderButton(
                text = "Add to cart : $orderCount",
                enabled = orderCount > 0,
                onClick = {
                    onAddToCart(orderCount)
                },
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DetailContentPreview() {
    SneakerShipTheme {
        DetailContent(
            R.drawable.sneakers_1,
            "Air Jordan Low SE",
            125,
            "Get into some summery fun in your new fave AJ1s. Made with a combination of suede and canvas, this pair gives you the comfort you know and love with a seasonal update.",
            1.0f,
            1,
            onBackClick = {},
            onAddToCart = {}
        )
    }
}

@Composable
fun OrderButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(Orange80),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = "Order Button"
            }
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun OrderButtonPreview() {
    SneakerShipTheme {
        OrderButton(
            text = "Order",
            onClick = {}
        )
    }
}