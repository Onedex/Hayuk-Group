package com.example.data

import android.content.Context
import com.example.model.BrandCategory
import com.example.model.BrandConfig
import com.example.model.BrandSubCategory
import org.json.JSONObject

data class BrandDirectoryPayload(
    val groupName: String,
    val brands: List<BrandConfig>
)

class BrandRepository(private val context: Context) {

    fun loadBrands(): BrandDirectoryPayload {
        val rawJson = context.assets.open(CONFIG_FILE_NAME).bufferedReader().use { it.readText() }
        return parseBrandsJson(rawJson)
    }

    companion object {
        const val CONFIG_FILE_NAME = "brands_config.json"

        fun parseBrandsJson(rawJson: String): BrandDirectoryPayload {
            val root = JSONObject(rawJson)
            val groupName = root.optString("groupName", "Hayuk! Group")
            val jsonArray = root.getJSONArray("brands")
            val parsed = buildList {
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val categoryString = item.optString("category", "FOOD").uppercase()
                    val category = if (categoryString == "BEVERAGE") {
                        BrandCategory.BEVERAGE
                    } else {
                        BrandCategory.FOOD
                    }
                    val subCategoryString = item.optString("subCategory", "").uppercase()
                    val subCategory = when (subCategoryString) {
                        "MAIN_COURSE" -> BrandSubCategory.MAIN_COURSE
                        "SNACKS" -> BrandSubCategory.SNACKS
                        "BEVERAGES" -> BrandSubCategory.BEVERAGES
                        else -> if (category == BrandCategory.BEVERAGE) {
                            BrandSubCategory.BEVERAGES
                        } else {
                            BrandSubCategory.MAIN_COURSE
                        }
                    }
                    add(
                        BrandConfig(
                            brandId = item.getString("brandId"),
                            brandName = item.getString("brandName"),
                            shortDescription = item.optString("shortDescription", ""),
                            category = category,
                            subCategory = subCategory,
                            logo = item.optString("logo", ""),
                            monogram = item.optString("monogram", ""),
                            accentColorHex = item.optString("accentColor", "#E10B17"),
                            webshopUrl = item.optString("webshopUrl", ""),
                            displayStatus = item.optBoolean("displayStatus", true),
                            featuredStatus = item.optBoolean("featuredStatus", false),
                            popularityScore = item.optInt("popularityScore", 0)
                        )
                    )
                }
            }
            return BrandDirectoryPayload(groupName = groupName, brands = parsed)
        }
    }
}
