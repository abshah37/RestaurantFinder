package com.restaurantfinderapp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.restaurantfinderapp.R
import com.restaurantfinderapp.common.RatingBar
import com.restaurantfinderapp.data.model.GetRestaurantsResponse
import java.math.RoundingMode
import java.text.DecimalFormat

private val itemTopPadding = 16.dp
private val columnStartPadding = 16.dp
val df = DecimalFormat("#.#")

@Composable
fun RestaurantItem(
    data: GetRestaurantsResponse.Results?,
    modifier: Modifier = Modifier,
    onItemClick: (GetRestaurantsResponse.Results) -> Unit
) {

    data?.let {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onItemClick(data)
                }
                .padding(top = itemTopPadding),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = if (data.photos.isNullOrEmpty().not()) {
                    data.photos[0].prefix + "492x618" + data.photos[0].suffix
                } else "",
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.no_image),
                fallback = painterResource(id = R.drawable.no_image),
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .requiredWidth(width = 82.dp)
                    .requiredHeight(height = 103.dp)
                    .clip(shape = RoundedCornerShape(19.dp))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-16).dp)
                    .padding(start = columnStartPadding),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = data.name.orEmpty(),
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                if (data.rating != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 4.dp)
                    ) {
                        RatingBar(
                            rating = data.rating?.div(2)?.toFloat() ?: 0.0f,
                            spaceBetween = 4.dp
                        )

                        Text(
                            text = df.format(data.rating?.div(2)).toString(),
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

                Row(
                    modifier = Modifier
                        .padding(top = 4.dp)
                ) {
                    if (data.hours != null && data.hours!!.open_now != null) {

                        Row {
                            Text(
                                lineHeight = 14.sp,
                                text = if (data.hours!!.open_now == true) "Open" else "Close",
                                color = if (data.hours!!.open_now == true) Color.Green else Color.Red,
                                style = TextStyle(
                                    fontSize = 14.sp
                                )
                            )

                            VerticalDivider(
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .height(10.dp)
                                    .padding(horizontal = 10.dp)
                            )
                        }

                        if (data.distance != null) {
                            Text(
                                lineHeight = 14.sp,
                                text = data.distance!!.formatDistance(),
                                style = TextStyle(
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                }

            }
        }
    }

}

fun Int?.formatDistance(): String {
    this?.let {
        df.roundingMode = RoundingMode.CEILING
        val distance = this.times(0.000621371).toFloat()
        return df.format(distance) + " miles away"
    }

    return ""
}

@Preview(showBackground = true)
@Composable
private fun PreviewRestaurantItem() {
    RestaurantItem(
        data = GetRestaurantsResponse.Results(
            name = "MoonBean's Coffee",
            hours = GetRestaurantsResponse.Hours(open_now = true),
            rating = 8.6
        ),
        onItemClick = {}
    )
}
