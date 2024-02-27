package com.venkatesh.sneakership.ui.screen.dashboard

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.venkatesh.sneakership.data.model.OrderItem
import com.venkatesh.sneakership.ui.components.SneakersItem
import com.venkatesh.sneakership.ui.theme.Orange80
import com.venkatesh.sneakership.utils.Resource

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel()

    dashboardViewModel.state.collectAsState(initial = Resource.Loading).value.let { uiState ->
        when (uiState) {
            is Resource.Loading -> {
                dashboardViewModel.getAllSneakers()
            }
            is Resource.Success -> {
                HomeContent(
                    orderSneakers = uiState.data,
                    modifier = modifier,
                    navigateToDetail = navigateToDetail,
                    viewModel = dashboardViewModel
                )
            }
            is Resource.Error -> {}
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    orderSneakers: List<OrderItem>,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val query by viewModel.query

    Column(modifier = Modifier) {
        SearchBar(
            query = query,
            onQueryChange = viewModel::search,
            modifier = Modifier.background(Orange80)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier.testTag("SneakersList")
        ) {
            items(orderSneakers, key = { it.sneakers.title}) { data ->
                SneakersItem(
                    image = data.sneakers.image,
                    title = data.sneakers.title,
                    price = data.sneakers.price,
                    rating= data.sneakers.rating,
                    modifier = Modifier
                        .clickable {
                            navigateToDetail(data.sneakers.id)
                        }
                        .animateItemPlacement(tween(durationMillis = 100))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = MaterialTheme.colorScheme.surface,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        placeholder = {
            Text("Search for sneakers")
        },
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(RoundedCornerShape(16.dp))
    )
}