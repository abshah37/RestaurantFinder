package com.restaurantfinderapp.data.model

data class RestaurantDetailResponse(
    var fsq_id: String? = null,
    var link: String? = null,
    var location: Location? = Location(),
    var name: String? = null,
    var photos: ArrayList<Photos> = arrayListOf(),
    var rating: Double? = null,
    var geocodes : Geocodes? = Geocodes(),
    var hours : Hours? = Hours(),
    var stats: Stats? = Stats()

) {

    data class Location(
        var address: String? = null,
        var country: String? = null,
        var crossStreet: String? = null,
        var formatted_address: String? = null,
        var locality: String? = null,
        var postcode: String? = null,
        var region: String? = null
    )

    data class Photos(
        var id: String? = null,
        var createdAt: String? = null,
        var prefix: String? = null,
        var suffix: String? = null,
        var width: Int? = null,
        var height: Int? = null
    )

    data class Stats(
        var total_photos: Int? = null,
        var total_ratings: Int? = null,
        var total_tips: Int? = null
    )

    data class Hours(
        var open_now: Boolean? = null,
    )

    data class Geocodes (
        var main : Main? = Main()
    )

    data class Main (
        var latitude  : Double? = null,
        var longitude : Double? = null
    )
}