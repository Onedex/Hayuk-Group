package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.BrandCategory
import com.example.model.BrandConfig
import com.example.model.BrandSubCategory
import com.example.model.DirectoryFilter
import com.example.model.DirectorySortOption
import com.example.model.FoodcourtUiState
import com.example.ui.components.BrandLogoVisual
import com.example.ui.components.ChilledDrinksCravingArtwork
import com.example.ui.components.CrispyPlatterCravingArtwork
import com.example.ui.components.FierySambalCravingArtwork
import com.example.ui.components.KopiKulangoProductArtwork
import com.example.ui.theme.CarbonBlack
import com.example.ui.theme.EspressoDark
import com.example.ui.theme.HairlineBorder
import com.example.ui.theme.HayukLogoGold
import com.example.ui.theme.HayukRed
import com.example.ui.theme.HayukRedBorder
import com.example.ui.theme.HayukRedDark
import com.example.ui.theme.HayukRedDeep
import com.example.ui.theme.HayukRedSoft
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SlateText
import com.example.ui.theme.SnowSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandDirectoryScreen(
    uiState: FoodcourtUiState,
    onSelectFilter: (DirectoryFilter) -> Unit,
    onSelectSort: (DirectorySortOption) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    onRefresh: () -> Unit,
    onOrderBrand: (BrandConfig) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val featuredInView = uiState.filteredFeaturedBrands
    val gridBrands = uiState.filteredStandardBrands
    val focusManager = LocalFocusManager.current
    val pullToRefreshState = rememberPullToRefreshState()
    val popularityRankById = remember(uiState.visibleBrands) {
        uiState.visibleBrands
            .sortedByDescending { it.popularityScore }
            .mapIndexed { index, brand -> brand.brandId to (index + 1) }
            .toMap()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureWhite)
            .windowInsetsPadding(WindowInsets.statusBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Minimalist Header Bar
        Surface(
            color = PureWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("directory_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back_button),
                            tint = CarbonBlack
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.directory_title),
                            style = MaterialTheme.typography.titleLarge.copy(
                                letterSpacing = 0.6.sp
                            ),
                            color = CarbonBlack,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(R.string.directory_subtitle, uiState.totalCount),
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlateText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    DirectorySortMenuButton(
                        selectedSort = uiState.selectedSort,
                        onSelectSort = onSelectSort
                    )
                }

                // Search Bar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_brands_placeholder),
                            style = MaterialTheme.typography.bodyLarge,
                            color = SlateText
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = stringResource(R.string.cd_search_icon),
                            tint = if (uiState.searchQuery.isNotEmpty()) HayukRed else SlateText
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onSearchQueryChange("")
                                    focusManager.clearFocus()
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("directory_search_clear_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = stringResource(R.string.cd_clear_search),
                                    tint = CarbonBlack
                                )
                            }
                        }
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = CarbonBlack),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = SnowSurface,
                        focusedBorderColor = HayukRed,
                        unfocusedBorderColor = HairlineBorder,
                        cursorColor = HayukRed
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .widthIn(max = 720.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("directory_search_input")
                )

                // Horizontal Scrollable Filter Chips below Search Bar
                CategoryFilterChipRow(
                    uiState = uiState,
                    onSelectFilter = onSelectFilter,
                    modifier = Modifier
                        .widthIn(max = 720.dp)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                HorizontalDivider(color = HairlineBorder, thickness = 1.dp)
            }
        }

        // Responsive 2-column Grid with Pull-to-Refresh (3 columns on large tablet displays)
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(SnowSurface),
            contentAlignment = Alignment.TopCenter
        ) {
            val columnCount = if (maxWidth >= 720.dp) 3 else 2

            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
                state = pullToRefreshState,
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        state = pullToRefreshState,
                        isRefreshing = uiState.isRefreshing,
                        containerColor = PureWhite,
                        color = HayukRed,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("brand_directory_pull_to_refresh"),
                contentAlignment = Alignment.TopCenter
            ) {
                if (featuredInView.isEmpty() && gridBrands.isEmpty()) {
                    EmptyBrandDirectoryState(
                        searchQuery = uiState.searchQuery,
                        selectedFilter = uiState.selectedFilter,
                        onResetSearch = {
                            onClearSearch()
                            focusManager.clearFocus()
                        },
                        onBrowseAll = { onSelectFilter(DirectoryFilter.ALL) },
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columnCount),
                        modifier = Modifier
                            .widthIn(max = 840.dp)
                            .fillMaxSize()
                            .testTag("brand_directory_grid"),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 28.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = featuredInView,
                            key = { "featured_${it.brandId}" },
                            span = { GridItemSpan(maxLineSpan) }
                        ) { featuredBrand ->
                            DirectoryFeaturedBrandCard(
                                brand = featuredBrand,
                                popularityRank = if (uiState.selectedSort == DirectorySortOption.POPULARITY) {
                                    popularityRankById[featuredBrand.brandId]
                                } else {
                                    null
                                },
                                isFavorite = uiState.isFavorite(featuredBrand.brandId),
                                onToggleFavorite = { onToggleFavorite(featuredBrand.brandId) },
                                onOrderClick = { onOrderBrand(featuredBrand) }
                            )
                        }

                        items(
                            items = gridBrands,
                            key = { it.brandId }
                        ) { brand ->
                            StandardBrandGridCard(
                                brand = brand,
                                popularityRank = if (uiState.selectedSort == DirectorySortOption.POPULARITY) {
                                    popularityRankById[brand.brandId]
                                } else {
                                    null
                                },
                                isFavorite = uiState.isFavorite(brand.brandId),
                                onToggleFavorite = { onToggleFavorite(brand.brandId) },
                                onOrderClick = { onOrderBrand(brand) }
                            )
                        }

                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Spacer(
                                modifier = Modifier
                                    .height(4.dp)
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class FilterChipSpec(
    val filter: DirectoryFilter,
    val label: String,
    val testTag: String
)

@Composable
private fun CategoryFilterChipRow(
    uiState: FoodcourtUiState,
    onSelectFilter: (DirectoryFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    val chips = listOf(
        FilterChipSpec(
            filter = DirectoryFilter.ALL,
            label = "${stringResource(R.string.tab_all)} (${uiState.totalCount})",
            testTag = "filter_chip_all"
        ),
        FilterChipSpec(
            filter = DirectoryFilter.FOOD,
            label = "${stringResource(R.string.tab_food)} (${uiState.foodCount})",
            testTag = "filter_chip_food"
        ),
        FilterChipSpec(
            filter = DirectoryFilter.BEVERAGES,
            label = "${stringResource(R.string.tab_beverage)} (${uiState.beveragesCount})",
            testTag = "filter_chip_beverage"
        ),
        FilterChipSpec(
            filter = DirectoryFilter.FAVORITES,
            label = "${stringResource(R.string.tab_favorites)} (${uiState.favoritesCount})",
            testTag = "filter_chip_favorites"
        ),
        FilterChipSpec(
            filter = DirectoryFilter.MAIN_COURSE,
            label = "${stringResource(R.string.tab_main_course)} (${uiState.mainCourseCount})",
            testTag = "filter_chip_main_course"
        ),
        FilterChipSpec(
            filter = DirectoryFilter.SNACKS,
            label = "${stringResource(R.string.tab_snacks)} (${uiState.snacksCount})",
            testTag = "filter_chip_snacks"
        )
    )

    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .testTag("directory_filter_chips_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        chips.forEach { chip ->
            val selected = uiState.selectedFilter == chip.filter
            FilterChip(
                selected = selected,
                onClick = { onSelectFilter(chip.filter) },
                label = {
                    Text(
                        text = chip.label,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1
                    )
                },
                leadingIcon = when {
                    selected -> {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    chip.filter == DirectoryFilter.FAVORITES -> {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Favorite,
                                contentDescription = null,
                                tint = HayukRed,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                    else -> null
                },
                shape = RoundedCornerShape(50),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = SnowSurface,
                    labelColor = CarbonBlack,
                    selectedContainerColor = HayukRed,
                    selectedLabelColor = PureWhite,
                    selectedLeadingIconColor = PureWhite
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    borderColor = HairlineBorder,
                    selectedBorderColor = HayukRed,
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.dp
                ),
                modifier = Modifier
                    .height(48.dp)
                    .testTag(chip.testTag)
            )
        }
    }
}

@Composable
private fun DirectorySortMenuButton(
    selectedSort: DirectorySortOption,
    onSelectSort: (DirectorySortOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isNonDefault = selectedSort != DirectorySortOption.DEFAULT
    val buttonLabel = when (selectedSort) {
        DirectorySortOption.DEFAULT -> stringResource(R.string.sort_label)
        DirectorySortOption.POPULARITY -> stringResource(R.string.sort_popular_short)
        DirectorySortOption.NAME_ASC -> stringResource(R.string.sort_name_short)
    }

    Box {
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(50),
            color = if (isNonDefault) HayukRedSoft else SnowSurface,
            border = BorderStroke(1.dp, if (isNonDefault) HayukRed else HairlineBorder),
            modifier = Modifier
                .height(48.dp)
                .testTag("directory_sort_button")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.SwapVert,
                    contentDescription = stringResource(R.string.cd_sort_brands),
                    tint = if (isNonDefault) HayukRedDeep else CarbonBlack,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = buttonLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isNonDefault) HayukRedDeep else CarbonBlack,
                    maxLines = 1
                )
                Icon(
                    imageVector = Icons.Rounded.ArrowDropDown,
                    contentDescription = null,
                    tint = if (isNonDefault) HayukRedDeep else SlateText,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = PureWhite,
            modifier = Modifier.testTag("directory_sort_menu")
        ) {
            SortMenuOptionItem(
                label = stringResource(R.string.sort_popularity),
                selected = selectedSort == DirectorySortOption.POPULARITY,
                testTag = "sort_option_popularity",
                onClick = {
                    expanded = false
                    onSelectSort(DirectorySortOption.POPULARITY)
                }
            )
            SortMenuOptionItem(
                label = stringResource(R.string.sort_name_asc),
                selected = selectedSort == DirectorySortOption.NAME_ASC,
                testTag = "sort_option_name_asc",
                onClick = {
                    expanded = false
                    onSelectSort(DirectorySortOption.NAME_ASC)
                }
            )
            SortMenuOptionItem(
                label = stringResource(R.string.sort_default),
                selected = selectedSort == DirectorySortOption.DEFAULT,
                testTag = "sort_option_default",
                onClick = {
                    expanded = false
                    onSelectSort(DirectorySortOption.DEFAULT)
                }
            )
        }
    }
}

@Composable
private fun SortMenuOptionItem(
    label: String,
    selected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                ),
                color = if (selected) HayukRedDeep else CarbonBlack
            )
        },
        trailingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = HayukRed,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else {
            null
        },
        onClick = onClick,
        modifier = Modifier.testTag(testTag)
    )
}

