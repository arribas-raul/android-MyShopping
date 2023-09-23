package com.arribas.myshoppinglist.ui.view.filter

import android.view.KeyEvent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    isTextEmptyVisible: Boolean = false,
    modifier: Modifier = Modifier
){
    var text by remember { mutableStateOf(filterUiState.name) }

    Column() {
        OutlinedTextField(
            value = filterUiState.name,
            shape = RoundedCornerShape(5.dp),

            onValueChange = {
                text = it
                onValueChange(text)
            },

            label = { Text(stringResource(R.string.general_filter_label)) },

            enabled = true,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),

            keyboardActions = KeyboardActions(
                onDone = {
                    onKeyEvent()
                }
            ),

            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = colorResource(R.color.black),
                focusedBorderColor = colorResource(R.color.my_primary),
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = colorResource(R.color.my_primary)
            ),

            trailingIcon = {
                if(!filterUiState.name.isEmpty()) {
                    IconButton(onClick = clearName) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(R.string.general_filter_clear_text)
                        )
                    }
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

        if(isTextEmptyVisible) {
            Column(modifier = modifier.fillMaxWidth())
            {
                Text(
                    text = stringResource(R.string.general_filter_empty_data),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = modifier
                        .padding(10.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}