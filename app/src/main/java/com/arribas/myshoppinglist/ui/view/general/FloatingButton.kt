package com.arribas.myshoppinglist.ui.view.general

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.arribas.myshoppinglist.R

@Composable
fun FloatingButton(
    onClick: ()-> Unit,
    modifier: Modifier = Modifier){

    val contextForToast = LocalContext.current.applicationContext

    FloatingActionButton(
        onClick = {  onClick() }
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.item_entry_title),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}