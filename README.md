# Employee Management Android App

A modern Android application for managing employee information, built with Kotlin and following MVVM architecture pattern.

## Features

- View list of employees with their initials, names, and designations
- Add new employees with detailed information
- Edit existing employee details
- Delete employees with confirmation dialog
- Input validation for all fields
- Material Design UI components
- Persistent data storage using Room database

## Technical Details

### Architecture
- MVVM (Model-View-ViewModel) architecture
- Repository pattern for data operations
- LiveData for reactive UI updates
- Coroutines for asynchronous operations

### Libraries Used
- AndroidX Core KTX
- Room Database
- Material Design Components
- ViewModel and LiveData
- Kotlin Coroutines
- AndroidX Activity KTX

### Minimum Requirements
- Android 5.0 (API level 21) or higher
- Kotlin 1.8.0
- Gradle 8.0.2

## Setup Instructions

1. Clone the repository
```bash
git clone [repository-url]
```

2. Open the project in Android Studio

3. Sync project with Gradle files

4. Run the app on an emulator or physical device

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/employeemanagement/
│   │   │   ├── data/
│   │   │   │   ├── AppDatabase.kt
│   │   │   │   ├── Employee.kt
│   │   │   │   └── EmployeeDao.kt
│   │   │   ├── repository/
│   │   │   │   └── EmployeeRepository.kt
│   │   │   ├── ui/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── EmployeeListAdapter.kt
│   │   │   │   ├── AddEmployeeActivity.kt
│   │   │   │   └── EditEmployeeActivity.kt
│   │   │   └── viewmodel/
│   │   │       └── EmployeeViewModel.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   └── drawable/
│   │   └── AndroidManifest.xml
│   └── test/
└── build.gradle
```

## Testing

The app includes basic unit tests and can be extended with:
- UI tests using Espresso
- Unit tests for ViewModel
- Database tests for Room

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details

## Acknowledgments

- Android Jetpack components
- Material Design guidelines
- Room persistence library 