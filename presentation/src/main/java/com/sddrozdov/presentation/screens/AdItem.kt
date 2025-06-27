package com.sddrozdov.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.sddrozdov.presentation.R

@Composable
fun AdCard(
    title: String,
    imageRes: Int,
    description: String,
    price: String,
    viewCount: Int,
    favCount: Int,
    publishTime: String,
    onClick: () -> Unit,
//    onEditClick: () -> Unit,
//    onDeleteClick: () -> Unit
) {

    val primarySurface = Color(0xFFF7F9FC)
    val accentColor = Color(0xFF6C5CE7)
    val darkText = Color(0xFF2D3436)
    val lightText = Color(0xFF636E72)
    val priceBg = Color(0xFF00B894)
    val iconColor = Color(0xFF636E72)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = primarySurface)

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(
                        color = accentColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                color = accentColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = darkText,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 3
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = price,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            color = priceBg,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 16.sp
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color(0xFFDFE6E9)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconWithCount(
                        iconRes = R.drawable.ic_eye,
                        count = viewCount,
                        tint = iconColor,
                        onIconClick = {}

                    )

                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconWithCount(
                        iconRes = R.drawable.ic_fav_normal,
                        count = favCount,
                        tint = iconColor,
                        onIconClick = {}//todo("" Добавление в избранные)
                    )
                }

//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        text = publishTime,
//                        color = lightText,
//                        fontSize = 12.sp
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    IconButton(
//                        onClick = onEditClick,
//                        modifier = Modifier.size(36.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_edit),
//                            contentDescription = "Edit",
//                            tint = accentColor
//                        )
//                    }
//                    IconButton(
//                        onClick = onDeleteClick,
//                        modifier = Modifier.size(36.dp)
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_delete_image),
//                            contentDescription = "Delete",
//                            tint = Color(0xFFE17055)
//                        )
//                    }
//                }
            }
        }
    }
}

@Composable
fun IconWithCount(iconRes: Int, count: Int, tint: Color, onIconClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clickable { onIconClick() },
            tint = tint
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = count.toString(),
            color = tint,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAdCard() {
    AdCard(
        title = "Дизайнерский стул Eames",
        imageRes = R.drawable.ic_def_image,
        description = "Эргономичное кресло премиум-класса с подлокотниками из натурального дуба. Идеальное состояние.",
        price = "24 990₽",
        viewCount = 142,
        favCount = 28,
        publishTime = "2 ч назад",
        onClick = {}
//        onEditClick = { },
//        onDeleteClick = { }
    )
}