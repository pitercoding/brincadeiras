// src/App.jsx
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import "./index.css";
import Home from "./pages/Home";

function App() {
  return (
    <BrowserRouter>
      <header className="header">
        <h1>🌸 Brincadeiras</h1>
        <nav>
          <Link to="/">Início</Link>
          <Link to="/nova">Cadastrar</Link>
        </nav>
      </header>

      <main className="main-content">
        <div className="background-layer"></div>

        <div className="content">
          <Routes>
            <Route path="/" element={<Home />} />
            {/* Rota /nova vazia por enquanto */}
            <Route path="/nova" element={<div>Nova Atividade</div>} />
          </Routes>
        </div>
      </main>
    </BrowserRouter>
  );
}

export default App;