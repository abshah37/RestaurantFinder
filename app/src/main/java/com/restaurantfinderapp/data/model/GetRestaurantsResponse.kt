package com.restaurantfinderapp.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class GetRestaurantsResponse(
    var results: ArrayList<Results> = arrayListOf(),
) {
    data class Results(
        var fsq_id: String? = null,
        var distance: Int? = null,
        var link: String? = null,
        var name: String? = null,
        var hours: Hours? = Hours(),
        var photos: ArrayList<Photos> = arrayListOf(),
        var rating : Double? = null
    )

    data class Photos(
        var id: String? = null,
        var createdAt: String? = null,
        var prefix: String? = null,
        var suffix: String? = null,
        var width: Int? = null,
        var height: Int? = null
    )

    data class Hours(
        var open_now: Boolean? = null,
    )

}
