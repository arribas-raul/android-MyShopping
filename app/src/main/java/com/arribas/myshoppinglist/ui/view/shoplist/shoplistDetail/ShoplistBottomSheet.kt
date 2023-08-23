package com.arribas.myshoppinglist.ui.view.shoplist.shoplistDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arribas.myshoppinglist.R
import com.arribas.myshoppinglist.ui.view.AppViewModelProvider
import com.arribas.myshoppinglist.ui.view.general.EditText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoplistBottomSheet(
    viewModel: ShoplistDetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState,
    showModalSheet: MutableState<Boolean>
){
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            ShoplistBottomSheetContent()
        }
    ) {
        ShoplistModalSheetWithAnchor(
            sheetState,
            showModalSheet
        )
    }
}

@Composable
fun ShoplistBottomSheetContent( ){
    Surface(
        modifier = Modifier.height(250.dp),
        color = colorResource(R.color.my_primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nueva lista",
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp),
                color = Color.White)

            Divider(
                modifier = Modifier.padding(5.dp),
                color = Color.White
            )

            EditText(
                title = "Nombre",
                name = "",
                onValueChange = {  },
                onKeyEvent = {  },
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            EditText(
                title = "Tipo",
                name = "",
                onValueChange = {  },
                onKeyEvent = {  },
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(
                modifier = Modifier.padding(5.dp)
            )

            Button(
                onClick = {  },
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
                    contentDescription = "Add article"
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoplistModalSheetWithAnchor(
    sheetState: ModalBottomSheetState,
    showModalSheet: MutableState<Boolean>
) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowUp,
            contentDescription = "",
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .clickable {
                    showModalSheet.value = !showModalSheet.value
                    scope.launch {
                        sheetState.show()
                    }
                }
        )
    }
}