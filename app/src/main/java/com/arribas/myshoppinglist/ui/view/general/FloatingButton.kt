package com.arribas.myshoppinglist.ui.view.general

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arribas.myshoppinglist.R

@Composable
fun FloatingButton(
    onClick: ()-> Unit,
    fabVisibility: Boolean,
    modifier: Modifier = Modifier){

    val density = LocalDensity.current

    AnimatedVisibility(
        modifier = modifier,
        visible = fabVisibility,
        enter = slideInVertically {
            with(density) { 40.dp.roundToPx() }
        } + fadeIn(),
        exit = fadeOut(
            animationSpec = keyframes {
                this.durationMillis = 120
            }
        )
    ) {
        FloatingActionButton(
            onClick = { onClick() },
            shape = CircleShape,
            containerColor = colorResource(R.color.my_primary),
            contentColor = colorResource(R.color.white)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.item_entry_title)
            )
        }
    }
}