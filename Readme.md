# Nearby Restaurants Finder App - Android

## App Description

This Android application helps users find nearby restaurants using the MVVM architecture and Jetpack Compose. The app fetches restaurant data from the FourSquare API and displays it in a user-friendly manner, including pagination and detailed restaurant information. An app has the following features:

* **Find Nearby Restaurants**: Fetches and displays a list of nearby restaurants based on the user's current location.
*  **Restaurant List View**: Displays restaurants with images, names, and open/closed status (if available)
*  **Restaurant Detail View**: Shows detailed information about the selected restaurant


## Setup
1. Clone the Repository:

```
git clone https://github.com/abshah37/RestaurantFinder.git
cd nearby-restaurants-finder
```
2. Add API Key:
* Open ApiConstants.kt file located in 'app/src/main/java/com/restaurantfinderapp/utils/'
* Add your FourSquare API key as shown below:
```
object ApiConstants {
    const val FOURSQUARE_API_KEY = "YOUR_API_KEY_HERE"
}
```
3. Build and Run:
* Build the project and run it on an emulator or a physical device.

## Code Structure
* **Model**: Contains data classes representing the restaurant data.
* **ViewModel**:Handles the business logic and communicates with the repository.
* **View**: Comprises Jetpack Compose UI components.
* **Repository**: Manages data operations, including network requests to the FourSquare API.

