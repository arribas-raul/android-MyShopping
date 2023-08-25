package com.arribas.myshoppinglist.ui.view.general

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.view.article.articleList.ArticleUiState
import com.arribas.myshoppinglist.ui.view.shoplist.ShoplistUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditText(
    title: String,
    value: String,
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    onKeyEvent: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(value) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,

        onValueChange = {
            name = it
            onValueChange(it.replace("\n",""))
        },

        label = { Text(title) },

        enabled = enabled,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),

        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.moveFocus(
                    focusDirection = FocusDirection.Next,
                )
                onKeyEvent()
            }
        ),

        colors = TextFieldDefaults
            .textFieldColors(
                textColor = colorResource(R.color.black),
                containerColor = colorResource(R.color.my_background)
            ),

        modifier = modifier
            .fillMaxWidth()
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    focusManager.moveFocus(
                        focusDirection = FocusDirection.Next,
                    )
                    onKeyEvent()
                    true
                }

                false
            }
    )
}