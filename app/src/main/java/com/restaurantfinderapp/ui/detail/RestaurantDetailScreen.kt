package com.restaurantfinderapp.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.restaurantfinderapp.R
import com.restaurantfinderapp.data.api.ApiService
import com.restaurantfinderapp.common.RatingBar
import com.restaurantfinderapp.data.repository.FindRestaurantRepository
import com.restaurantfinderapp.data.model.RestaurantDetailResponse
import androidx.lifecycle.viewmodel.compose.viewModel
import com.restaurantfinderapp.ui.home.df

private val imageHeight = 360.dp
private val restaurantInformationHorizontalPadding = 36.dp
private val restaurantInformationTopPadding = 290.dp
private val restaurantTitleEndPadding = 16.dp
private val restaurantInformationTopInnerPadding = 30.dp
private val favoriteIconSize = 48.dp
private val backIconSize = 48.dp
private val backIconPadding = 8.dp
private val favoriteIconPadding = 10.dp

@Composable
fun RestaurantDetailScreen(
    viewModel: RestaurantDetailViewModel = viewModel<RestaurantDetailViewModel>(),
    fsqId: String,
    onBackClick: () -> Unit,
) {

    LaunchedEffect(key1 = true) {
        viewModel.getRestaurantDetail(
            repository = FindRestaurantRepository(ApiService.create()),
            fsqId = fsqId
        )
    }

    val showProgress = viewModel.showProgress.collectAsState().value
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        if (showProgress) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            viewModel.uiState.collectAsState().value?.let {
                RestaurantDetail(
                    modifier = Modifier
                        .padding(innerPadding),
                    it,
                    onBackClick
                )
            }
        }

    }

}

@Composable
fun RestaurantDetail(
    modifier: Modifier,
    uiState: RestaurantDetailResponse?,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Column(modifier.verticalScroll(rememberScrollState())) {
        Box {

            AsyncImage(
                model = if (uiState?.photos.isNullOrEmpty()) "" else {
                    uiState?.photos!![0].prefix + "original" + uiState.photos[0].suffix
                },
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
            )

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = restaurantInformationTopPadding)
                    .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(color = Color.White)
                    .padding(horizontal = restaurantInformationHorizontalPadding)
                    .padding(top = restaurantInformationTopInnerPadding)
            ) {

                var favorite by remember {
                    mutableStateOf(false)
                }

                Row {
                    Text(
                        text = uiState?.name.orEmpty(),
                        color = Color.Black,
                        style = TextStyle(
                            fontSize = 32.sp
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = restaurantTitleEndPadding)
                    )

                    Image(
                        painter = painterResource(id = if (favorite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite_unselected),
                        contentDescription = "favorite Icon",
                        modifier = Modifier
                            .size(favoriteIconSize)
                            .padding(favoriteIconPadding)
                            .clickable {
                                favorite = !favorite
                            }
                    )
                }

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp)
                ) {
                    val (divider, ratingView, addressView) = createRefs()

                    VerticalDivider(modifier = Modifier
                        .constrainAs(divider) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(ratingView.bottom)
                            height = Dimension.fillToConstraints
                        }
                    )

                    ConstraintLayout(modifier = Modifier
                        .constrainAs(addressView) {
                            start.linkTo(divider.end, 8.dp)
                            end.linkTo(parent.end)
                            top.linkTo(parent.top)
                            width = Dimension.fillToConstraints
                        }
                        .offset(y = (-20).dp)
                        .clickable {
                            uiState?.geocodes?.main?.latitude?.let { latitude ->
                                uiState.geocodes?.main?.longitude?.let { longitude ->
                                    val uri = Uri.parse("google.navigation:q=$latitude,$longitude")
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    context.startActivity(intent)
                                }
                            }
                        }
                    ) {
                        val (mapIcon, addressText, directionIcon) = createRefs()
                        Image(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = "",
                            modifier = modifier
                                .size(18.dp)
                                .constrainAs(mapIcon) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                }
                        )

                        Image(
                            painter = painterResource(id = R.drawable.ic_forward_arrow),
                            contentDescription = "",
                            modifier = modifier
                                .padding(start = 10.dp)
                                .size(20.dp)
                                .constrainAs(directionIcon) {
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                }
                        )

                        Text(
                            text = if (uiState?.location?.formatted_address == null) "Not Available" else uiState.location?.formatted_address.orEmpty(),
                            color = Color(0xff353535),
                            style = TextStyle(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = modifier
                                .constrainAs(addressText) {
                                    start.linkTo(mapIcon.end, 4.dp)
                                    end.linkTo(directionIcon.start)
                                    top.linkTo(parent.top)
                                    width = Dimension.preferredWrapContent
//                                    height = Dimension.preferredWrapContent
                                }
                        )

                    }

                    Column(
                        modifier = Modifier
                            .constrainAs(ratingView) {
                                start.linkTo(parent.start)
                                end.linkTo(divider.start)
                                top.linkTo(parent.top)
                                width = Dimension.fillToConstraints
                            }
                    ) {
                        if (uiState?.rating != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RatingBar(
                                    rating = uiState.rating?.div(2)?.toFloat() ?: 0.0f,
                                    spaceBetween = 4.dp
                                )

                                Text(
                                    text = df.format(uiState.rating?.div(2)).toString(),
                                    color = Color(0xff353535),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                )
                            }
                        }

                        Text(
                            text = "${uiState?.stats?.total_ratings.takeIf { it != null }} Reviews",
                            color = Color(0xffababab),
                            style = TextStyle(
                                fontSize = 10.sp
                            ),
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                    }

                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    color = Color.Black.copy(alpha = 0.2f)
                )

            }

            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "",
                modifier = Modifier
                    .padding(backIconPadding)
                    .size(backIconSize)
                    .padding(10.dp)
                    .clickable {
                        onBackClick()
                    }
            )
        }
    }
}
