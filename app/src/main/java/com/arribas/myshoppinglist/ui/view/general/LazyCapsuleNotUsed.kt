package com.arribas.myshoppinglist.ui.view.general

/*
* LazyRow(
            modifier = modifier.padding(top = 5.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 3.dp)
        ){
            items(items = articleUiState.categories, key = { it.id }) { category ->

                Card(modifier = Modifier
                    .background(
                        color = Color.LightGray,
                        shape = RoundedCornerShape(size = 10.dp)
                    )
                    .padding(start = 8.dp, end = 8.dp),) {
                    Row(){
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Black,
                            modifier = Modifier
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(size = 10.dp)
                                )
                                .padding(start = 8.dp, end = 8.dp), // add inner padding
                        )
                        ClickableText(
                            text = AnnotatedString("x"),
                            style = MaterialTheme.typography.titleSmall,
                            onClick = {
                                //var aux = articleUiState.categories.indexOfFirst { it.id == category.id }
                                //articleUiState.categories.removeAt(aux)
                            },
                            modifier = Modifier
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(size = 10.dp)
                                )
                                .padding(start = 8.dp, end = 8.dp), // add inner padding
                        )
                    }
                }

            }
        }
* */