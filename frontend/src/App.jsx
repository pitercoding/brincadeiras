import { BrowserRouter, Routes, Route, NavLink } from "react-router-dom";
import "./index.css";

import Home from "./pages/Home";
import NovaAtividade from "./pages/NovaAtividade";
import DetalheAtividade from "./pages/DetalheAtividade";
import Footer from "./components/Footer";

import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function AppWrapper() {
  return (
    <div className="app-container">
      <header className="header">
        <NavLink to="/" className="header-logo">
          <img
            src="/gis-de-cera.png"
            alt="Logo Brincadeiras"
            className="logo-img"
          />
          Brincadeiras
        </NavLink>
        <nav className="header-nav">
          <NavLink
            to="/"
            end
            className={({ isActive }) =>
              `nav-link ${isActive ? "active" : ""}`
            }
          >
            InÍcio
          </NavLink>
          <NavLink
            to="/nova"
            className={({ isActive }) =>
              `nav-link ${isActive ? "active" : ""}`
            }
          >
            Cadastrar
          </NavLink>
        </nav>
      </header>

      {/* ToastContainer global: qualquer componente pode disparar toasts */}
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />

      <main className="main-content">
        <div className="background-layer"></div>

        <div className="content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/nova" element={<NovaAtividade />} />
            <Route path="/atividades/:id" element={<DetalheAtividade />} />
          </Routes>
        </div>
      </main>

      <Footer />
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <AppWrapper />
    </BrowserRouter>
  );
}

export default App;
