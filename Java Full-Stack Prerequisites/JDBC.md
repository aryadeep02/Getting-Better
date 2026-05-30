# JDBC (Java Database Connectivity) - Complete Notes

## What is JDBC?

JDBC (Java Database Connectivity) is a Java API that enables Java applications to communicate with relational databases such as MySQL, PostgreSQL, Oracle, SQL Server, etc.

It acts as a bridge between a Java application and a database.

```
Java Application
       ↓
     JDBC
       ↓
 JDBC Driver
       ↓
   Database
```

---

# Why JDBC?

* Provides a standard way to access databases.
* Makes Java applications database-independent.
* Allows execution of SQL queries from Java code.
* Supports CRUD operations.
* Forms the foundation of Hibernate and Spring Data JPA.

---

# JDBC Architecture

### 1. Java Application

The program written by the developer.

### 2. JDBC API

Provides interfaces and classes for database interaction.

### 3. JDBC Driver

Converts JDBC calls into database-specific commands.

### 4. Database

Stores and manages application data.

---

# JDBC Workflow

Every JDBC application follows these steps:

1. Load Driver
2. Create Connection
3. Create Statement
4. Execute Query
5. Process Result
6. Close Resources

```
Load Driver
     ↓
Create Connection
     ↓
Create Statement
     ↓
Execute SQL
     ↓
Process Result
     ↓
Close Resources
```

---

# Loading JDBC Driver

Older approach:

```java
Class.forName("com.mysql.cj.jdbc.Driver");
```

Modern JDBC automatically loads the driver if the dependency exists.

---

# Creating a Database Connection

```java
Connection conn = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studentdb",
        "root",
        "password");
```

### URL Breakdown

```
jdbc:mysql://localhost:3306/studentdb
```

| Part      | Meaning       |
| --------- | ------------- |
| jdbc      | JDBC Protocol |
| mysql     | Database Type |
| localhost | Host          |
| 3306      | Port Number   |
| studentdb | Database Name |

---

# Connection Interface

Represents an active connection to a database.

```java
Connection conn;
```

Common Methods:

```java
conn.commit();
conn.rollback();
conn.close();
conn.prepareStatement();
conn.createStatement();
```

---

# Statement Interface

Used to execute SQL queries.

```java
Statement stmt = conn.createStatement();
```

Example:

```java
ResultSet rs = stmt.executeQuery(
        "SELECT * FROM students");
```

---

# ResultSet

Stores the data returned by a SELECT query.

```java
ResultSet rs = stmt.executeQuery(
        "SELECT * FROM students");
```

### Traversing ResultSet

```java
while(rs.next()) {
    System.out.println(rs.getInt("id"));
    System.out.println(rs.getString("name"));
}
```

---

# Complete JDBC Example

```java
import java.sql.*;

public class JdbcDemo {

    public static void main(String[] args) throws Exception {

        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/studentdb",
                "root",
                "password");

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(
                "SELECT * FROM students");

        while(rs.next()) {
            System.out.println(rs.getInt("id"));
            System.out.println(rs.getString("name"));
        }

        conn.close();
    }
}
```

---

# CRUD Operations

CRUD = Create, Read, Update, Delete

---

## INSERT

```java
String sql =
"INSERT INTO students VALUES(1,'Aryadeep')";

stmt.executeUpdate(sql);
```

---

## SELECT

```java
ResultSet rs =
stmt.executeQuery(
"SELECT * FROM students");
```

---

## UPDATE

```java
stmt.executeUpdate(
"UPDATE students SET name='John' WHERE id=1");
```

---

## DELETE

```java
stmt.executeUpdate(
"DELETE FROM students WHERE id=1");
```

---

# executeQuery() vs executeUpdate()

## executeQuery()

Used for SELECT statements.

```java
ResultSet rs =
stmt.executeQuery(sql);
```

Returns:

```java
ResultSet
```

---

## executeUpdate()

Used for:

* INSERT
* UPDATE
* DELETE

```java
int rows =
stmt.executeUpdate(sql);
```

Returns the number of affected rows.

---

# SQL Injection

A major security vulnerability caused by concatenating user input directly into SQL queries.

### Unsafe Example

```java
String name = scanner.nextLine();

String sql =
"SELECT * FROM students WHERE name='"
+ name + "'";
```

Malicious input:

```sql
' OR '1'='1
```

Can expose all records from the database.

---

# PreparedStatement

PreparedStatement is the preferred way to execute SQL queries.

Advantages:

