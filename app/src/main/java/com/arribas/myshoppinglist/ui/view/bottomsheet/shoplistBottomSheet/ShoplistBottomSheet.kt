package com.arribas.myshoppinglist.ui.view.bottomsheet.shoplistBottomSheet

import android.view.KeyEvent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.data.utils.DialogUiState
import com.arribas.myshoppinglist.ui.view.dialog.general.SimpleAlertDialog
import com.arribas.myshoppinglist.ui.view.general.EditText
import com.arribas.myshoppinglist.ui.view.screen.shoplist.ShoplistUiState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoplistBottomSheet(
    viewModel: ShoplistBottomSheetViewModel,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState
){
    val dialogState: DialogUiState by viewModel.dialogState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel
            .toastMessage
            .collect { message ->
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT,
                ).show()
            }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,

        sheetContent = {
            ShoplistBottomSheetContent(
                shoplistUiState = viewModel.shoplistUiState,
                onValueChange = { viewModel.onChange(it) },
                onClick = {
                    viewModel.onSave()
                }
            )
        }
    ) {}

    SimpleAlertDialog(
        dialogState = dialogState,
        onDismiss = viewModel::onDialogDismiss,
        onConfirm = viewModel::onDialogConfirm
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoplistBottomSheetContent(
    shoplistUiState: ShoplistUiState,
    onValueChange: (ShoplistUiState) -> Unit,
    onClick: (ShoplistUiState) -> Unit
){
    val focusManager = LocalFocusManager.current

    val title =
        if(shoplistUiState.id === 0){
            stringResource(R.string.shoplist_bottomsheet_title_create)
        }else{
            stringResource(R.string.shoplist_bottomsheet_title_update)
        }

    Surface(
        modifier = Modifier.height(250.dp),
        color = colorResource(R.color.my_primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp),
                color = Color.White)

            Divider(
                modifier = Modifier.padding(5.dp),
                color = Color.White
            )

            OutlinedTextField(
                label = { Text(stringResource(R.string.shoplist_bottomsheet_form_name)) },
                value = shoplistUiState.name,
                singleLine = true,
                onValueChange = {
                    onValueChange( shoplistUiState.copy(name = it))
                },


                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                colors = TextFieldDefaults
                    .textFieldColors(
                        textColor = colorResource(R.color.black),
                        containerColor = colorResource(R.color.my_background)
                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            focusManager.moveFocus(FocusDirection.Down)
                            true
                        }

                        false
                    }
            )

            OutlinedTextField(
                label = { Text(stringResource(R.string.shoplist_bottomsheet_form_description)) },
                value = shoplistUiState.type,
                singleLine = true,
                onValueChange = {
                    onValueChange( shoplistUiState.copy(type = it))
                },

                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                colors = TextFieldDefaults
                    .textFieldColors(
                        textColor = colorResource(R.color.black),
                        containerColor = colorResource(R.color.my_background)
                    ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            focusManager.clearFocus()
                            true
                        }

                        false
                    }
            )

            Spacer(
                modifier = Modifier.padding(5.dp)
            )

            Button(
                onClick = { onClick(shoplistUiState) },
                enabled = true,
                colors = ButtonDefaults.buttonColors(colorResource(R.color.my_secondary)),
                shape = RoundedCornerShape(size = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.shoplist_bottomsheet_bt_save_desctiption)
                )
                Spacer(modifier = Modifier.width(width = 8.dp))
                Text(
                    text = stringResource(R.string.create_action).uppercase(),
                    color = colorResource(R.color.white)
                )
            }
        }
    }
}