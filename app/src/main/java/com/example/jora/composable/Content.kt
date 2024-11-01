package com.example.jora.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DualRowContent(
    modifier: Modifier = Modifier,
    leftSide: @Composable (() -> Unit)? = null,
    rightSide: @Composable (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth(1f)
    ) {
        Box {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                leftSide?.invoke()
            }
        }
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalAlignment = Alignment.End
            ) {
                rightSide?.invoke()
            }
        }
    }
}

@Composable
fun DualRowContent(
    leftSide: @Composable () -> Unit,
    rightSide: @Composable () -> Unit,
    vertical:  Alignment.Vertical,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = vertical,
        modifier = modifier.fillMaxWidth(1f)
    ) {
        Box {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                leftSide()
            }
        }
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(1f),
                horizontalAlignment = Alignment.End
            ) {
                rightSide()
            }
        }
    }
}