* Prevents SQL Injection
* Faster Execution
* Reusable Queries
* Better Performance

### Example

```java
PreparedStatement ps =
conn.prepareStatement(
"SELECT * FROM students WHERE id=?");

ps.setInt(1, 101);

ResultSet rs = ps.executeQuery();
```

### Insert Example

```java
PreparedStatement ps =
conn.prepareStatement(
"INSERT INTO students VALUES (?, ?)");

ps.setInt(1, 1);
ps.setString(2, "Aryadeep");

ps.executeUpdate();
```

---

# Statement vs PreparedStatement

| Statement                   | PreparedStatement      |
| --------------------------- | ---------------------- |
| Less Secure                 | More Secure            |
| Vulnerable to SQL Injection | Prevents SQL Injection |
| Compiled Every Time         | Compiled Once          |
| Slower                      | Faster                 |
| Not Reusable                | Reusable               |

---

# CallableStatement

Used to execute Stored Procedures.

```java
CallableStatement cs =
conn.prepareCall("{call getStudent(?)}");
```

Less common for freshers but important to know.

---

# Transactions

A transaction is a group of operations executed as a single unit.

Example:

1. Withdraw Money
2. Deposit Money

Both operations should either succeed together or fail together.

---

# ACID Properties

### Atomicity

All operations succeed or none succeed.

### Consistency

Database remains valid before and after transactions.

### Isolation

Transactions do not interfere with each other.

### Durability

Committed changes are permanently saved.

---

# Transaction Management

Disable Auto Commit:

```java
conn.setAutoCommit(false);
```

Commit Changes:

```java
conn.commit();
```

Rollback Changes:

```java
conn.rollback();
```

---

# Batch Processing

Executes multiple queries together.

```java
stmt.addBatch(sql1);
stmt.addBatch(sql2);
stmt.addBatch(sql3);

stmt.executeBatch();
```

Used for:

* Bulk Inserts
* Data Migration
* ETL Jobs

---

# Connection Pooling

Creating a database connection is expensive.

Instead of creating new connections repeatedly, applications maintain a pool of reusable connections.

```
Connection Pool
      |
 --------------
 |     |      |
C1    C2     C3
```

Popular Connection Pools:

* HikariCP
* C3P0

Spring Boot uses HikariCP by default.

---

# Try-With-Resources

Recommended way to close JDBC resources.

```java
try (
        Connection conn = DriverManager.getConnection(...);
        PreparedStatement ps = conn.prepareStatement(...);
        ResultSet rs = ps.executeQuery()
) {

}
```

Benefits:

* Cleaner Code
* Automatic Resource Management
* Prevents Resource Leaks

---

# SQLException

Parent exception for JDBC errors.

```java
catch(SQLException e) {
    System.out.println(e.getMessage());
}
```

Useful Methods:

```java
e.getMessage();
e.getErrorCode();
e.getSQLState();
```

---

# JDBC in Modern Applications

Today, most enterprise applications use:

```
Spring Boot
     ↓
Hibernate/JPA
     ↓
JDBC
     ↓
Database
```

Although developers usually work with JPA/Hibernate, JDBC still operates underneath.

Understanding JDBC helps in debugging, performance optimization, and database-related interview questions.

---

# Interview Questions

## Beginner

1. What is JDBC?
2. Why is JDBC needed?
3. Explain JDBC Architecture.
4. Explain JDBC Workflow.
5. What is DriverManager?
6. What is ResultSet?
7. Difference between executeQuery() and executeUpdate()?

---

## Intermediate

8. What is SQL Injection?
9. Statement vs PreparedStatement?
10. What is Connection Pooling?
11. What are Transactions?
12. What is Auto Commit?

---

## Advanced

13. What is Batch Processing?
14. What is CallableStatement?
15. Explain ACID Properties.
16. What are Transaction Isolation Levels?
17. How does Spring Data JPA use JDBC internally?

---

# Key Takeaways

✅ JDBC is the standard API for database connectivity in Java.

✅ Core Components:

* DriverManager
* Connection
* Statement
* PreparedStatement
* ResultSet

✅ Always prefer PreparedStatement over Statement.

✅ Learn CRUD operations thoroughly.

✅ Understand Transactions and ACID Properties.

✅ Learn Connection Pooling and Batch Processing.

✅ JDBC forms the foundation of Hibernate and Spring Data JPA.

Mastering JDBC makes learning Spring Boot, Hibernate, JPA, and enterprise backend development significantly easier.
