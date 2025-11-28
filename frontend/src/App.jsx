import { BrowserRouter, Routes, Route, Link, useLocation } from "react-router-dom";
import "./index.css";

import Home from "./pages/Home";
import NovaAtividade from "./pages/NovaAtividade";
import DetalheAtividade from "./pages/DetalheAtividade";
import Footer from "./components/Footer";

// adicionado: ToastContainer e css do react-toastify
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

// Wrapper para usar hook useLocation no topo
function AppWrapper() {
  const location = useLocation();
  const isFixedFooter = location.pathname === "/";

  return (
    <div className="app-container">
      <header className="header">
        <Link to="/" className="header-logo">
          <img
            src="/gis-de-cera.png"
            alt="Logo Brincadeiras"
            className="logo-img"
          />
          Brincadeiras
        </Link>
        <nav>
          <Link to="/">Início</Link>
          <Link to="/nova">Cadastrar</Link>
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

      {/* passa prop isFixedFooter para o Footer */}
      <Footer isFixedFooter={isFixedFooter} />
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