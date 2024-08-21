
# 🍹 DrinksTime

**DrinksTime** is a mobile application developed for Android using Kotlin. The app serves as a platform to compare offers and announcements from nearby bars based on the user's location, with a focus on coastal areas. Whether you're looking for the best deals, live music events, or just want to explore the local bar scene, DrinksTime is your go-to app for discovering and comparing what's on offer.

## 🎯 Project Overview

### Purpose

The primary goal of DrinksTime is to enhance the nightlife experience by providing users with up-to-date information on bars near their location, particularly in coastal regions. The app features filters to compare bars based on their proximity to the beach, and users can view details such as operating hours, promotions, live events, and menus. 

### Key Features

- **Location-Based Recommendations**: Find bars based on your current location, with a special focus on bars near the beach.
- **User Roles**: 
  - **Main Administrator**: Full control over the app's content and user management.
  - **Request Validator Administrator**: Validates business registration requests and their postings.
  - **Seller (Business Owner)**: Registers their business, posts time-limited offers, and manages their content.
- **CRUD Operations**: Create, read, update, and delete posts based on user roles.
- **Reporting**: Generate detailed reports on user activity, business performance, and more.

## 🛠️ Tech Stack

- **Kotlin**: The main programming language used for developing the application.
- **Android Studio**: The integrated development environment (IDE) used for Android app development.
- **Firebase**: Used for authentication, database management, and storage.
- **Google Maps API**: Integrated to provide location-based services.

## 🗃️ Database Schema

### Overview

The database is structured to efficiently manage user roles, business information, posts, and location data. Below is a high-level overview of the schema:

- **Users Table**: Stores user information including roles (Admin, Validator, Seller).
- **Businesses Table**: Contains business details like name, location, owner, and type.
- **Posts Table**: Manages posts made by businesses, including offers, events, and menus.
- **Locations Table**: Manages geographical data to link businesses with specific areas (e.g., beach zones).
- **Reports Table**: Stores generated reports for analysis and tracking.

### Implementation

- **Authentication**: Managed through Firebase Authentication, ensuring secure and scalable user management.
- **Real-time Database**: Firebase Realtime Database stores business listings, posts, and user interactions, allowing for real-time updates and queries.
- **Cloud Storage**: Used for storing images and other media files related to business posts.

## 📱 Application Views

### User Interface

- **Home Screen**: Displays location-based bar recommendations and promotions.
- **Search and Filter**: Allows users to search for bars by name, location, or specific filters (e.g., live music, proximity to the beach).
- **Bar Profile**: Detailed view of the bar, including operating hours, menu, current promotions, and upcoming events.
- **Admin Dashboard**: For administrators to manage user roles, validate business registrations, and review reports.
- **Seller Dashboard**: Allows business owners to create, update, and manage their posts.
- **Reports View**: For administrators to generate and view reports on the app's performance and user activities.

## 📊 Reporting

DrinksTime includes a robust reporting system that allows administrators to generate various reports, including:

- **User Activity Reports**: Track user engagement and interactions within the app.
- **Business Performance Reports**: Analyze how businesses are performing based on user ratings, post interactions, and sales metrics.
- **Post Analysis Reports**: Evaluate the success of promotions and offers based on user engagement.

## 📜 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## 👥 Team

Meet the team behind DrinksTime:

- **Axel Leonardo Sandoval Heredia** - [@AxelSandovalH](https://github.com/AxelSandovalH)
- **Marco Antonio García Gámez** - [@mggmz](https://github.com/mggmz)
- **Ramón Dalai Casillas Sánchez** - [@rcasillas2](https://github.com/rcasillas2)
- **Cesar Jared Cobián García** - [@Jared17-17](https://github.com/Jared17-17)
- **Víctor Manuel Aguirre** - [@gasperzitoh](https://github.com/gasperzitoh)
- **José Luis de Los Santos Sandoval** - [@joseluis-dls](https://github.com/joseluis-dls)

## 📬 Contact

For any inquiries or contributions, feel free to reach out to any of the team members through their GitHub profiles.
