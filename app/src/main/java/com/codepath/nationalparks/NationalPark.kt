package com.codepath.nationalparks

import com.google.gson.annotations.SerializedName

/**
 * The Model for storing a single park from the National Parks API.
 */
class NationalPark {

    // Name field
    @JvmField
    @SerializedName("fullName")
    var name: String? = null

    // Description field
    @JvmField
    @SerializedName("description")
    var description: String? = null

    // Location or State field
    @JvmField
    @SerializedName("states")
    var location: String? = null

    // List of images from the API
    @SerializedName("images")
    var images: List<Image>? = null

    // Convenience property to get the first image URL
    val imageUrl: String? get() = images?.firstOrNull()?.url

    // Inner class representing a single image object
    class Image {
        @SerializedName("url")
        var url: String? = null
    }
}