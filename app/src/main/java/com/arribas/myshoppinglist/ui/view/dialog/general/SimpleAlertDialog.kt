package com.arribas.myshoppinglist.ui.view.dialog.general

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.utils.DialogUiState

@Composable
fun SimpleAlertDialog(
    dialogState: DialogUiState,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    if(dialogState.isShow) {
        AlertDialog(
            onDismissRequest = onDismiss,

            confirmButton = {
                TextButton(onClick = onConfirm)
                { Text(text = "OK", color = colorResource(R.color.my_primary)) }
            },

            dismissButton = {
                if(dialogState.isShowBtDismiss) {
                    TextButton(onClick = onDismiss)
                    { Text(text = "Cancel", color = colorResource(R.color.my_primary)) }
                }
            },

            title = { Text(text = dialogState.title, color = Color.Black) },
            text = { Text(text = dialogState.body, color = Color.Black) },
            containerColor = colorResource(R.color.my_background)
        )
    }
}