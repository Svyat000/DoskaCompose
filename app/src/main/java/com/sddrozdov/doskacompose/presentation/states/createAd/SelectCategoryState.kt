package com.sddrozdov.doskacompose.presentation.states.createAd

import com.sddrozdov.doskacompose.domain.models.Category

data class SelectCategoryState(
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategoryId: Long? = null
)

sealed class SelectCategoryEvent {
    data class OnCategorySelected(val categoryId: Long) : SelectCategoryEvent()
}