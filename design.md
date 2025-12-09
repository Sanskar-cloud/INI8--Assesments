# Design Document – Patient Document Portal

## 1. Tech Stack Choices

### Q1. What frontend framework did you use and why?

**Framework:** React (TypeScript) with Vite

**Why:**

- React is widely used and an industry standard for building fast, interactive Single Page Applications (SPAs).
- TypeScript (TSX) adds static typing, which improves maintainability and reduces runtime errors.
- Vite provides extremely fast builds and a modern development environment.
- The React ecosystem provides strong UI libraries such as Material UI and React Hot Toast, enabling a professional and responsive UI quickly.
- Axios + React Hooks make interaction with REST APIs seamless and structured.

---

### Q2. What backend framework did you choose and why?

**Framework:** Spring Boot (Java 17)

**Why:**

- Spring Boot is production-ready, enterprise-level, and widely adopted in backend development.
- Provides built-in support for REST APIs, file uploads, and dependency injection.
- Encourages clean architecture through:
    - Service Layer Pattern
    - Repository Pattern
    - ConfigurationProperties for clean configuration
    - Centralized exception handling
- Integrates very well with relational databases and offers scalability.
- Easy deployment and maintainability for real-world backend systems.

---

### Q3. What database did you choose and why?

**Database:** SQLite

**Why:**

- Lightweight and file-based, making it ideal for local development and assignments.
- Requires zero installation or separate DB server, simplifying setup for reviewers.
- Fast and perfectly capable for small-scale applications.
- Stores all data in a single `.db` file, making portability easy.

*Note: PostgreSQL would be preferred in production for better scalability and concurrency.*

---

### Q4. If you were to support 1,000 users, what changes would you consider?

To handle a larger load (e.g., 1,000 users), the following enhancements are recommended:

#### Infrastructure Improvements
- Replace SQLite → **PostgreSQL** for better performance under concurrent use.
- Move uploaded file storage to a **Cloud Storage Provider** such as AWS S3 or Google Cloud Storage.

#### Backend Enhancements
- Introduce a caching layer (Redis) for faster retrieval of document metadata.
- Add pagination to `/documents` endpoint.
- Use asynchronous processing for file uploads to reduce blocking.
- Add authentication/authorization (e.g., JWT).

#### Frontend Enhancements
- Implement pagination or infinite scroll.
- Improved retry logic and error boundaries.
- Optimize large list rendering using virtualization.

#### DevOps Enhancements
- Dockerize frontend + backend.
- Add CI/CD pipeline with GitHub Actions or GitLab CI.
- Deploy using Kubernetes for auto-scaling.

---

## 2. Architecture Overview

### 2.1 High-Level Architecture Diagram

```text
+---------------------+       +-------------------------+       +--------------------+
|   React Frontend    | --->  |   Spring Boot Backend   | --->  |   SQLite Database   |
+---------------------+       +-------------------------+       +--------------------+
            |                            |
            |                            |
            +----------------------------+
                  File Upload / Read
                     Local Storage

```
### 2.2 Flow Description

### Frontend (React + TypeScript)

- Provides a clean UI for uploading, listing, downloading, and deleting documents.

- Validates file type before upload.

- Sends files using multipart/form-data via Axios.

- Uses Material UI for interactive components and React Hot Toast for user notifications.

### Backend (Spring Boot)

- Validates uploaded files (must be PDF).

- Uses UUIDs for unique filenames to avoid conflicts.

- Stores metadata in SQLite database.

- Streams files for downloading.

- Deletes both database record and physical file on delete operation.

- Follows layered architecture with Service and Repository patterns.

### Database (SQLite)

#### Stores document metadata:

- id

- originalFilename

- storedFilename

- fileSize

- createdAt

#### File Storage (Local Filesystem)

- Files are stored inside uploads/ directory.

- UUID-based names ensure unique and collision-free storage.

___

## 3. API Specification

**Base Response Format (BaseApiResponse<T>)**

***Every response follows this structure:***

```json
{
  "success": true,
  "message": "Some message",
  "data": { }
}
```
### 3.1 Upload a PDF

**Endpoint:** `/documents/upload`  
**Method:** `POST`  
**Content-Type:** `multipart/form-data`

#### **Request Example**

`file: <pdf-file>`

**Sample Response**
```json
{
  "success": true,
  "message": "File uploaded successfully",
  "data": {
    "id": 1,
    "filename": "report.pdf",
    "size": 23882,
    "createdAt": "2025-12-09T10:20:00"
  }
}
```


**Description:** `Uploads a PDF, stores it on disk, and saves metadata in the database.`

### 3.2 List All Documents

**Endpoint:** `/documents`

**Method:** `GET`

**Sample Response**
```json
{
  "success": true,
  "message": "Documents fetched successfully",
  "data": [
    {
      "id": 1,
      "filename": "report.pdf",
      "size": 23882,
      "createdAt": "2025-12-09T10:20:00"
    }
  ]
}
```


**Description:** `Returns a list of all document metadata.`

### 3.3 Download a Document

**Endpoint:** `/documents/:id`

**Method:** `GET`

**Response**

A PDF file stream with headers like:

**Content-Disposition:** `attachment; filename="report.pdf"`

**Content-Type:** `application/pdf`


**Description:** `Retrieves file from local storage and streams it to the client.`

### 3.4 Delete a Document

**Endpoint:** `/documents/:id`

**Method:** `DELETE`

**Sample Response**
```json
{
"success": true,
"message": "Document deleted successfully",
"data": null
}
```


**Description:** `Deletes the file from disk and removes metadata from the database.`

___

## 4. Data Flow Description

### Q5. Step-by-step file upload and download process

---

###  **Upload Flow (Step-by-Step)**

1. The user selects a **PDF document** in the React UI.
2. The frontend sends the file to the backend using **Axios** with `multipart/form-data`.
3. The Spring Boot backend receives the file as a `MultipartFile`.
4. The backend performs validation:
   - The file must not be empty.
   - The file must be a **PDF** (`.pdf` extension).
5. A **UUID-based filename** is generated to ensure uniqueness.
6. The validated file is stored in the `uploads/` directory.
7. Metadata is saved in the SQLite database, including:
   - `originalFilename`
   - `storedFilename`
   - `fileSize`
   - `createdAt`
8. The backend returns a standardized `BaseApiResponse` with metadata.
9. The frontend refreshes the document list and displays a **success toast**.

---

###  **Download Flow (Step-by-Step)**

1. The user clicks **"Download"** on a specific document.
2. The React frontend calls `GET /documents/:id`.
3. The backend retrieves the document metadata from the database.
4. The stored filename is mapped to the corresponding file inside the `uploads/` directory.
5. The backend reads the file and streams it as a **binary PDF** with appropriate headers.
6. The browser automatically initiates the **file download**.

---

## 5. Assumptions

### Q6. Assumptions Made

- The system is intended for a **single user**, so authentication and authorization are not required.
- Only **PDF files** are allowed to be uploaded for this assignment.
- The maximum allowed upload size is **10 MB**, enforced through Spring Boot configuration.
- The server environment has **sufficient local storage** to store uploaded documents.
- **SQLite** is sufficient for assignment-level use due to low concurrency and lightweight operations.
- All files are stored **locally** in the `uploads/` directory instead of using cloud storage.
- **UUID-based filenames** ensure no filename collisions occur even if multiple users upload files with the same original name.

---
