package com.example.model

enum class BrandCategory {
    FOOD,
    BEVERAGE
}

enum class BrandSubCategory {
    MAIN_COURSE,
    SNACKS,
    BEVERAGES
}

enum class DirectoryFilter {
    ALL,
    FAVORITES,
    FOOD,
    MAIN_COURSE,
    SNACKS,
    BEVERAGES
}

enum class DirectorySortOption {
    DEFAULT,
    POPULARITY,
    NAME_ASC
}

data class BrandConfig(
    val brandId: String,
    val brandName: String,
    val shortDescription: String,
    val category: BrandCategory,
    val subCategory: BrandSubCategory,
    val logo: String,
    val monogram: String,
    val accentColorHex: String,
    val webshopUrl: String,
    val displayStatus: Boolean,
    val featuredStatus: Boolean,
    val popularityScore: Int = 0
) {
    val hasValidWebshopUrl: Boolean
        get() {
            val trimmed = webshopUrl.trim()
            return trimmed.startsWith("https://", ignoreCase = true) ||
                trimmed.startsWith("http://", ignoreCase = true)
        }
}

data class FoodcourtUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val groupName: String = "Hayuk! Group",
    val featuredBrands: List<BrandConfig> = emptyList(),
    val visibleBrands: List<BrandConfig> = emptyList(),
    val favoriteBrandIds: Set<String> = emptySet(),
    val selectedFilter: DirectoryFilter = DirectoryFilter.ALL,
    val selectedSort: DirectorySortOption = DirectorySortOption.DEFAULT,
    val searchQuery: String = "",
    val comingSoonBrand: BrandConfig? = null
) {
    val flagshipBrand: BrandConfig?
        get() = featuredBrands.firstOrNull { it.subCategory == BrandSubCategory.BEVERAGES }
            ?: featuredBrands.firstOrNull()

    val secondaryFeaturedBrands: List<BrandConfig>
        get() {
            val flagshipId = flagshipBrand?.brandId
            return featuredBrands.filter { it.brandId != flagshipId }
        }

    val favoritesCount: Int
        get() = visibleBrands.count { it.brandId in favoriteBrandIds }

    val foodCount: Int
        get() = visibleBrands.count { it.category == BrandCategory.FOOD }

    val mainCourseCount: Int
        get() = visibleBrands.count { it.subCategory == BrandSubCategory.MAIN_COURSE }

    val snacksCount: Int
        get() = visibleBrands.count { it.subCategory == BrandSubCategory.SNACKS }

    val beveragesCount: Int
        get() = visibleBrands.count { it.subCategory == BrandSubCategory.BEVERAGES }

    val totalCount: Int
        get() = visibleBrands.size

    fun isFavorite(brandId: String): Boolean = brandId in favoriteBrandIds

    fun findBrandById(brandId: String): BrandConfig? =
        visibleBrands.firstOrNull { it.brandId == brandId }

    private fun BrandConfig.matchesSearch(): Boolean {
        val query = searchQuery.trim()
        return query.isEmpty() || brandName.contains(query, ignoreCase = true)
    }

    private fun BrandConfig.matchesFilter(): Boolean {
        return when (selectedFilter) {
            DirectoryFilter.ALL -> true
            DirectoryFilter.FAVORITES -> brandId in favoriteBrandIds
            DirectoryFilter.FOOD -> category == BrandCategory.FOOD
            DirectoryFilter.MAIN_COURSE -> subCategory == BrandSubCategory.MAIN_COURSE
            DirectoryFilter.SNACKS -> subCategory == BrandSubCategory.SNACKS
            DirectoryFilter.BEVERAGES -> subCategory == BrandSubCategory.BEVERAGES
        }
    }

    private fun List<BrandConfig>.applySort(): List<BrandConfig> {
        return when (selectedSort) {
            DirectorySortOption.DEFAULT -> this
            DirectorySortOption.POPULARITY -> sortedByDescending { it.popularityScore }
            DirectorySortOption.NAME_ASC -> sortedBy { it.brandName.lowercase() }
        }
    }

    val filteredFeaturedBrands: List<BrandConfig>
        get() = featuredBrands
            .filter { it.displayStatus && it.matchesSearch() && it.matchesFilter() }
            .applySort()

    val filteredStandardBrands: List<BrandConfig>
        get() {
            val featuredIds = filteredFeaturedBrands.map { it.brandId }.toSet()
            return visibleBrands
                .filter { brand ->
                    brand.matchesFilter() && brand.matchesSearch() && brand.brandId !in featuredIds
                }
                .applySort()
        }
}
