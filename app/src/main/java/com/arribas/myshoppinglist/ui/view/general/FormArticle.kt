package com.arribas.myshoppinglist.ui.view.general

import android.util.Size
import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.theme.MyShoppingListTheme
import com.arribas.myshoppinglist.ui.view.Category.ListCategoryUiState
import com.arribas.myshoppinglist.ui.view.detailArticle.NewForm
import com.arribas.myshoppinglist.ui.view.listArticle.ArticleUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBody(
    articleUiState: ArticleUiState = ArticleUiState(),
    onItemValueChange: (ArticleUiState) -> Unit = {},
    onSaveClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    listCategoryUiState: ListCategoryUiState = ListCategoryUiState(),
    onCategoryClick: () -> Unit = {},
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

        Row(Modifier.fillMaxWidth()) {
            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf("") }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .weight(7f)
            ) {
                TextField(
                    readOnly = true,
                    value = selectedOptionText,
                    onValueChange = { },
                    label = { Text("Categorias") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth()
                        .menuAnchor()
                        .background(colorResource(id = R.color.white))
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.background(colorResource(id = R.color.my_primary))
                ) {
                    listCategoryUiState.itemList.forEachIndexed() { index, category ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = category.name
                                onItemValueChange(articleUiState.copy(category = category.id))
                                expanded = false
                            },
                            text = { Text(text = category.name) }
                        )
                    }
                }
            }

            IconButton(
                onClick = onCategoryClick,
                modifier = modifier
                    .weight(1f)
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp)
                    .background(color = colorResource(R.color.my_primary)))
            {
                Icon(
                    imageVector = Icons.Filled.Add,
                    tint = Color.White,
                    contentDescription = "More"
                )
            }
        }

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
    articleUiState: ArticleUiState = ArticleUiState(),
    onValueChange: (ArticleUiState) -> Unit = {},
    enabled: Boolean = true,
    onKeyEvent: () -> Unit = {},
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

@Preview(showBackground = true)
@Composable
fun ItemInputFormPreview() {
    ItemInputForm()
}

@Preview(showBackground = true)
@Composable
fun DetailBodyPreview() {
    DetailBody()
}