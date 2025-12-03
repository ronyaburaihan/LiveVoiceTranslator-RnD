package com.example.livevoicetranslator_rd.presentation.screen.phrases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.component.CategoryCard
import com.example.livevoicetranslator_rd.presentation.navigation.ScreenRoute
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_accommodation
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_airport
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_basic
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_emergency
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_favourite
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_others
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_restaurant
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_shopping
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_tourism
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_transport
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_travel
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_workplace

@Composable
fun PhrasesScreen() {

    val categories = remember {
        listOf(
            CategoryData("1", "Basics", Res.drawable.ic_cat_basic),
            CategoryData("2", "Travel", Res.drawable.ic_cat_travel, true),
            CategoryData("3", "Airport", Res.drawable.ic_cat_airport, true),
            CategoryData("4", "Restaurant", Res.drawable.ic_cat_restaurant),
            CategoryData("5", "Transport", Res.drawable.ic_cat_transport),
            CategoryData("6", "Accommodation", Res.drawable.ic_cat_accommodation, true),
            CategoryData("7", "Shopping", Res.drawable.ic_cat_shopping),
            CategoryData("8", "Emergency", Res.drawable.ic_cat_emergency, true),
            CategoryData("9", "Tourism", Res.drawable.ic_cat_tourism),
            CategoryData("10", "Workplace", Res.drawable.ic_cat_workplace),
            CategoryData("11", "Other", Res.drawable.ic_cat_others),
            CategoryData("12", "Favourite", Res.drawable.ic_cat_favourite)
        )
    }

    val navController = LocalNavController.current

    PhrasesScreenContent(
        categories = categories,
        onCategoryClick = { category ->
            navController.navigate(ScreenRoute.PhraseDetail(category.title))
        },
        onNavigationClick = {
            navController.navigateUp()
        },
        onProButtonClick = {
            navController.navigate(ScreenRoute.Premium)
        }
    )
}


@Composable
fun PhrasesScreenContent(
    categories: List<CategoryData>,
    onCategoryClick: (CategoryData) -> Unit,
    onNavigationClick: () -> Unit = {},
    onProButtonClick: () -> Unit = {},
) {

    Scaffold { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(17.dp)
        ) {
            items(categories) { item ->
                CategoryCard(
                    title = item.title,
                    icon = item.iconRes,
                    isLocked = item.isLocked,
                    onClick = { onCategoryClick(item) }
                )
            }
        }
    }
}
