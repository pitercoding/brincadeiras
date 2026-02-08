<h1 align="center">
  <img src="frontend/public/gis-de-cera.png" alt="Logo Brincadeiras" width="40" style="vertical-align: middle; margin-right: 10px;">
  Brincadeiras
</h1>

<p align="center">
  🌎 <strong>Languages:</strong><br>
  <a href="README.md">🇧🇷 Português</a> |
  <a href="README.en.md">🇺🇸 English</a>
</p>

**Brincadeiras** is an application that allows users to search for or receive suggestions of creative activities for children, using simple materials such as paper, sticks, string, etc.

The goal is to **encourage creativity and free play** with practical and accessible ideas.

## 🚀 How to Access the Project (important!)

The backend of the project is hosted on **Render**, which may hibernate after some time without usage. The frontend is hosted normally on **Vercel**.

To ensure everything works correctly, follow these steps:

### 1️⃣ Wake up the backend on Render

Before opening the site, access: 👉 [**Backend - Click Here**](https://brincadeiras-backend.onrender.com/)

This will make Render “wake up” the server.

- The process usually takes **20 to 40 seconds**.
- If it seems stuck, wait — this is normal on the free tier.  
- When the page shows something like *"Backend Active!"* or a similar message, it’s ready.

### 2️⃣ Access the frontend normally

After the backend is awake, open the app: 👉 [**Frontend - Click Here**](https://brincadeiras-one.vercel.app/)

Now all features will be available:

- Search for activities  
- Generate random suggestions  
- Filter by age range  
- View activity details  

### ℹ️ Why is this necessary?

Render Free Tier puts projects into “hibernation” after a period without access. When this happens, the first request of the day needs to “wake up” the server — causing this initial delay.

## 🏆 Motivation

During childhood, play is essential for cognitive and social development. As a Computer Science student, this project emerged from the need to create creative and educational activities for children, combining personal interest with professional learning.

In addition to providing practical and accessible activities for children, the project allowed me to develop skills in **full-stack development**, **database integration**, **REST API handling**, **artificial intelligence implementation**, and deployment of complete web applications.

## 📚 Learning

During the development of the project, skills were applied and improved in:

- **Frontend:** React, Vite, responsive CSS, React Router, Axios, React-Toastify.
- **Backend:** Spring Boot, REST API, Spring Security, global exception handling, logging.
- **Database:** Modeling and integration with MongoDB Atlas.
- **Artificial Intelligence:** Integration with OpenAI GPT-3.5 for automatic activity generation.
- **Deploy:** Backend on Render and frontend on Vercel.

---

## 🧱 General Structure

| Layer        | Technology        | Main Function |
|--------------|-------------------|---------------|
| Frontend     | React + Vite      | Interface to view, filter, add, edit, and generate activities with AI |
| Backend      | Spring Boot       | REST API with logs, global exception handling, and automatic AI generation |
| Database     | MongoDB           | Stores activities and materials |
| AI           | OpenAI GPT-3.5    | Automatic generation of activity ideas |

---

## ⚙️ Technologies and Tools

### 🎨 Frontend (React + Vite)

- ✅ React 18+  
- ✅ Axios  
- ✅ React Router  
- ✅ Responsive CSS3  
- ✅ Vite  
- ✅ React-Toastify  
- ✅ ModalGerarIA.jsx  

**Current features:**

- Activity cards from the backend  
- Filters by title, age range, and materials  
- Proper communication via Axios  
- Home, ActivityDetail, and NewActivity pages  
- Responsive Navbar and Footer  
- AI generation modal with internal scroll and button disabling during generation  
- Refreshed visual style with playful typography, unified colors, and full responsiveness

---

### ⚙️ Backend (Spring Boot + MongoDB)

- ✅ Spring Boot 3+  
- ✅ Spring Web (REST APIs)  
- ✅ Spring Data MongoDB  
- ✅ Lombok  
- ✅ Spring Boot DevTools  
- ✅ Spring Security (SecurityConfig.java)  
- ✅ Configured CORS (WebConfig.java)  
- ✅ Centralized exception handling (GlobalExceptionHandler.java)  
- ✅ Endpoint `/atividades/gerar`  
- ✅ Support for automatic generation via AI (OpenAI GPT-3.5-turbo)  
- ✅ Adjusted data structure: returns title, description, materials, ageRange, id, and type  
- ✅ OpenAI API error handling and improved logging  
- ✅ Dependencies added for OpenAI integration  

**Implemented endpoints:**

| Method | Route | Function | Status |
|--------|-------|----------|--------|
| GET    | /atividades | List all activities | ✅ |
| POST   | /atividades | Create a new activity (use `CadastrarAtividadeRequest`) | ✅ |
| GET    | /atividades/{id} | Get activity details | ✅ |
| PUT    | /atividades/{id} | Update an activity | ✅ |
| DELETE | /atividades/{id} | Remove an activity | ✅ |
| POST   | /atividades/gerar | Automatically generate an activity via AI | ✅ |

---

### 🗄️ Database (MongoDB Atlas)

- ✅ Cluster created and connected to Spring Boot  
- ✅ Tests completed via Compass and Postman  
- ✅ Document structure:

```json
{
  "titulo": "Painting with popsicle sticks",
  "descricao": "Build a brush with sticks and string and paint colorful figures",
  "materiais": ["popsicle sticks", "string", "gouache paint", "paper"],
  "faixaEtaria": "4-6 years"
}
```

### 🤖 Artificial Intelligence

- ✅ Endpoint `/atividades/gerar`
- ✅ Connection with OpenAI GPT-3.5-turbo
- ✅ Frontend integrated via `ModalGerarIA.jsx`
- ✅ Activities return title, description, materials, age group, id, and type
- ✅ Styled and functional “✨ Generate idea with AI” button that disables buttons during generation

## 📸 Screenshots

Cards:
![Cards](docs/images/cards.png)

Detail (update/delete):
![Update/Delete](docs/images/update-delete-card.png)

AI Modal:
![Modal IA](docs/images/modal-ia.png)

New Activity:
![New Activity](docs/images/new-card.png)

## 🧭 Application Flow

```scss
User → Frontend (React)
↓
REST API (Spring Boot, Spring Security, logs, exception handling, AI generation)
↓
MongoDB (Atlas)
↑
(AI generates automatic ideas and returns them to the frontend)
```

## ✅ Current Status

| Area          | Status        | Description |
|---------------|---------------|-----------|
| Backend       | ✅ Completed  | Full CRUD + automatic generation via AI + MongoDB + logs + validations + global handling |
| Frontend      | ✅ Completed  | Home, filters, details, registration, and AI generation integrated |
| Integration   | ✅ Tested     | Axios + Spring Boot working |
| Database      | ✅ Operational| Synchronized with backend |
| AI            | ✅ Implemented| Endpoint and frontend integrated with OpenAI |
| Deploy        | ✅ Completed  | Backend → Render, Frontend → Vercel |

## 🗂️ Folder Structure (essential)

```bash
brincadeiras/
├── 📂 backend/
│   ├── 📂 src/
│   │   ├── 📂 main/
│   │   │   ├── 📂 java/com/brincadeiras/
│   │   │   │   ├── 📂 config/           # Security, CORS, and Logging settings
│   │   │   │   ├── 📂 controller/       # API Endpoints
│   │   │   │   ├── 📂 dto/              # Data Transfer Objects
│   │   │   │   ├── 📂 model/            # Entities (Activity/Atividade)
│   │   │   │   ├── 📂 repository/       # Database access interfaces
│   │   │   │   ├── 📂 service/          # Business logic
│   │   │   │   └── BrincadeirasBackendApplication.java
│   │   │   └── 📂 resources/
│   │   │       ├── application.properties
│   │   │       └── application-secret.properties
│   │   └── 📂 test/                     # Unit and integration tests
│   ├── Dockerfile                       # Container configuration
│   └── pom.xml                          # Maven dependencies
├── 📂 docs/
│   └── 📂 images/                       # Documentation screenshots
├── 📂 frontend/
│   ├── 📂 public/                       # Static assets (icons, images)
│   ├── 📂 src/
│   │   ├── 📂 components/               # Reusable React components
│   │   ├── 📂 pages/                    # Main views (Home, New Activity)
│   │   ├── 📂 services/                 # Axios/Fetch API configuration
│   │   ├── App.jsx                      # Main routing and structure
│   │   ├── index.css                    # Global styles
│   │   └── main.jsx                     # React entry point
│   ├── .env.example                     # Environment variables template
│   ├── package.json                     # Scripts and JS dependencies
│   └── vite.config.js                   # Vite configuration
├── LICENSE
├── README.md                            # Main documentation
└── .gitignore                           # Files excluded from version control
```

## 📜 **License**

This project is under the **MIT** license.

## 🧑‍💻 Author

**Piter Gomes** — Computer Science Student (5th Semester) & Full-Stack Developer

📧 [Email](mailto:piterg.bio@gmail.com) | 💼 [LinkedIn](https://www.linkedin.com/in/piter-gomes-4a39281a1/) | 💻 [GitHub](https://github.com/pitercoding) | 🌐 [Portfolio](https://portfolio-pitergomes.vercel.app/)