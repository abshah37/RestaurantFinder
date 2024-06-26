package com.restaurantfinderapp.ui.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.restaurantfinderapp.R
import com.restaurantfinderapp.data.api.ApiService
import com.restaurantfinderapp.common.RequestComposablePermission
import com.restaurantfinderapp.data.repository.FindRestaurantRepository
import com.restaurantfinderapp.data.model.GetRestaurantsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.DecimalFormat

private val profileSectionTopPadding = 24.dp
private val screenHorizontalPadding = 18.dp
private val descriptionTopPadding = 18.dp
private val searchIconSize = 16.dp
private val searchIconStartPadding = 16.dp

val repository = FindRestaurantRepository(ApiService.create())

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel<HomeViewModel>(),
    onRestaurantItemClick: (GetRestaurantsResponse.Results) -> Unit = {}
) {
    var isPermissionDenied by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        if (ActivityCompat.checkSelfPermission(
                LocalContext.current,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            RequestComposablePermission(
                permission = Manifest.permission.ACCESS_COARSE_LOCATION,
                onDenied = {
                    LaunchedEffect(Unit) {
                        viewModel.getRestaurants(repository, "")
                    }
                },
                onGranted = {
                    FetchCurrentLocation(
                        viewModel = viewModel,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth(),
                        onRestaurantItemClick = onRestaurantItemClick,
                        isPermissionDenied = isPermissionDenied
                    )
                },
                onPermissionDenied = {
                    isPermissionDenied = true
                }
            )
        } else {
            FetchCurrentLocation(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                isPermissionDenied = isPermissionDenied,
                onRestaurantItemClick = onRestaurantItemClick
            )
        }

        if (isPermissionDenied) {
            FetchCurrentLocation(
                viewModel = viewModel,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                onRestaurantItemClick = onRestaurantItemClick,
                isPermissionDenied = isPermissionDenied
            )
        }

    }
}

@Composable
fun FetchCurrentLocation(
    modifier: Modifier,
    viewModel: HomeViewModel,
    isPermissionDenied: Boolean,
    onRestaurantItemClick: (GetRestaurantsResponse.Results) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var ll by remember {
        mutableStateOf<String?>(null)
    }

    if (isPermissionDenied) {
        LaunchedEffect(key1 = ll) {
            viewModel.getRestaurants(repository, null)
        }
    } else {
        LaunchedEffect(key1 = ll) {
            viewModel.getRestaurants(repository, ll)
        }
    }

    viewModel.restaurants.collectAsLazyPagingItems().let {
        HomeScreenContent(
            modifier,
            onRestaurantItemClick,
            it
        )
    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        LaunchedEffect(true) {
            scope.launch(Dispatchers.IO) {
                val priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
                val result = locationClient.getCurrentLocation(
                    priority,
                    CancellationTokenSource().token,
                ).await()

                result?.let { fetchedLocation ->
                    val df = DecimalFormat("#.##")
                    ll = "${df.format(fetchedLocation.latitude)},${df.format(fetchedLocation.longitude)}"
                }
            }
        }
    }

}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onRestaurantItemClick: (GetRestaurantsResponse.Results) -> Unit,
    items: LazyPagingItems<GetRestaurantsResponse.Results>
) {
    /*var grant by remember {
        mutableStateOf<Boolean?>(null)
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        grant = isGranted
        *//*if (isGranted) {

        } else {

        }*//*
    }

    when (PackageManager.PERMISSION_GRANTED) {
        ContextCompat.checkSelfPermission(
            LocalContext.current,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) -> {
            LaunchedEffect(key1 = true) {

            }
        }
        else -> {
            SideEffect {
                launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

    if (grant == true) {

    }*/
//    val items = viewModel.restaurants.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White),
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(horizontal = screenHorizontalPadding, vertical = 0.dp)
    ) {
        item(key = "profile") {
            ProfileSection(
                modifier = Modifier
                    .padding(top = profileSectionTopPadding)
            )
        }

        // create common component
        item(key = "subtitle") {
            Text(
                text = "Find the best restaurants\n near you...",
                color = Color.Black,
                style = TextStyle(
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .padding(top = descriptionTopPadding)
            )
        }

        item(key = "searchBox") {
            val textState = remember { mutableStateOf(TextFieldValue(text = "")) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .offset(y = (-10).dp)
                    .padding(top = 0.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(
                        border = BorderStroke(
                            0.25.dp,
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "search",
                    modifier = Modifier
                        .padding(start = searchIconStartPadding)
                        .size(searchIconSize)
                )

                // create common component
                BasicTextField(
                    value = textState.value,
                    onValueChange = {
                        textState.value = it
                    },
                    maxLines = 1,
                    decorationBox = { innerTextField ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            if (textState.value.text.isEmpty()) {
                                Text(
                                    text = "Search",
                                    color = Color.Black.copy(alpha = 0.2f),
                                    fontSize = 14.sp
                                )
                            }
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .padding(start = 10.dp)
                )

            }
        }

        items(items.itemCount) {
            RestaurantItem(
                modifier = Modifier
                    .offset(y = (-20).dp),
                data = items[it],
                onItemClick = onRestaurantItemClick
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
//    HomeScreenContent(modifier = Modifier, onRestaurantItemClick = {}, items = LazyPagingItems(
//        listOf()
//    ))
}

