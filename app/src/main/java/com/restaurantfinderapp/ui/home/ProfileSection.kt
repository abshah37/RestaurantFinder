package com.restaurantfinderapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.restaurantfinderapp.R

private val profileImageSize = 56.dp
private val profileTextStartPadding = 16.dp
private val menuImageSize = 48.dp
private val menuPadding = 10.dp

@Composable
fun ProfileSection(modifier: Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.profile),
            contentDescription = "profile",
            modifier = Modifier
                .requiredSize(size = profileImageSize)
                .clip(shape = CircleShape)
        )

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                ) { append("Hi\n") }
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) { append("Kitty!") }
            },
            modifier = Modifier
                .padding(start = profileTextStartPadding)
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "Menu",
                colorFilter = ColorFilter.tint(Color(0xff000000)),
                modifier = Modifier
                    .requiredSize(menuImageSize)
                    .padding(all = menuPadding)
            )

        }
    }

}

@Preview
@Composable
fun PreviewProfileSection() {
    ProfileSection(modifier = Modifier)
}