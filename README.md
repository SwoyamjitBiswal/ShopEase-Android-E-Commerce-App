# ShopEase: A Feature-Rich E-Commerce Android App

ShopEase is a complete, modern e-commerce application for Android, built with Kotlin and using a robust, hybrid data model. It provides a full suite of features for both customers and administrators, demonstrating a professional, scalable, and offline-first architecture.

## Core Features

### Customer-Facing App
- **Dynamic Home Page:** Displays a curated list of products, including categories, "Deals of the Day," and "New Arrivals."
- **Product Catalog:** Users can browse the full product catalog with a grid layout and search functionality.
- **Product Detail:** A detailed view for each product, showing its name, price, rating, user reviews, and a full description.
- **Shopping Cart:** A fully functional shopping cart that allows users to add, remove, and change the quantity of items.
- **Wishlist:** Users can add products to a personal wishlist for later.
- **Seamless Checkout:** A multi-step checkout process that includes a payment screen and an "Order Successful" confirmation.
- **Order History:** Users can view a complete list of all their past orders.
- **User Authentication:** A complete authentication system that supports both email/password and Google Sign-In via Firebase Authentication.
- **User Profile:** A dedicated profile screen where users can view their details, manage shipping addresses, and log out.

### Admin Panel
- **Secure Admin Login:** A separate, password-protected login for administrators.
    - **Admin Password:** `@Prince123`
- **Product Management:** A full suite of CRUD (Create, Read, Update, Delete) operations for managing the product catalog. Admins can add new products, edit existing ones, and remove products from the store.

## Technical Architecture

ShopEase is built with a modern, professional, and robust technical architecture designed for scalability and a great user experience.

### Hybrid Data Model (Offline-First)
The app uses a hybrid data model that combines the speed of a local Room database with the real-time power of Firebase.

- **Local Database (Room):** The UI is powered by a local Room database, which makes the app incredibly fast, responsive, and fully functional even when the user is offline.
- **Online Backend (Firebase):** Firebase serves as the single source of truth for all data.
    - **Firebase Realtime Database:** Stores the product catalog, user carts, wishlists, orders, shipping addresses, and product reviews.
    - **Firebase Authentication:** Handles all user authentication and account management.
- **Real-time Synchronization:** The app uses a central `ShoppingRepository` that listens for changes in Firebase in real-time (using `ValueEventListener` and `AuthStateListener`) and automatically updates the local Room database. This ensures that data is always fresh and synced across all of a user's devices.

### Key Technologies & Libraries
- **Language:** [Kotlin](https://kotlinlang.org/)
- **Architecture:** Model-View-ViewModel (MVVM)
- **UI:** Android XML with `ConstraintLayout` & Material Design 3 components.
- **Asynchronous Programming:** Kotlin Coroutines & Flow for managing background threads and reactive data streams.
- **Local Database:** [Android Room Persistence Library](https://developer.android.com/training/data-storage/room) (`androidx.room:room-ktx:2.6.1`)
- **Backend & Sync:** 
    - [Firebase Realtime Database](https://firebase.google.com/docs/database) (`com.google.firebase:firebase-database`)
    - [Firebase Authentication](https://firebase.google.com/docs/auth) (`com.google.firebase:firebase-auth-ktx`)
    - [Google Sign-In](https://developers.google.com/identity/sign-in/android/start) (`com.google.android.gms:play-services-auth:21.0.0`)
- **Dependency Injection:** A custom, manual dependency injection container (`AppDataContainer`).
- **Image Loading:** [Glide](https://github.com/bumptech/glide) (`com.github.bumptech.glide:glide:4.12.0`)
- **UI Libraries:**
    - [Material Components for Android](https://material.io/develop/android/docs/getting-started) (`com.google.android.material:material:1.11.0`)
    - [CircleImageView](https://github.com/hdodenhof/CircleImageView) (`de.hdodenhof:circleimageview:3.1.0`)
