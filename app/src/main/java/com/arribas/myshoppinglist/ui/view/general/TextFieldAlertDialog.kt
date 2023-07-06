package com.arribas.myshoppinglist.ui.view.general

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.data.utils.TextFieldDialogUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldAlertDialog(
    dialogState: TextFieldDialogUiState = TextFieldDialogUiState(),
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onKeyEvent: () -> Unit = {},
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

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

            text = {
                Text(text = dialogState.body, color = Color.Black)

                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        onValueChange(it.text)
                    },
                    label = { Text(stringResource(R.string.item_name_req)) },

                    enabled = true,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),

                    keyboardActions = KeyboardActions(
                        onDone = {
                            onKeyEvent()
                        }
                    ),

                    colors = TextFieldDefaults
                        .textFieldColors(
                            textColor = colorResource(R.color.black),
                            containerColor = colorResource(R.color.my_background)
                        ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .onKeyEvent {
                            if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                                onKeyEvent()
                                true
                            }

                            false
                        }
                )
            },

            containerColor = colorResource(R.color.my_background)
        )
    }
}