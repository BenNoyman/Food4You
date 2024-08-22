# Food4You

**Food4You** is a comprehensive Android application developed using Java and Android Studio. It provides a seamless food ordering experience by allowing users to explore various food categories, view details of each dish, and add items to their cart for ordering. The app integrates location-based services with Google Maps to recommend nearby restaurants and manage location data. Additional features include managing favorite foods, viewing order history, applying discounts, and more.

<img src="https://github.com/user-attachments/assets/72c2beff-8bf0-4614-a149-65515880ca73" alt="logo" width="200"/>
<img src="https://github.com/user-attachments/assets/1512338a-d589-426b-9838-067d4831d3a3" alt="logo2" width="200"/>


## Table of Contents

- [Features](#features)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Screenshots](#screenshots)
- [License](#license)

## Features

- 🛠️ **User Authentication**: Sign in and sign out functionality with Firebase authentication.
- 🍽️ **Browse Categories**: Explore various food categories and view detailed dish information.
- ⭐ **Best Foods**: Access top-rated or popular food items.
- ❤️ **Favorites**: Add and manage your favorite dishes for quick access.
- 🛒 **Order Management**: Add items to the cart, apply discounts via a coupon system, and place orders.
- 🗺️ **Location-Based Services**: Use Google Maps to fetch and display user's current location, and recommend nearby restaurants.
- 📜 **Order History**: View and review past orders with detailed breakdowns.
- 📱 **Responsive UI**: Optimized for various screen sizes.

## Usage

### 1. Launch the App

- **Splash Screen**: Initializes location services and checks for permissions.
- **Login Screen**: Redirects to the login screen after splash.

### 2. User Authentication

- **Log In**: Access main features with your credentials.
- **Sign Up**: Create an account if you’re a new user.

### 3. Main Activity

- **Browse Categories**: View and select food categories.
- **Best Foods**: Explore top-rated food items.
- **Location**: Update your location for restaurant recommendations.

### 4. Favorites

- **Add to Favorites**: Tap the heart icon to save dishes.
- **View Favorites**: Access from the menu.

### 5. Order Management

- **Add to Cart**: Add items and manage your cart.
- **Apply Discounts**: Use coupons at checkout.
- **Place Order**: Review cart and confirm delivery.

### 6. Order History

- **View Orders**: Access detailed information about past orders.

## Project Structure
The project is organized into several packages and classes, each serving a specific purpose. Below is a high-level overview of the structure:

### Activities

- **`SplashActivity`**: Initializes app, handles permissions.
- **`LoginActivity`**: Manages user authentication.
- **`MainActivity`**: Displays categories, best foods, and more.

### Adapters

- **`OrderHistoryAdapter`**: Displays past orders in a RecyclerView.
- **`InnerAdapter`**: Nested adapter for detailed order items.

### Helpers

- **`DataManager`**: Singleton for Firebase interactions and data retrieval.
- **`LocationManager`**: Manages location services and permissions.
- **`TinyDB`**: Utility for local data storage using `SharedPreferences` and Gson.

### Models

- **`Category`**: Represents food categories.
- **`Foods`**: Represents food items.
- **`Location`**: Represents location data.
- **`Price`**: Represents pricing details.
- **`Time`**: Represents time-related data.

### Utilities

- **`LocationManager`**: Handles location-based functionalities.

### Callbacks

- **`LocationUpdateCallback`**: Interface for location updates.
- **`CategoryCallback`**: Interface for category data.
- **`BestFoodCallback`**: Interface for best food items.
- **`FavoritesCallback`**: Interface for favorite foods.
- **`PriceCallback`**: Interface for pricing data.
- **`TimeCallback`**: Interface for time-related data.

### Resources

- **Firebase**: Utilizes Firebase Realtime Database for data storage and retrieval.

## Screenshots

![Food4You Screenshot](https://github.com/user-attachments/assets/8fe783de-ecc9-4ca4-92cb-ca3a877ff499)

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
