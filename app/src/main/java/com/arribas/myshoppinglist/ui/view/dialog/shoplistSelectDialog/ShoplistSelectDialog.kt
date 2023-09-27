package com.arribas.myshoppinglist.ui.view.dialog.shoplistSelectDialog


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.arribas.myshoppinglist.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.ui.view.app.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.screen.shoplist.ShoplistUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoplistSelectDialog(
    value: ShoplistUiState,
    onItemValueChange: (ShoplistUiState) -> Unit = {},
    isShow: Boolean,
    setShowDialog: (Boolean) -> Unit,
    viewModel: ShoplistSelectDialogViewModel = viewModel(factory = AppViewModelProvider.Factory)) {

    val shoplistUiState by viewModel.listUiState.collectAsState()

    viewModel.setShoplist(value)

    if(isShow) {

        Dialog(onDismissRequest = { setShowDialog(false) }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Seleccionar lista",
                                style = TextStyle(
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Icon(
                                imageVector = Icons.Filled.List,
                                contentDescription = "",
                                tint = colorResource(R.color.my_secondary),
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp)
                                    .clickable { setShowDialog(false) }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 16.dp, end = 8.dp)
                        ) {
                            var expanded by remember { mutableStateOf(false) }
                            var selectedOptionText by remember {
                                mutableStateOf(viewModel.shoplistUiState.id)
                            }

                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = {
                                    expanded = !expanded
                                },
                                modifier = Modifier.weight(7f)
                            ) {
                                TextField(
                                    readOnly = true,
                                    value = shoplistUiState?.itemList?.find { it.id === selectedOptionText }?.name.orEmpty(),

                                    onValueChange = {},
                                    label = { Text("Listas") },
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
                                    modifier = Modifier.background(
                                        colorResource(id = R.color.my_primary)
                                    )
                                ) {
                                    shoplistUiState.itemList.forEachIndexed() { index, shoplist ->
                                        DropdownMenuItem(
                                            onClick = {
                                                selectedOptionText = shoplist.id
                                                onItemValueChange(
                                                    viewModel.shoplistUiState.copy(
                                                        id = shoplist.id,
                                                        name = shoplist.name,
                                                        type = shoplist.type
                                                    )
                                                )
                                                expanded = false
                                            },
                                            text = { Text(text = shoplist.name) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}