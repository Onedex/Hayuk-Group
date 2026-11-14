package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.BrandRepository
import com.example.model.BrandCategory
import com.example.model.BrandSubCategory
import com.example.model.DirectoryFilter
import com.example.model.FoodcourtUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `app name matches Hayuk branding`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Hayuk!", appName)
    }

    @Test
    fun `centralized config loads 28 brands with 3 featured brands across beverage food and snack`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val payload = BrandRepository(context).loadBrands()

        assertEquals(28, payload.brands.size)
        assertEquals(22, payload.brands.count { it.category == BrandCategory.FOOD })
        assertEquals(6, payload.brands.count { it.category == BrandCategory.BEVERAGE })

        val featured = payload.brands.filter { it.featuredStatus }
        assertEquals(3, featured.size)
        assertNotNull(featured.firstOrNull { it.subCategory == BrandSubCategory.BEVERAGES })
        assertNotNull(featured.firstOrNull { it.subCategory == BrandSubCategory.MAIN_COURSE })
        assertNotNull(featured.firstOrNull { it.subCategory == BrandSubCategory.SNACKS })
    }

    @Test
    fun `ui state filters food beverage main course and snacks with featured brands`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val brands = BrandRepository(context).loadBrands().brands
        val featured = brands.filter { it.featuredStatus }

        val allState = FoodcourtUiState(
            isLoading = false,
            featuredBrands = featured,
            visibleBrands = brands,
            selectedFilter = DirectoryFilter.ALL
        )
        assertEquals(28, allState.totalCount)
        assertEquals("Kopi Kulango", allState.flagshipBrand?.brandName)
        assertEquals(2, allState.secondaryFeaturedBrands.size)
        assertEquals(3, allState.filteredFeaturedBrands.size)
        assertEquals(25, allState.filteredStandardBrands.size)

        val foodState = allState.copy(selectedFilter = DirectoryFilter.FOOD)
        assertEquals(2, foodState.filteredFeaturedBrands.size)
        assertEquals(20, foodState.filteredStandardBrands.size)

        val mainCourseState = allState.copy(selectedFilter = DirectoryFilter.MAIN_COURSE)
        assertEquals(1, mainCourseState.filteredFeaturedBrands.size)
        assertEquals("Penyetan Cak Suro Sambal Surabaya", mainCourseState.filteredFeaturedBrands.first().brandName)
        assertEquals(14, mainCourseState.filteredStandardBrands.size)

        val snacksState = allState.copy(selectedFilter = DirectoryFilter.SNACKS)
        assertEquals(1, snacksState.filteredFeaturedBrands.size)
        assertEquals("Kriuk Bro! Chicken", snacksState.filteredFeaturedBrands.first().brandName)
        assertEquals(6, snacksState.filteredStandardBrands.size)

        val beverageState = allState.copy(selectedFilter = DirectoryFilter.BEVERAGES)
        assertEquals(1, beverageState.filteredFeaturedBrands.size)
        assertEquals("Kopi Kulango", beverageState.filteredFeaturedBrands.first().brandName)
        assertEquals(5, beverageState.filteredStandardBrands.size)
    }

    @Test
    fun `webshop url validation and short descriptions are configured on all brands`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val brands = BrandRepository(context).loadBrands().brands
        assertTrue(brands.all { it.shortDescription.isNotBlank() })
        assertTrue(brands.all { it.hasValidWebshopUrl })
        assertEquals(
            "https://lemonsquarebakerytreats.klikit.io/menu/brand/4804",
            brands.first().webshopUrl
        )

        val unconfigured = brands.first().copy(webshopUrl = "")
        assertFalse(unconfigured.hasValidWebshopUrl)
    }

    @Test
    fun `search query filters brands by name case-insensitively`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val brands = BrandRepository(context).loadBrands().brands
        val featured = brands.filter { it.featuredStatus }

        val baseState = FoodcourtUiState(
            isLoading = false,
            featuredBrands = featured,
            visibleBrands = brands,
            selectedFilter = DirectoryFilter.ALL
        )

        val bebekSearch = baseState.copy(searchQuery = "bebek")
        assertEquals(0, bebekSearch.filteredFeaturedBrands.size)
        assertEquals(4, bebekSearch.filteredStandardBrands.size)

        val kopiSearch = baseState.copy(searchQuery = "  KULANGO ")
        assertEquals(1, kopiSearch.filteredFeaturedBrands.size)
        assertEquals("Kopi Kulango", kopiSearch.filteredFeaturedBrands.first().brandName)
        assertEquals(0, kopiSearch.filteredStandardBrands.size)

        val pizzaSearch = baseState.copy(searchQuery = "pizza")
        assertEquals(0, pizzaSearch.filteredFeaturedBrands.size)
        assertEquals(1, pizzaSearch.filteredStandardBrands.size)
        assertEquals("Mama's Pizza - Jagir", pizzaSearch.filteredStandardBrands.first().brandName)

        val noMatch = baseState.copy(searchQuery = "sushi")
        assertEquals(0, noMatch.filteredFeaturedBrands.size)
        assertEquals(0, noMatch.filteredStandardBrands.size)
    }

    @Test
    fun `favorites filter isolates saved favorite brands`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val brands = BrandRepository(context).loadBrands().brands
        val featured = brands.filter { it.featuredStatus }

        val stateWithFavorites = FoodcourtUiState(
            isLoading = false,
            featuredBrands = featured,
            visibleBrands = brands,
            favoriteBrandIds = setOf("kopi_kulango", "mamas_pizza_jagir"),
            selectedFilter = DirectoryFilter.FAVORITES
        )

        assertEquals(2, stateWithFavorites.favoritesCount)
        assertTrue(stateWithFavorites.isFavorite("kopi_kulango"))
        assertFalse(stateWithFavorites.isFavorite("chocorush"))
        assertEquals(1, stateWithFavorites.filteredFeaturedBrands.size)
        assertEquals("Kopi Kulango", stateWithFavorites.filteredFeaturedBrands.first().brandName)
        assertEquals(1, stateWithFavorites.filteredStandardBrands.size)
        assertEquals("Mama's Pizza - Jagir", stateWithFavorites.filteredStandardBrands.first().brandName)
    }

    @Test
    fun `findBrandById resolves brand for in-app webshop screen`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val brands = BrandRepository(context).loadBrands().brands
        val state = FoodcourtUiState(
            isLoading = false,
            featuredBrands = brands.filter { it.featuredStatus },
            visibleBrands = brands
        )

        val kopi = state.findBrandById("kopi_kulango")
        assertNotNull(kopi)
        assertEquals("Kopi Kulango", kopi?.brandName)
        assertTrue(kopi?.hasValidWebshopUrl == true)
        assertEquals(null, state.findBrandById("non_existent_brand"))
    }

    @Test
    fun `viewmodel refreshBrands reloads brand directory and preserves filter state`() = kotlinx.coroutines.runBlocking {
        val application = ApplicationProvider.getApplicationContext<android.app.Application>()
        val viewModel = com.example.ui.FoodcourtViewModel(application)
        viewModel.selectFilter(DirectoryFilter.SNACKS)
        viewModel.updateSearchQuery("Kriuk")

        viewModel.refreshBrands()

        val start = System.currentTimeMillis()
        while (viewModel.uiState.value.isRefreshing && System.currentTimeMillis() - start < 3000L) {
            org.robolectric.shadows.ShadowLooper.idleMainLooper(100L, java.util.concurrent.TimeUnit.MILLISECONDS)
            Thread.sleep(20L)
        }
        org.robolectric.shadows.ShadowLooper.idleMainLooper()

        val state = viewModel.uiState.value
        assertFalse(state.isRefreshing)
        assertFalse(state.isLoading)
        assertEquals(28, state.totalCount)
        assertEquals(DirectoryFilter.SNACKS, state.selectedFilter)
        assertEquals("Kriuk", state.searchQuery)
        assertEquals(1, state.filteredFeaturedBrands.size)
    }

    @Test
    fun `popularity and alphabetical sorting order brands accurately`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val brands = BrandRepository(context).loadBrands().brands
        val featured = brands.filter { it.featuredStatus }

        assertTrue(brands.all { it.popularityScore > 0 })

        val popularityState = FoodcourtUiState(
            isLoading = false,
            featuredBrands = featured,
            visibleBrands = brands,
            selectedFilter = DirectoryFilter.ALL,
            selectedSort = com.example.model.DirectorySortOption.POPULARITY
        )

        val sortedFeatured = popularityState.filteredFeaturedBrands
        assertEquals("Kopi Kulango", sortedFeatured.first().brandName)
        assertTrue(sortedFeatured.zipWithNext().all { (a, b) -> a.popularityScore >= b.popularityScore })

        val sortedStandard = popularityState.filteredStandardBrands
        assertEquals("Geprek Sarasa Jagir", sortedStandard.first().brandName)
        assertTrue(sortedStandard.zipWithNext().all { (a, b) -> a.popularityScore >= b.popularityScore })

        val nameSortedState = popularityState.copy(
            selectedSort = com.example.model.DirectorySortOption.NAME_ASC
        )
        assertEquals("Al-Jazeera! Kebab, Burger & Martabak", nameSortedState.filteredStandardBrands.first().brandName)
    }
}
