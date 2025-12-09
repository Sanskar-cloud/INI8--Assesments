# Patient Document Portal

A full-stack application built using **React (TypeScript) + Vite** for the frontend and **Spring Boot (Java 17)** for the backend.  
The system allows users to **upload, list, download, and delete PDF documents**.  
Files are stored locally, while metadata is stored in **SQLite**.

---

##  Project Overview

This project implements a simple yet production-style Document Management Portal:

### **Features**
- Upload PDF documents
- View list of uploaded documents
- Download documents
- Delete documents
- Interactive UI using Material UI + Toast notifications
- Consistent API responses using `BaseApiResponse<T>`
- Clean backend architecture: Controller → Service → Repository → Storage Layer

### **Tech Stack**
- **Frontend:** React + TypeScript + Vite, Material UI, Axios
- **Backend:** Spring Boot (Java 17), Spring MVC, Spring Data JPA
- **Database:** SQLite
- **Storage:** Local filesystem (`uploads/` folder)

---

## Folder Structure

```
/backend
/frontend
design.md
README.md
```

---

##  How to Run the Project Locally

### Prerequisites

Make sure you have installed:
- Node.js (v16+)
- Java 17
- Maven (optional if using IntelliJ auto-import)
- Git

---

##  1. Run the Backend (Spring Boot)

### Step 1 — Open the backend folder

```sh
cd backend
```

### Step 2 — Build and run the project

```sh
mvn spring-boot:run
```

If using IntelliJ, simply click **Run Application**.

Backend runs at:

```
http://localhost:8080
```

The backend will automatically create:
- `uploads/` directory
- `documents.db` SQLite database

---

##  2. Run the Frontend (React + Vite)

### Step 1 — Open the frontend folder

```sh
cd frontend
```

### Step 2 — Install dependencies

```sh
npm install
```

### Step 3 — Start the development server

```sh
npm run dev
```

Frontend runs at:

```
http://localhost:5173
```

---

##  3. API Endpoints Overview

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/documents/upload` | POST | Upload a PDF |
| `/documents` | GET | List all documents |
| `/documents/{id}` | GET | Download PDF by ID |
| `/documents/{id}` | DELETE | Delete PDF by ID |

---

##  Example API Calls (cURL + Postman)

### 1️. Upload a PDF

**Curl**

```sh
curl -X POST http://localhost:8080/documents/upload \
  -F "file=@sample.pdf"
```

**Postman**
- Method: `POST`
- URL: `http://localhost:8080/documents/upload`
- Body → `form-data`
    - Key: `file`
    - Type: `File`
    - Value: your PDF

---

### List All Documents

**Curl**

```sh
curl http://localhost:8080/documents
```

**Postman**
- Method: `GET`
- URL: `http://localhost:8080/documents`

---

###  Download a Document

**Curl**

```sh
curl -OJ http://localhost:8080/documents/1
```

`-OJ` saves the file using the server-provided filename.

**Postman**
- Method: `GET`
- URL: `http://localhost:8080/documents/1`
- Click **Send**, then **Save Response**.

---

###  Delete a Document

**Curl**

```sh
curl -X DELETE http://localhost:8080/documents/1
```

**Postman**
- Method: `DELETE`
- URL: `http://localhost:8080/documents/1`

---

## Base API Response Format

All backend responses follow this structure:

```json
{
  "success": true,
  "message": "Your message here",
  "data": {}
}
```

---

##  Developer Notes

- Uploaded files are located in:
  ```
  backend/uploads/
  ```

- Database file is located in:
  ```
  backend/documents.db
  ```

- To reset the project, delete `uploads/` and `documents.db`.

---

## Conclusion

This project demonstrates a complete full-stack workflow using modern technologies and clean architecture.  
It is structured for readability, maintainability, and easy scalability.