@Composable
private fun DirectoryFeaturedBrandCard(
    brand: BrandConfig,
    popularityRank: Int?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOrderClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("directory_featured_card_${brand.brandId}"),
        shape = RoundedCornerShape(22.dp),
        color = CarbonBlack,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(CarbonBlack, EspressoDark, HayukRedDark)
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 104.dp, height = 124.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                when (brand.subCategory) {
                    BrandSubCategory.BEVERAGES -> {
                        if (brand.brandId == "kopi_kulango") {
                            KopiKulangoProductArtwork(modifier = Modifier.fillMaxSize())
                        } else {
                            ChilledDrinksCravingArtwork(modifier = Modifier.fillMaxSize())
                        }
                    }
                    BrandSubCategory.MAIN_COURSE -> FierySambalCravingArtwork(modifier = Modifier.fillMaxSize())
                    BrandSubCategory.SNACKS -> CrispyPlatterCravingArtwork(modifier = Modifier.fillMaxSize())
                }
                Surface(
                    color = PureWhite,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                ) {
                    Box(modifier = Modifier.padding(3.dp)) {
                        BrandLogoVisual(brand = brand, size = 34.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = HayukRed,
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                                tint = HayukLogoGold,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (popularityRank != null) {
                                    "#$popularityRank • ${stringResource(R.string.featured_brand_badge)}"
                                } else {
                                    stringResource(R.string.featured_brand_badge)
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = PureWhite
                            )
                        }
                    }

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("favorite_toggle_${brand.brandId}")
                    ) {
                        Icon(
                            imageVector = if (isFavorite) {
                                Icons.Rounded.Favorite
                            } else {
                                Icons.Rounded.FavoriteBorder
                            },
                            contentDescription = if (isFavorite) {
                                stringResource(R.string.cd_favorite_remove, brand.brandName)
                            } else {
                                stringResource(R.string.cd_favorite_add, brand.brandName)
                            },
                            tint = if (isFavorite) HayukRed else PureWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = brand.brandName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = PureWhite,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                val categoryLabel = when {
                    brand.brandId == "kopi_kulango" -> stringResource(R.string.flagship_brand_badge)
                    brand.subCategory == BrandSubCategory.MAIN_COURSE -> stringResource(R.string.featured_main_course_badge)
                    brand.subCategory == BrandSubCategory.SNACKS -> stringResource(R.string.featured_snacks_badge)
                    else -> stringResource(R.string.category_beverage)
                }
                Text(
                    text = categoryLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = HayukLogoGold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (brand.shortDescription.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = brand.shortDescription,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                        color = PureWhite.copy(alpha = 0.88f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onOrderClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PureWhite,
                        contentColor = CarbonBlack
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("order_brand_${brand.brandId}")
                ) {
                    Text(
                        text = stringResource(R.string.cta_order_here),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StandardBrandGridCard(
    brand: BrandConfig,
    popularityRank: Int?,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOrderClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(262.dp)
            .testTag("brand_card_${brand.brandId}"),
        shape = RoundedCornerShape(20.dp),
        color = PureWhite,
        border = BorderStroke(1.dp, HayukRedBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val baseCategoryLabel = if (brand.category == BrandCategory.FOOD) {
                    stringResource(R.string.category_food)
                } else {
                    stringResource(R.string.category_beverage)
                }
                Surface(
                    color = HayukRedSoft,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (popularityRank != null) {
                            "#$popularityRank • $baseCategoryLabel"
                        } else {
                            baseCategoryLabel
                        },
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = HayukRedDeep,
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("favorite_toggle_${brand.brandId}")
                ) {
                    Icon(
                        imageVector = if (isFavorite) {
                            Icons.Rounded.Favorite
                        } else {
                            Icons.Rounded.FavoriteBorder
                        },
                        contentDescription = if (isFavorite) {
                            stringResource(R.string.cd_favorite_remove, brand.brandName)
                        } else {
                            stringResource(R.string.cd_favorite_add, brand.brandName)
                        },
                        tint = if (isFavorite) HayukRed else SlateText,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            BrandLogoVisual(
                brand = brand,
                size = 72.dp
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = brand.brandName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp
                    ),
                    color = CarbonBlack,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (brand.shortDescription.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = brand.shortDescription,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        ),
                        color = SlateText,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Button(
                onClick = onOrderClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (brand.hasValidWebshopUrl) HayukRed else CarbonBlack,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("order_brand_${brand.brandId}")
            ) {
                Text(
                    text = stringResource(R.string.cta_order_here),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun EmptyBrandDirectoryState(
    searchQuery: String,
    selectedFilter: DirectoryFilter,
    onResetSearch: () -> Unit,
    onBrowseAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isSearchEmpty = searchQuery.isNotBlank()
    val isFavoritesEmpty = !isSearchEmpty && selectedFilter == DirectoryFilter.FAVORITES
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = when {
                isSearchEmpty -> Icons.Rounded.Search
                isFavoritesEmpty -> Icons.Rounded.FavoriteBorder
                else -> Icons.Rounded.Storefront
            },
            contentDescription = null,
            tint = HayukRed,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = when {
                isSearchEmpty -> stringResource(R.string.empty_search_title)
                isFavoritesEmpty -> stringResource(R.string.empty_favorites_title)
                else -> stringResource(R.string.empty_directory_title)
            },
            style = MaterialTheme.typography.titleMedium,
            color = CarbonBlack,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = when {
                isSearchEmpty -> stringResource(R.string.empty_search_subtitle, searchQuery.trim())
                isFavoritesEmpty -> stringResource(R.string.empty_favorites_subtitle)
                else -> stringResource(R.string.empty_directory_subtitle)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = SlateText,
            textAlign = TextAlign.Center
        )
        if (isSearchEmpty) {
            Spacer(modifier = Modifier.height(18.dp))
            OutlinedButton(
                onClick = onResetSearch,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, HayukRed),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("directory_reset_search_button")
            ) {
                Text(
                    text = stringResource(R.string.action_reset_search),
                    style = MaterialTheme.typography.labelMedium,
                    color = HayukRed
                )
            }
        } else if (isFavoritesEmpty) {
            Spacer(modifier = Modifier.height(18.dp))
            OutlinedButton(
                onClick = onBrowseAll,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, HayukRed),
                modifier = Modifier
                    .height(48.dp)
                    .testTag("directory_browse_all_button")
            ) {
                Text(
                    text = stringResource(R.string.action_browse_all),
                    style = MaterialTheme.typography.labelMedium,
                    color = HayukRed
                )
            }
        }
    }
}
