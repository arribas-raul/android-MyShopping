package com.arribas.myshoppinglist.ui.view.general

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.viewModel.listArticle.ArticleUiState
import java.util.Currency
import java.util.Locale

@Composable
fun DetailBody(
    articleUiState: ArticleUiState,
    onItemValueChange: (ArticleUiState) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier.background(colorResource(R.color.my_background))
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }

        if(articleUiState.id > 0) {
            Button(
                onClick = onDeleteClick,
                enabled = articleUiState.actionEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ItemInputForm(
    articleUiState: ArticleUiState,
    modifier: Modifier = Modifier,
    onValueChange: (ArticleUiState) -> Unit = {},
    enabled: Boolean = true,
    onKeyEvent: () -> Unit
) {
    //val (focusRequester) = FocusRequester.createRefs()
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = articleUiState.name,
            onValueChange = { onValueChange(articleUiState.copy(name = it)) },
            label = { Text(stringResource(R.string.item_name_req)) },

            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    onKeyEvent()
                    //focusManager.clearFocus()
                }
            ),

            modifier = Modifier
                .fillMaxWidth()
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                        onKeyEvent()
                        //focusManager.clearFocus()
                        true
                    }
                    false
                }
        )

        /*if(articleUiState.id > 0) {
            OutlinedTextField(
                value = articleUiState.quantity,
                onValueChange = { onValueChange(articleUiState.copy(quantity = it)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text(stringResource(R.string.quantity_req)) },
                leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = true
            )
        }*/
    }
}