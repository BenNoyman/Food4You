# Food4You

**Food4You** is a food ordering Android application that allows users to explore various categories of food, view details of each dish, add items to their cart, and place orders. The app also includes features like managing favorite foods, viewing order history, and locating nearby restaurants. This project is built using Java and Firebase.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Screenshots](#screenshots)
- [License](#license)

## Features

- **User Authentication**: Sign in and sign out functionality.
- **Browse Categories**: Explore different food categories.
- **Best Foods**: View and order from a list of top-rated foods.
- **Favorites**: Add and manage favorite dishes.
- **Order History**: View previous orders with detailed breakdowns.
- **Location-Based Services**: Fetch user's current location for nearby restaurant recommendations.
- **Order Management**: Add items to the cart, apply discounts, and place orders.
- **Responsive UI**: User-friendly interface optimized for various screen sizes.

## Usage

Once you have the app installed and running, here's how you can use it:

1. **Launch the App**:
   - The app starts with a splash screen that initializes location services and checks for any required permissions.
   - After the splash screen, you are directed to the login screen.

2. **User Authentication**:
   - Log in with your credentials to access the app's main features.
   - If it's your first time using the app, you'll need to sign up.

3. **Main Activity**:
   - **Browse Categories**: On the main screen, you can see a list of food categories. Tap on any category to view the items available within it.
   - **Best Foods**: A dedicated section showcasing top-rated or popular food items.
   - **Location**: The app uses your current location to suggest nearby restaurants or offers. You can update your location through the settings.

4. **Favorites**:
   - You can add any dish to your favorites by tapping the heart icon. Access your favorites from the menu to quickly view and order your preferred dishes.

5. **Order Management**:
   - **Add to Cart**: Browse through food items and add them to your cart.
   - **Apply Discounts**: If you have a coupon, you can apply it at checkout to get a discount.
   - **Place Order**: Review your cart, confirm your delivery details, and place your order.

6. **Order History**:
   - View all your previous orders in the "Order History" section. Tap on any past order to see detailed information about the items, prices, and delivery status.

## Project Structure

The project is organized into several packages and classes, each serving a specific purpose. Below is a high-level overview of the structure:

### Activities
- **SplashActivity**: Handles the initialization of the app, including location permissions and transitioning to the login screen.
- **LoginActivity**: Manages user authentication, allowing users to log in or sign up.
- **MainActivity**: The core activity that displays categories, best foods, and access to other features like favorites and order history.

### Adapters
- **OrderHistoryAdapter**: Adapter for displaying a list of past orders in a RecyclerView.
- **InnerAdapter**: Nested adapter used within the `OrderHistoryAdapter` to display detailed items for each order.

### Helpers
- **DataManager**: A Singleton class responsible for interacting with Firebase. It fetches data like categories, best foods, and more, and provides it to other components of the app.
- **LocationManager**: Manages location services, including requesting location permissions, fetching the current location, and handling location updates.
- **TinyDB**: A utility class that simplifies data storage using `SharedPreferences`. It uses Gson for serializing and deserializing complex objects like lists.

### Models
- **Category**: Represents a food category.
- **Foods**: Represents a food item, including attributes like name, price, description, and whether it is a favorite or best food.
- **Location**: Represents a location entity, including attributes like latitude, longitude, and address.
- **Price**: Represents pricing details for food items.
- **Time**: Represents time-related data for ordering and delivery.

### Utilities
- **LocationManager**: A comprehensive utility for handling all location-based functionalities in the app, such as requesting permissions and retrieving the user's current location.

### Callbacks
- **LocationUpdateCallback**: Interface for receiving location updates.
- **CategoryCallback**: Interface for receiving data about food categories.
- **BestFoodCallback**: Interface for receiving data about the best food items.
- **FavoritesCallback**: Interface for receiving data about the user's favorite foods.
- **PriceCallback**: Interface for receiving data about food pricing.
- **TimeCallback**: Interface for receiving time-related data.

### Resources
- **Firebase**: The app uses Firebase Realtime Database to store and retrieve data such as food categories, food items, user data, and order history.

### Screenshots
![image](https://github.com/user-attachments/assets/8fe783de-ecc9-4ca4-92cb-ca3a877ff499)


## Installation

To set up this project locally:

1. **Clone the repository:**

   ```bash
   git clone https://github.com/yourusername/Food4You.git
