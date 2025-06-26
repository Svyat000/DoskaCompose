package com.sddrozdov.domain.models

import java.io.Serializable

data class Ad(
    val key: String? = null,
    val uid: String? = null,
    val title: String? = null,
    val description: String? = null,
    val price: String? = null,
    val country: String? = null,
    val city: String? = null,
    val category: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val postalCode: String? = null,
    val time: String = "0",

    val mainImage: String = "empty",
    val image2: String = "empty",
    val image3: String = "empty",

    var viewsCounter: String = "0",
    var emailsCounter: String = "0",
    var callsCounter: String = "0",
    var favoriteCounter: String = "0",

    var isFavorite: Boolean = false
) : Serializable