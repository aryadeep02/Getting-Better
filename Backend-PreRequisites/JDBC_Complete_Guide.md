# JDBC (Java Database Connectivity) — Complete Guide: Beginner to Expert

## Table of Contents
1. [Introduction & Architecture](#1-introduction--architecture)
2. [JDBC Drivers](#2-jdbc-drivers)
3. [Setting Up a Connection](#3-setting-up-a-connection)
4. [Statement, PreparedStatement, CallableStatement](#4-statement-preparedstatement-callablestatement)
5. [ResultSet Deep Dive](#5-resultset-deep-dive)
6. [Transactions](#6-transactions)
7. [Batch Processing](#7-batch-processing)
8. [Exception Handling (SQLException)](#8-exception-handling-sqlexception)
9. [Metadata APIs](#9-metadata-apis)
10. [Connection Pooling (HikariCP, etc.)](#10-connection-pooling-hikaricp-etc)
11. [DataSource vs DriverManager](#11-datasource-vs-drivermanager)
12. [RowSet](#12-rowset)
13. [Advanced/Expert Topics](#13-advancedexpert-topics)
14. [JDBC + Spring Boot (JdbcTemplate)](#14-jdbc--spring-boot-jdbctemplate)
15. [Best Practices Checklist](#15-best-practices-checklist)
16. [Common Interview Questions](#16-common-interview-questions)

---

## 1. Introduction & Architecture

**JDBC (Java Database Connectivity)** is a Java API that defines how a client may access a database. It's part of `java.sql` and `javax.sql` packages and provides a vendor-neutral way to connect to relational databases (MySQL, PostgreSQL, Oracle, SQL Server, etc.).

### Architecture Layers

```
Java Application
      │
      ▼
JDBC API (java.sql.*, javax.sql.*)   <- interfaces, vendor-independent
      │
      ▼
JDBC Driver Manager
      │
      ▼
JDBC Driver (vendor-specific, e.g., mysql-connector-j)
      │
      ▼
Database (native protocol)
```

JDBC is **interface-driven**: `java.sql.Connection`, `Statement`, `ResultSet` etc. are all interfaces. The actual implementation is supplied by the **driver JAR** for your specific database. This is why the same code works against MySQL or PostgreSQL with only the driver and connection URL changing.

### Core Interfaces at a Glance

| Interface | Purpose |
|---|---|
| `DriverManager` | Manages a list of drivers, creates connections |
| `Connection` | Represents a session with the database |
| `Statement` | Executes static SQL |
| `PreparedStatement` | Executes precompiled, parameterized SQL |
| `CallableStatement` | Executes stored procedures |
| `ResultSet` | Represents the result table from a query |
| `ResultSetMetaData` | Info about columns in a ResultSet |
| `DatabaseMetaData` | Info about the database itself |
| `SQLException` | Checked exception for DB errors |
| `DataSource` | Factory for connections (used with pooling) |

---

## 2. JDBC Drivers

There are four historical types, but in modern development you'll almost always use **Type 4**.

| Type | Name | Description | Status |
|---|---|---|---|
| 1 | JDBC-ODBC Bridge | Translates JDBC calls to ODBC | Removed since Java 8 |
| 2 | Native-API driver | Uses native client libraries | Rare today |
| 3 | Network Protocol driver | Middleware translates to DB protocol | Rare today |
| 4 | Thin/Pure Java driver | Directly talks to DB using its native protocol | **Standard today** |

### Adding a Driver (Maven example — MySQL)

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>9.1.0</version>
</dependency>
```

### PostgreSQL

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.4</version>
</dependency>
```

Since JDBC 4.0 (Java 6+), drivers are auto-registered via `META-INF/services/java.sql.Driver` (Service Provider Interface), so `Class.forName("com.mysql.cj.jdbc.Driver")` is **no longer required** in modern code — though you'll still see it in legacy tutorials and older codebases.

---

## 3. Setting Up a Connection

### Basic Connection

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BasicConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/company_db";
        String user = "root";
        String password = "yourpassword";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

### Anatomy of a JDBC URL

```
jdbc:<subprotocol>://<host>:<port>/<database>?<properties>
jdbc:mysql://localhost:3306/company_db?useSSL=false&serverTimezone=UTC
jdbc:postgresql://localhost:5432/company_db
jdbc:oracle:thin:@localhost:1521:orcl
```

### Try-With-Resources (mandatory practice)

`Connection`, `Statement`, and `ResultSet` all implement `AutoCloseable`. **Always** use try-with-resources so resources close automatically, even on exceptions — this prevents connection leaks, one of the most common production bugs.

```java
try (Connection conn = DriverManager.getConnection(url, user, password);
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {

    while (rs.next()) {
        System.out.println(rs.getString("name"));
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

---

## 4. Statement, PreparedStatement, CallableStatement

### 4.1 `Statement` — Static SQL

Use only for simple, hardcoded, non-parameterized queries. **Never** use for user input — vulnerable to SQL injection.

```java
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM employees WHERE dept = 'IT'");
```

- `executeQuery(sql)` → returns `ResultSet` (for SELECT)
- `executeUpdate(sql)` → returns `int` (rows affected, for INSERT/UPDATE/DELETE/DDL)
- `execute(sql)` → returns `boolean` (true if result is a ResultSet), used when statement type is unknown ahead of time

### 4.2 `PreparedStatement` — Parameterized, Precompiled SQL

**This is what you should use 95% of the time.** Benefits:
- Prevents SQL injection (parameters are bound, not concatenated)
- Precompiled by the DB → better performance on repeated execution
- Cleaner handling of types (dates, nulls, etc.)

```java
String sql = "INSERT INTO employees (name, salary, dept) VALUES (?, ?, ?)";

try (PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setString(1, "Aryan Sharma");
    ps.setDouble(2, 85000.00);
    ps.setString(3, "Engineering");

    int rowsInserted = ps.executeUpdate();
    System.out.println(rowsInserted + " row(s) inserted.");
}
```

Parameterized SELECT:

```java
String sql = "SELECT * FROM employees WHERE salary > ? AND dept = ?";

try (PreparedStatement ps = conn.prepareStatement(sql)) {
    ps.setDouble(1, 50000);
    ps.setString(2, "Engineering");

    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            System.out.println(rs.getString("name") + " - " + rs.getDouble("salary"));
        }
    }
}
```

### Why PreparedStatement Stops SQL Injection

```java
// VULNERABLE — never do this:
String sql = "SELECT * FROM users WHERE username = '" + userInput + "'";
// userInput = "' OR '1'='1" turns this into a query that returns everything

// SAFE:
String sql = "SELECT * FROM users WHERE username = ?";
ps.setString(1, userInput); // treated strictly as data, never as SQL syntax
```

### 4.3 `CallableStatement` — Stored Procedures

```java
// Assume stored procedure: CALL GetEmployeeCount(IN dept VARCHAR(50), OUT total INT)
String sql = "{CALL GetEmployeeCount(?, ?)}";

try (CallableStatement cs = conn.prepareCall(sql)) {
    cs.setString(1, "Engineering");
    cs.registerOutParameter(2, java.sql.Types.INTEGER);

    cs.execute();

    int total = cs.getInt(2);
    System.out.println("Total employees: " + total);
}
```

CallableStatement supports **IN**, **OUT**, and **INOUT** parameters, and can also return multiple `ResultSet`s via `getMoreResults()`.

---

## 5. ResultSet Deep Dive

A `ResultSet` maintains a cursor pointing to the current row, initially positioned **before the first row**.

```java
while (rs.next()) {   // moves cursor forward, returns false when no more rows
    int id = rs.getInt("id");
    String name = rs.getString("name");
    Date hireDate = rs.getDate("hire_date");
}
```

### ResultSet Types (set at Statement creation)

```java
Statement stmt = conn.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,  // or TYPE_FORWARD_ONLY, TYPE_SCROLL_SENSITIVE
    ResultSet.CONCUR_UPDATABLE          // or CONCUR_READ_ONLY
);
```

| Type | Meaning |
|---|---|
| `TYPE_FORWARD_ONLY` | Default. Cursor moves only forward. Fastest. |
| `TYPE_SCROLL_INSENSITIVE` | Can move forward/backward; doesn't see live DB changes |
| `TYPE_SCROLL_SENSITIVE` | Can move forward/backward; reflects live DB changes |

| Concurrency | Meaning |
|---|---|
| `CONCUR_READ_ONLY` | Default. Cannot update rows through ResultSet |
| `CONCUR_UPDATABLE` | Can call `rs.updateRow()`, `rs.deleteRow()`, `rs.insertRow()` |

### Scrollable ResultSet Navigation

```java
rs.absolute(5);   // jump to row 5
rs.relative(-2);  // move back 2 rows
rs.first();
rs.last();
rs.beforeFirst();
rs.afterLast();
```

### Updatable ResultSet Example

```java
Statement stmt = conn.createStatement(
    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
ResultSet rs = stmt.executeQuery("SELECT * FROM employees WHERE id = 5");

if (rs.next()) {
    rs.updateDouble("salary", 95000.00);
    rs.updateRow();   // pushes change back to DB immediately
}
```

### Common getter/setter type mappings

| SQL Type | Java Type | Getter/Setter |
|---|---|---|
| INT | int | `getInt` / `setInt` |
| VARCHAR | String | `getString` / `setString` |
| DECIMAL/NUMERIC | BigDecimal | `getBigDecimal` / `setBigDecimal` |
| DATE | java.sql.Date | `getDate` / `setDate` |
| TIMESTAMP | java.sql.Timestamp | `getTimestamp` / `setTimestamp` |
| BOOLEAN | boolean | `getBoolean` / `setBoolean` |
| BLOB | Blob/byte[] | `getBlob`/`getBytes` |
| CLOB | Clob/String | `getClob`/`getString` |
| NULL check | — | `rs.wasNull()` after a getter call |

---

## 6. Transactions

By default, JDBC connections are in **auto-commit mode** — every statement is its own transaction. For multi-statement atomicity, disable it manually.

```java
Connection conn = DriverManager.getConnection(url, user, password);
try {
    conn.setAutoCommit(false);

    PreparedStatement debit = conn.prepareStatement(
        "UPDATE accounts SET balance = balance - ? WHERE id = ?");
    debit.setDouble(1, 500);
    debit.setInt(2, 101);
    debit.executeUpdate();

    PreparedStatement credit = conn.prepareStatement(
        "UPDATE accounts SET balance = balance + ? WHERE id = ?");
    credit.setDouble(1, 500);
    credit.setInt(2, 202);
    credit.executeUpdate();

    conn.commit();   // both succeed together
} catch (SQLException e) {
    conn.rollback();  // both fail together
    e.printStackTrace();
} finally {
    conn.setAutoCommit(true);
    conn.close();
}
```

### Savepoints

```java
conn.setAutoCommit(false);
Savepoint sp1 = conn.setSavepoint("beforeSecondUpdate");

try {
    // risky operation
    stmt.executeUpdate("...");
} catch (SQLException e) {
    conn.rollback(sp1);  // rollback only to this point, not the whole transaction
}

conn.commit();
```

### Transaction Isolation Levels

| Level | Prevents Dirty Read | Prevents Non-Repeatable Read | Prevents Phantom Read |
|---|---|---|---|
| `TRANSACTION_READ_UNCOMMITTED` | ❌ | ❌ | ❌ |
| `TRANSACTION_READ_COMMITTED` | ✅ | ❌ | ❌ |
| `TRANSACTION_REPEATABLE_READ` | ✅ | ✅ | ❌ |
| `TRANSACTION_SERIALIZABLE` | ✅ | ✅ | ✅ |

```java
conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
```

Higher isolation = stronger consistency but more locking and lower concurrency. Most OLTP systems default to `READ_COMMITTED`.

---

## 7. Batch Processing

Batching drastically reduces network round-trips when inserting/updating many rows.

```java
String sql = "INSERT INTO employees (name, salary, dept) VALUES (?, ?, ?)";

try (PreparedStatement ps = conn.prepareStatement(sql)) {
    conn.setAutoCommit(false);

    for (Employee e : employeeList) {
        ps.setString(1, e.getName());
        ps.setDouble(2, e.getSalary());
        ps.setString(3, e.getDept());
        ps.addBatch();

        if (employeeList.indexOf(e) % 1000 == 0) {
            ps.executeBatch();   // flush every 1000 rows to avoid huge memory buildup
            ps.clearBatch();
        }
    }

    ps.executeBatch();
    conn.commit();
} catch (SQLException e) {
    conn.rollback();
}
```

For MySQL, add `rewriteBatchedStatements=true` to the URL — it rewrites batched inserts into a single multi-row `INSERT ... VALUES (...),(...),(...)` statement, which is dramatically faster:

```
jdbc:mysql://localhost:3306/db?rewriteBatchedStatements=true
```

---

## 8. Exception Handling (SQLException)

`SQLException` is a **checked exception** with a chain structure and vendor-specific codes.

```java
try {
    // JDBC operation
} catch (SQLException e) {
    System.out.println("Message: " + e.getMessage());
    System.out.println("SQLState: " + e.getSQLState());     // standardized 5-char code
    System.out.println("Vendor code: " + e.getErrorCode()); // DB-specific

    // Exceptions can be chained
    SQLException next = e.getNextException();
    while (next != null) {
        System.out.println("Chained: " + next.getMessage());
        next = next.getNextException();
    }
}
```

### Common subclasses (JDBC 4+)
- `SQLIntegrityConstraintViolationException` — unique/foreign key violations
- `SQLTimeoutException` — query/connection timeout
- `SQLSyntaxErrorException` — malformed SQL
- `BatchUpdateException` — thrown from `executeBatch()`, includes partial results via `getUpdateCounts()`

```java
catch (BatchUpdateException e) {
    int[] updateCounts = e.getUpdateCounts();
    for (int i = 0; i < updateCounts.length; i++) {
        if (updateCounts[i] == Statement.EXECUTE_FAILED) {
            System.out.println("Row " + i + " failed");
        }
    }
}
```

---

## 9. Metadata APIs

### `DatabaseMetaData` — info about the DB itself

```java
DatabaseMetaData dbMeta = conn.getMetaData();
System.out.println(dbMeta.getDatabaseProductName());
System.out.println(dbMeta.getDatabaseProductVersion());
System.out.println(dbMeta.getDriverName());

ResultSet tables = dbMeta.getTables(null, null, "%", new String[]{"TABLE"});
while (tables.next()) {
    System.out.println(tables.getString("TABLE_NAME"));
}
```

### `ResultSetMetaData` — info about a query's result shape

```java
ResultSetMetaData rsMeta = rs.getMetaData();
int colCount = rsMeta.getColumnCount();

for (int i = 1; i <= colCount; i++) {
    System.out.println(rsMeta.getColumnName(i) + " : " + rsMeta.getColumnTypeName(i));
}
```

This is extremely useful for building **generic tools** — e.g., a CSV exporter or a dynamic table renderer that doesn't know column names in advance.

---

## 10. Connection Pooling (HikariCP, etc.)

Opening a raw TCP connection to a DB per request is **expensive** (handshake, auth, socket setup). Production systems always use a **connection pool**: a set of pre-opened, reusable connections.

**HikariCP** is the current industry standard (default in Spring Boot since 2.x).

```xml
<dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>6.2.1</version>
</dependency>
```

```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/company_db");
config.setUsername("root");
config.setPassword("yourpassword");
config.setMaximumPoolSize(10);
config.setMinimumIdle(2);
config.setIdleTimeout(30000);
config.setConnectionTimeout(30000);
config.setMaxLifetime(1800000);

HikariDataSource dataSource = new HikariDataSource(config);

try (Connection conn = dataSource.getConnection()) {
    // use connection — it's returned to the pool automatically on close(),
    // NOT physically closed
}
```

### Pool Sizing Rule of Thumb

HikariCP's own guidance (based on the formula from "PostgreSQL: How Many Connections Do You Really Need?") suggests:

```
connections = ((core_count * 2) + effective_spindle_count)
```

For most modern SSD-backed systems, a pool size of **10–20** is plenty even under heavy load — oversized pools often perform *worse* due to context-switching and lock contention on the DB side.

### Other pool options
- **Apache DBCP2**
- **C3P0** (legacy, rarely used in new projects)
- **Tomcat JDBC Pool**

---

## 11. DataSource vs DriverManager

| | `DriverManager` | `DataSource` |
|---|---|---|
| Use case | Simple scripts, learning | Production applications |
| Pooling | No (raw connections) | Yes, typically |
| JNDI lookup | No | Yes, supported |
| Configuration | Hardcoded URL/creds in code | Externalized, injectable |
| Standard in enterprise apps | No | **Yes** |

```java
// javax.sql.DataSource is the interface; HikariDataSource implements it
DataSource ds = ...; // injected via Spring, or constructed manually
try (Connection conn = ds.getConnection()) {
    // ...
}
```

In Spring Boot, you rarely construct a `DataSource` manually — it's autoconfigured from `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/company_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.hikari.maximum-pool-size=10
```

---

## 12. RowSet

`RowSet` (in `javax.sql`) is a `ResultSet` that can be **disconnected** from the database — useful for passing data around without holding a live connection open.

```java
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

RowSetFactory factory = RowSetProvider.newFactory();
CachedRowSet crs = factory.createCachedRowSet();

crs.setUrl("jdbc:mysql://localhost:3306/company_db");
crs.setUsername("root");
crs.setPassword("yourpassword");
crs.setCommand("SELECT * FROM employees");
crs.execute();

// connection is now closed internally, but crs still holds all the data
while (crs.next()) {
    System.out.println(crs.getString("name"));
}
```

`CachedRowSet` is largely superseded in modern practice by mapping rows into POJOs or DTOs immediately, but it still shows up in legacy/enterprise codebases and exam syllabi.

---

## 13. Advanced/Expert Topics

### 13.1 Fetch Size — Controlling Memory for Large Result Sets

By default, some drivers (e.g., MySQL Connector/J) load the **entire ResultSet into memory** unless told otherwise. For large tables, this can cause `OutOfMemoryError`.

```java
Statement stmt = conn.createStatement();
stmt.setFetchSize(Integer.MIN_VALUE);  // MySQL-specific trick for true row-streaming
// For most other DBs (Postgres, Oracle):
stmt.setFetchSize(500);  // fetch 500 rows at a time from the server
```

For streaming in MySQL specifically, you also need `useCursorFetch=true` in the URL, or the `Integer.MIN_VALUE` trick above combined with `TYPE_FORWARD_ONLY` + `CONCUR_READ_ONLY`.

### 13.2 Query Timeouts

```java
stmt.setQueryTimeout(5); // seconds — throws SQLTimeoutException if exceeded
```

### 13.3 Batch + Generated Keys

Retrieving auto-generated primary keys after insert:

```java
String sql = "INSERT INTO employees (name, salary) VALUES (?, ?)";
try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    ps.setString(1, "Aryan");
    ps.setDouble(2, 90000);
    ps.executeUpdate();

    try (ResultSet keys = ps.getGeneratedKeys()) {
        if (keys.next()) {
            long newId = keys.getLong(1);
            System.out.println("New employee ID: " + newId);
        }
    }
}
```

### 13.4 N+1 Query Problem

A classic performance trap — looping over a result set and issuing a query per row:

```java
// BAD: N+1 queries
List<Employee> employees = getAllEmployees();
for (Employee e : employees) {
    Department d = getDepartmentById(e.getDeptId()); // separate query EVERY iteration!
}

// GOOD: single JOIN query
String sql = """
    SELECT e.*, d.name AS dept_name
    FROM employees e
    JOIN departments d ON e.dept_id = d.id
    """;
```

This matters even more in JDBC than in ORMs, because there's no lazy-loading magic hiding the cost from you — you feel every round-trip directly.

### 13.5 Handling BLOB/CLOB

```java
// Storing a file as BLOB
String sql = "INSERT INTO documents (name, content) VALUES (?, ?)";
try (PreparedStatement ps = conn.prepareStatement(sql);
     InputStream fis = new FileInputStream("resume.pdf")) {
    ps.setString(1, "resume.pdf");
    ps.setBinaryStream(2, fis);
    ps.executeUpdate();
}

// Reading a BLOB back
ResultSet rs = stmt.executeQuery("SELECT content FROM documents WHERE id = 1");
if (rs.next()) {
    try (InputStream is = rs.getBinaryStream("content");
         OutputStream os = new FileOutputStream("downloaded_resume.pdf")) {
        is.transferTo(os);
    }
}
```

### 13.6 Thread Safety

`Connection` objects are **not thread-safe**. Never share a single `Connection` across threads — this is precisely why pooling exists: each thread borrows its own connection from the pool and returns it when done.

### 13.7 Retry Logic for Transient Failures

Production-grade code often wraps DB calls with retry logic for transient network blips or deadlocks:

```java
int maxRetries = 3;
for (int attempt = 1; attempt <= maxRetries; attempt++) {
    try {
        // execute transaction
        break;
    } catch (SQLException e) {
        if (isTransient(e) && attempt < maxRetries) {
            Thread.sleep((long) Math.pow(2, attempt) * 100); // exponential backoff
            continue;
        }
        throw e;
    }
}
```

A common transient error is **deadlock** (MySQL error code 1213, SQLState `40001`) — the standard response is to retry the whole transaction.

### 13.8 Statement Caching / Prepared Statement Pooling

Some connection pools (and drivers) cache compiled `PreparedStatement`s so identical SQL text isn't re-parsed by the DB server on every call. In HikariCP + MySQL, this is tuned via:

```properties
cachePrepStmts=true
prepStmtCacheSize=250
prepStmtCacheSqlLimit=2048
```

### 13.9 JDBC vs JPA/Hibernate — When to Use Raw JDBC

| Scenario | Prefer |
|---|---|
| Simple CRUD app, rapid development | JPA/Hibernate |
| Fine-grained control over SQL, performance-critical paths | JDBC / `JdbcTemplate` |
| Complex reporting/analytics queries | JDBC (raw SQL) |
| Bulk data loads (ETL) | JDBC batch |
| Domain-driven design with rich entity graphs | JPA |

Even teams using Hibernate heavily will often drop to raw JDBC (or `JdbcTemplate`) for hot-path queries where the ORM's overhead or generated SQL isn't good enough — this is a common **quant/fintech backend pattern** where query latency really matters.

---

## 14. JDBC + Spring Boot (JdbcTemplate)

`JdbcTemplate` wraps raw JDBC to eliminate boilerplate (manual connection open/close, exception handling) while still giving you full SQL control — a good middle ground between raw JDBC and full ORM.

```java
@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findBySalaryAbove(double minSalary) {
        String sql = "SELECT * FROM employees WHERE salary > ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new Employee(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("salary")
            ), minSalary);
    }

    public int insert(Employee e) {
        String sql = "INSERT INTO employees (name, salary) VALUES (?, ?)";
        return jdbcTemplate.update(sql, e.getName(), e.getSalary());
    }

    public Employee findById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
            new Employee(rs.getInt("id"), rs.getString("name"), rs.getDouble("salary")), id);
    }
}
```

Spring translates every `SQLException` into an **unchecked** `DataAccessException` hierarchy (`DuplicateKeyException`, `DataIntegrityViolationException`, etc.), which is far more pleasant to work with than raw checked `SQLException`.

### Transactions in Spring (declarative)

```java
@Service
public class TransferService {

    @Autowired private JdbcTemplate jdbcTemplate;

    @Transactional
    public void transfer(int fromId, int toId, double amount) {
        jdbcTemplate.update("UPDATE accounts SET balance = balance - ? WHERE id = ?", amount, fromId);
        jdbcTemplate.update("UPDATE accounts SET balance = balance + ? WHERE id = ?", amount, toId);
        // Spring auto-commits on success, auto-rolls-back on any RuntimeException
    }
}
```

---

## 15. Best Practices Checklist

- ✅ Always use `PreparedStatement`, never string-concatenate user input into SQL.
- ✅ Always use try-with-resources for `Connection`, `Statement`, `ResultSet`.
- ✅ Use a connection pool (HikariCP) in any real application — never `DriverManager` directly in production code.
- ✅ Keep transactions **short** — don't hold a connection open across slow, unrelated operations (e.g., calling an external API mid-transaction).
- ✅ Set sensible connection/query timeouts.
- ✅ Use batch operations for bulk inserts/updates.
- ✅ Choose the right isolation level — don't default to `SERIALIZABLE` "just to be safe"; it kills concurrency.
- ✅ Log `SQLState` and vendor error codes, not just messages, for debugging.
- ✅ Watch for the N+1 problem, even outside ORMs.
- ✅ Close resources in reverse order of opening (handled automatically by try-with-resources).
- ❌ Never share a single `Connection` object across threads.
- ❌ Never swallow `SQLException` silently.

---

## 16. Common Interview Questions

1. **What's the difference between `Statement` and `PreparedStatement`?**
   Precompilation, parameter binding (SQL injection safety), performance on repeated execution.

2. **Why use a connection pool instead of `DriverManager.getConnection()` per request?**
   TCP/auth handshake cost, resource exhaustion, latency under load.

3. **What happens if you don't call `commit()` with auto-commit off?**
   The transaction stays open, locks are held, and on connection close it typically rolls back (behavior can be driver-dependent) — this is also a classic cause of connection pool exhaustion.

4. **Explain the difference between `executeQuery`, `executeUpdate`, and `execute`.**

5. **What is the N+1 problem, and how do you avoid it in JDBC?**

6. **How do transaction isolation levels affect performance vs correctness?**

7. **What's the purpose of `ResultSet.TYPE_SCROLL_INSENSITIVE` vs `TYPE_FORWARD_ONLY`?**

8. **How would you stream a very large result set without running out of memory?**
   `setFetchSize`, cursor-based fetching, avoiding loading the full `ResultSet` into a `List` upfront.

9. **What is a `SQLState` code and how does it differ from a vendor error code?**

10. **How does `JdbcTemplate` simplify JDBC compared to raw JDBC code?**

---

## Suggested Learning Path (Practice Projects)

1. **CRUD console app** — employees table, raw JDBC, `PreparedStatement` only.
2. **Bank transfer simulator** — transactions, rollback, savepoints, isolation levels.
3. **Bulk CSV importer** — batch inserts, `rewriteBatchedStatements`, performance benchmarking (batch size 1 vs 100 vs 1000).
4. **Connection-pooled REST API** — Spring Boot + HikariCP + `JdbcTemplate`, expose employee CRUD over HTTP.
5. **Generic table exporter** — use `ResultSetMetaData` to dump *any* table to CSV without hardcoding column names.
6. **Mini order-matching ledger** — given your interest in quant/trading systems: model orders/trades tables, use transactions + `SERIALIZABLE` isolation to simulate safe concurrent order matching, and benchmark throughput at different isolation levels. This bridges JDBC fundamentals directly into the kind of low-latency, correctness-critical persistence layer used in trading systems.
