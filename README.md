# Ordering Application

## Description

This Android application is designed for visitors who wish to add orders for various products for each customer in offline mode. The application uses **Room Database** for local storage and follows the **DDP (Data, Domain, Presentation)** architecture with **MVVM** (Model-View-ViewModel) pattern. The application adheres to **SOLID** principles, ensuring maintainable and scalable code.

## Features

### 1. Customers
The customer management feature allows users to handle customer data such as name, mobile, and creation timestamp. It provides the following functionality:

#### 1.1. Create
- Users can create new customers by entering their name, mobile number, and other relevant details.

#### 1.2. Edit
- Users can modify customer information, including their name, mobile, and creation date.

#### 1.3. List
- Users can view a list of all customers added to the database.

#### 1.4. Delete
- Users can delete a customer from the database.

---

### 2. Products
Products can be added to the orders. Each product has a name, code, and created timestamp. The following features are available:

#### 2.1. Create
- Users can create new products by entering their name, product code, and other details.

#### 2.2. Edit
- Users can update product details such as name, code, and creation date.

#### 2.3. List
- Users can view a list of all products added to the system.

#### 2.4. Delete
- Users can delete products from the system.

---

### 3. Orders
This feature handles the creation, editing, listing, and deletion of orders.

#### 3.1. Create
- Orders can be created by selecting a customer and adding products to the order. The user can specify the quantity of each product in the order.

#### 3.2. Edit
- Orders can be edited, including changing customer information and updating the list of products and their quantities.

#### 3.3. List
- A list of all orders is displayed, including a count of the products in each order.

#### 3.4. Delete
- Users can delete an order from the system.

---

## Order Creation Flow

To create an order, users should follow these steps:

1. **Select Customer**: Choose the customer from the available list.
2. **Add Products**: In the products page, users can select products and set their quantity by clicking the "Add to Basket" button.
3. **Edit Cart**: Users can view the shopping cart, where they can edit the quantity of products or remove products from the cart.
4. **Preview and Save**: After finalizing the cart, users can preview the order details and save the order.

---

## Architecture

### 1. Data Layer (Room Database)
- Room is used to persist the data locally for customers, products, and orders.
- **DAO** interfaces are used to interact with the database for CRUD operations (Create, Read, Update, Delete).

### 2. Domain Layer
- Contains business logic that is decoupled from the presentation and data layers.
- Defines use cases and interacts with the repository to fetch or save data.

### 3. Presentation Layer
- MVVM (Model-View-ViewModel) is followed to separate the logic.
- **ViewModel** interacts with the domain layer and provides data to the **View**.
- **Flow** is used to observe data changes asynchronously and update the UI accordingly.

---

## SOLID Principles

The application follows the **SOLID** principles to ensure maintainable and flexible code:

- **S**: Single Responsibility Principle (SRP) - Each class has one responsibility.
- **O**: Open/Closed Principle (OCP) - The system is open for extension but closed for modification.
- **L**: Liskov Substitution Principle (LSP) - Derived classes can be used interchangeably with their base class without altering the correctness.
- **I**: Interface Segregation Principle (ISP) - The application uses specific interfaces to ensure that classes are not forced to implement unnecessary methods.
- **D**: Dependency Inversion Principle (DIP) - High-level modules do not depend on low-level modules, but both depend on abstractions.

---

## Dependencies

- **Room**: For local database storage.
- **Flow**: For observing and handling asynchronous data streams.
- **Data Binding**: To bind UI components with data in the ViewModel.

---

## Setup Instructions

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or real device.
4. Ensure all dependencies are synced properly by allowing Gradle to build the project.

---

## Contributing

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Write unit tests for new features or bug fixes.
4. Submit a pull request with a clear description of the changes.

---

## License

This project is licensed under the MIT License.

---