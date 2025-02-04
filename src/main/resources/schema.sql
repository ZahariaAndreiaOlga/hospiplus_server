CREATE TABLE Role (
    id INT PRIMARY KEY AUTO_INCREMENT,
    roleName VARCHAR(255) NOT NULL
);

CREATE TABLE Category (
    id INT PRIMARY KEY AUTO_INCREMENT,
    catgName VARCHAR(255) NOT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME NOT NULL
);

CREATE TABLE `User` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    userName VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    userPassword TEXT NOT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME NOT NULL,
    roleId INT,
    FOREIGN KEY (roleId) REFERENCES Role(id)
);

CREATE TABLE `Order` (
    id INT PRIMARY KEY AUTO_INCREMENT,
    orderNum INT NOT NULL,
    orderedAt DATETIME NOT NULL,
    arrival DATETIME NOT NULL,
    quantity INT NOT NULL,
    pricePerProduct FLOAT NOT NULL,
    totalPrice FLOAT NOT NULL,
    userId INT,
    FOREIGN KEY (userId) REFERENCES `User`(id)
);

CREATE TABLE Payment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    payType VARCHAR(255) NOT NULL,
    orderId INT,
    FOREIGN KEY (orderId) REFERENCES `Order`(id)
);

CREATE TABLE Product (
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(5) NOT NULL,
    mu VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    criticalQuantity INT NOT NULL,
    pricePerUnit FLOAT NOT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME NOT NULL,
    categoryId INT,
    FOREIGN KEY (categoryId) REFERENCES Category(id)
);

CREATE TABLE ProductHistory (
    id INT PRIMARY KEY AUTO_INCREMENT,
    quantity INT NOT NULL,
    createdAt DATETIME NOT NULL,
    productId INT,
    FOREIGN KEY (productId) REFERENCES Product(id)
);

CREATE TABLE OrderedProduct (
    id INT PRIMARY KEY AUTO_INCREMENT,
    productId INT NOT NULL,
    orderId INT NOT NULL,
    quantity INT NOT NULL,
    pricePerUnit FLOAT NOT NULL,
    totalPrice FLOAT NOT NULL,
    FOREIGN KEY (productId) REFERENCES Product(id),
    FOREIGN KEY (orderId) REFERENCES `Order`(id)
);