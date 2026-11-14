package com.example.ui

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BrandRepository
import com.example.model.BrandConfig
import com.example.model.DirectoryFilter
import com.example.model.DirectorySortOption
import com.example.model.FoodcourtUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val Context.hayukFavoritesStore by preferencesDataStore(name = "hayuk_favorites")

sealed interface WebshopActionEvent {
    data class OpenWebshopUrl(val brand: BrandConfig) : WebshopActionEvent
}

class FoodcourtViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BrandRepository(application.applicationContext)
    private val appContext = application.applicationContext
    private val favoritesKey = stringSetPreferencesKey("favorite_brand_ids")

    private val _uiState = MutableStateFlow(FoodcourtUiState())
    val uiState: StateFlow<FoodcourtUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<WebshopActionEvent>()
    val events: SharedFlow<WebshopActionEvent> = _events.asSharedFlow()

    init {
        loadBrandDirectory()
        observeSavedFavorites()
    }

    private fun observeSavedFavorites() {
        viewModelScope.launch {
            appContext.hayukFavoritesStore.data
                .map { prefs -> prefs[favoritesKey] ?: emptySet() }
                .collect { savedIds ->
                    _uiState.update { it.copy(favoriteBrandIds = savedIds) }
                }
        }
    }

    fun loadBrandDirectory() {
        val payload = repository.loadBrands()
        val visible = payload.brands.filter { it.displayStatus }
        val featured = visible.filter { it.featuredStatus }
            .ifEmpty { listOfNotNull(visible.firstOrNull()) }

        _uiState.update {
            it.copy(
                isLoading = false,
                groupName = payload.groupName,
                featuredBrands = featured,
                visibleBrands = visible
            )
        }
    }

    fun refreshBrands() {
        if (_uiState.value.isRefreshing) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val payload = withContext(Dispatchers.IO) {
                repository.loadBrands()
            }
            delay(450L)
            val visible = payload.brands.filter { it.displayStatus }
            val featured = visible.filter { it.featuredStatus }
                .ifEmpty { listOfNotNull(visible.firstOrNull()) }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isRefreshing = false,
                    groupName = payload.groupName,
                    featuredBrands = featured,
                    visibleBrands = visible
                )
            }
        }
    }

    fun selectFilter(filter: DirectoryFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
    }

    fun selectSort(sortOption: DirectorySortOption) {
        _uiState.update { it.copy(selectedSort = sortOption) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "", selectedFilter = DirectoryFilter.ALL) }
    }

    fun toggleFavorite(brandId: String) {
        val current = _uiState.value.favoriteBrandIds
        val updated = if (brandId in current) current - brandId else current + brandId
        _uiState.update { it.copy(favoriteBrandIds = updated) }
        viewModelScope.launch {
            appContext.hayukFavoritesStore.edit { prefs ->
                prefs[favoritesKey] = updated
            }
        }
    }

    fun onOrderBrandClicked(brand: BrandConfig) {
        if (brand.hasValidWebshopUrl) {
            viewModelScope.launch {
                _events.emit(WebshopActionEvent.OpenWebshopUrl(brand))
            }
        } else {
            _uiState.update { it.copy(comingSoonBrand = brand) }
        }
    }

    fun dismissComingSoonSheet() {
        _uiState.update { it.copy(comingSoonBrand = null) }
    }
}
