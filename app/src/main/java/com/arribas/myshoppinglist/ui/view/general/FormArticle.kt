package com.arribas.myshoppinglist.ui.view.general

import android.view.KeyEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.viewModel.listArticle.ArticleUiState

@Composable
fun DetailBody(
    articleUiState: ArticleUiState,
    onItemValueChange: (ArticleUiState) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ItemInputForm(
            articleUiState = articleUiState,
            onValueChange = onItemValueChange,
            onKeyEvent = onSaveClick
        )

        Button(
            onClick = onSaveClick,
            enabled = articleUiState.actionEnabled,
            colors = ButtonDefaults.buttonColors(colorResource(R.color.my_primary)),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.save_action),
                color = colorResource(R.color.white)
            )
        }

        if(articleUiState.id > 0) {
            Button(
                onClick = onDeleteClick,
                enabled = articleUiState.actionEnabled,
                colors = ButtonDefaults.buttonColors(colorResource(R.color.my_danger)),
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(
                    text = stringResource(R.string.delete),
                    color = colorResource(R.color.white)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInputForm(
    articleUiState: ArticleUiState,
    onValueChange: (ArticleUiState) -> Unit = {},
    enabled: Boolean = true,
    onKeyEvent: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = articleUiState.name,
        onValueChange = { onValueChange(articleUiState.copy(name = it.replace("\n",""))) },
        label = { Text(stringResource(R.string.item_name_req)) },

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
}