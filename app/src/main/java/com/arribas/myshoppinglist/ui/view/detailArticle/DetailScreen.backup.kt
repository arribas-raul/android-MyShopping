package com.arribas.myshoppinglist.ui.view.detailArticle

/*
object DetailDestination : NavigationDestination {
    const val itemIdArg = "itemId"

    override val route = "detail"
    val routeWithArgs = "$route/{$itemIdArg}"

    override val titleRes = R.string.item_detail_title
}

@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory),
    categoryViewModel: CategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val dialogState: DialogUiState by viewModel.dialogState.collectAsState()
    val categoryDialogState: TextFieldDialogUiState by categoryViewModel.dialogState.collectAsState()
    val listCategoryUiState by categoryViewModel.listUiState.collectAsState()

    DetailForm(
        navigateBack = { navigateBack() },
        updateUiState = { viewModel.updateUiState(it) },

        updateItem = {
            coroutineScope.launch(Dispatchers.IO) {
                viewModel.updateItem()
                //navigateBack()
            }
        },

        deleteItem = viewModel::onDialogDelete,
        articleUiState = viewModel.articleUiState,
        listCategoryUiState = listCategoryUiState,
        onCategoryClick = categoryViewModel::openDialog,
        modifier = modifier
    )

    SimpleAlertDialog(
        dialogState = dialogState,
        onDismiss = viewModel::onDialogDismiss,
        onConfirm = {
            coroutineScope.launch {
                viewModel.onDialogConfirm()
                navigateBack()
            }
        }
    )

    TextFieldAlertDialog(
        dialogState = categoryDialogState,
        onConfirm = categoryViewModel::onDialogConfirm,
        onDismiss = categoryViewModel::onDialogDismiss,
        onValueChange = { categoryViewModel.updateUiState(it) },
        onKeyEvent = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailForm(
    navigateBack: () -> Unit = {},
    updateUiState: (ArticleUiState) -> Unit = {},
    updateItem: () -> Unit = {},
    deleteItem: () -> Unit = {},
    articleUiState: ArticleUiState,
    listCategoryUiState: ListCategoryUiState = ListCategoryUiState(),
    onCategoryClick: () -> Unit = {},
    modifier: Modifier = Modifier)
{
    Scaffold(
        topBar = {
            TopBar(
                title = articleUiState.name,
                canNavigateBack = true,
                navigateUp = navigateBack,
                tag = MainTag.ITEM_LIST,
                onClickDrawer = {}
            )
        },

        ) { innerPadding ->

        Row(modifier = modifier
            .wrapContentWidth()
            .padding(innerPadding)
        ) {

            DetailBody(
                articleUiState = articleUiState,
                onItemValueChange = { updateUiState(it) },
                onSaveClick = { updateItem() },
                onDeleteClick = deleteItem,
                listCategoryUiState = listCategoryUiState,
                onCategoryClick = onCategoryClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    val articleUiState = ArticleUiState(name = "Bolsa de patatas")

    MyShoppingListTheme {
        DetailForm(articleUiState = articleUiState )
    }
}*/