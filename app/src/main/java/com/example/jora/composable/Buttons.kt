package com.example.jora.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jora.ui.theme.background
import com.example.jora.ui.theme.buttonBackground
import com.example.jora.ui.theme.buttonTint
import com.example.jora.ui.theme.buttonTint2
import com.example.jora.ui.theme.cardBackground
import com.example.jora.ui.theme.dmDisplay
import com.example.jora.ui.theme.regularBlack

@Composable
fun AabanaButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(1f),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonColors(
            containerColor = buttonBackground,
            contentColor = buttonTint2,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = Color.LightGray
        ),
        border = BorderStroke(1.38.dp, Color.LightGray),
        onClick = {
            onClick()
        }
    ) {
        Text(
            title,
            color = buttonTint2,
            fontFamily = dmDisplay,
            fontWeight = FontWeight.W300,
            fontSize = 20.sp
        )
    }
}

@Composable
fun AabanaSecondButton(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = buttonTint2,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        onClick = { onClick() }
    ) {
        Text(
            title,
            color = color,
            fontFamily = dmDisplay,
            fontWeight = FontWeight.W600,
            fontSize = 20.sp
        )
    }
}

@Composable
fun MainSecondActionButton(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = buttonTint2,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        shape = RoundedCornerShape(4.dp),
        onClick = { onClick() }
    ) {
        Text(
            title,
            color = color,
            fontFamily = dmDisplay,
            fontWeight = FontWeight.W600,
            fontSize = 22.sp
        )
    }
}

@Composable
fun ProfileSecondActionButton(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = buttonTint2,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        onClick = { onClick() }
    ) {
        Text(
            title,
            color = color,
            fontFamily = dmDisplay,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            modifier = modifier
        )
    }
}

@Composable
fun MainActionButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(1f),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonColors(
            containerColor = buttonBackground,
            contentColor = buttonTint,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = Color.LightGray
        ),
        border = BorderStroke(1.38.dp, Color.DarkGray),
        onClick = {
            onClick()
        }
    ) {
        Text(
            title,
            color = buttonTint,
            fontFamily = dmDisplay,
            fontWeight = FontWeight.W300,
            fontSize = 22.sp
        )
    }
}

@Composable
fun ProfileActionButton(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = buttonTint,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(1f),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonColors(
            containerColor = buttonBackground,
            contentColor = color,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = Color.LightGray
        ),
        border = BorderStroke(1.38.dp, Color.DarkGray),
        onClick = {
            onClick()
        }
    ) {
        Text(
            title,
            color = color,
            fontFamily = dmDisplay,
            fontWeight = FontWeight.W300,
            fontSize = 18.sp
        )
    }
}

@Composable
fun NavigationActionButton(
    modifier: Modifier = Modifier,
    title: String,
    language: String,
    icon: ImageVector,
    color: Color = buttonTint,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.padding(8.dp).fillMaxWidth(1f),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonColors(
            containerColor = Color.White,
            contentColor = color,
            disabledContainerColor = Color.DarkGray,
            disabledContentColor = Color.LightGray
        ),
        border = BorderStroke(1.38.dp, Color.LightGray),
        onClick = {
            onClick()
        }
    ) {
        DualRowContent(leftSide = {
            Text(
                title,
                color = color,
                fontFamily = dmDisplay,
                fontWeight = FontWeight.W300,
                fontSize = 16.sp
            )
            Text(
                language,
                color = regularBlack,
                fontFamily = dmDisplay,
                fontWeight = FontWeight.W600,
                fontSize = 12.sp
            )
        }, rightSide = {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = regularBlack,
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(6.dp))
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(6.dp))
                    .padding(8.dp)
            )
        }, vertical = Alignment.CenterVertically)

    }
}
