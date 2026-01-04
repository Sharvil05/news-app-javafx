# 📰 JavaFX News Application

A JavaFX-based desktop News Application that allows users to securely sign up and log in using Firebase Authentication and read real-time news fetched from NewsAPI.
A JavaFX-based News Application with Firebase Authentication and NewsAPI integration, featuring secure login/signup, category-wise news browsing, multithreaded API calls, and a clean MVC-based UI.
The application follows MVC architecture**, uses multithreading for smooth performance, and provides a clean, modern UI.

## 🚀 Features

* 🔐 Secure Login & Signup using Firebase Authentication
* 📰 Live News fetching using NewsAPI
* 📂 Category-wise news (Business, Sports, Health, Science, etc.)
* 🌐 Read full articles inside the app using WebView
* ⚡ Multithreaded API calls for responsive UI
* 🎨 Modern JavaFX UI with animations and hover effects
* 🔁 Logout with confirmation dialog
* 🧱 MVC architecture for clean code structure

---

## 🛠️ Tech Stack

* Language: Java
* UI Framework: JavaFX
* Authentication: Firebase Authentication (REST API)
* News Data: NewsAPI
* JSON Parsing: org.json
* Build Tool: Maven
* Architecture: MVC (Model–View–Controller)

---

## 📁 Project Structure

```
news-app-javafx
│
├── src/main/java
│   ├── com.newsproject.view        # JavaFX UI (Views)
│   ├── com.newsproject.controller  # Business Logic & API Handling
│
├── src/main/resources
│   ├── bg_image.png                # Background image
│
├── pom.xml                          # Maven dependencies
└── README.md
```

---

## ▶️ How to Run the Project

1. Clone the repository:

```bash
git clone https://github.com/your-username/news-app-javafx.git
```

2. Open the project in VS Code / IntelliJ

3. Make sure:

   * Java 17+ is installed
   * JavaFX is properly configured

4. Add your:

   * Firebase API Key
   * NewsAPI Key

5. Run the `MainApp` class

---

## 🔐 Authentication Flow

* User signs up or logs in using email & password
* Firebase validates credentials
* On success, user is redirected to the News Dashboard
* User must log out to return to login page

---

## 🧵 Multithreading

* API calls are executed using `Task` and background threads
* UI updates are handled using `Platform.runLater()`
* Ensures smooth and responsive UI

---

## 📌 Future Improvements

* 🔑 Forgot password email using Firebase
* 🌙 Dark mode support
* 📱 Mobile version using Flutter
* 💾 Offline caching of news
* 👨‍💼 Admin dashboard

---

## 👨‍💻 Author

**Sharvil Punekar**
Java | JavaFX | Firebase | REST API



