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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.view.article.articleList.ArticleUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditText(
     title: String,
     name: String = "",
     onValueChange: (String) -> Unit = {},
     enabled: Boolean = true,
     onKeyEvent: () -> Unit = {},
     modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = name,
        onValueChange = { onValueChange(it.replace("\n","")) },
        label = { Text(title) },

        enabled = enabled,
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

        modifier = modifier
            .fillMaxWidth()
            .onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    onKeyEvent()
                    true
                }

                false
            }
    )
}