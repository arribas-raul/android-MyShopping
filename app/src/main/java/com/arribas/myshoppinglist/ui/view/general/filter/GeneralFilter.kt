package com.arribas.myshoppinglist.ui.view.general.filter

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arribas.myshoppinglist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralFilter(
    filterUiState: GeneralFilterUiState = GeneralFilterUiState(),
    onValueChange: (String) -> Unit = {},
    onKeyEvent: () -> Unit,
    clearName: () -> Unit,
    modifier: Modifier = Modifier
){
    var text by remember { mutableStateOf(filterUiState.name) }

    OutlinedTextField(
        value = filterUiState.name,

        onValueChange = {
            text = it
            onValueChange(text)
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

        trailingIcon = {
            IconButton(onClick = clearName ) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "clear text"
                )
            }
        },

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    onKeyEvent()
                    true
                }

                false
            }
    )
}