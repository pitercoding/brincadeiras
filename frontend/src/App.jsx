import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import "./index.css";
import Home from "./pages/Home";
import NovaAtividade from "./pages/NovaAtividade";
import DetalheAtividade from "./pages/DetalheAtividade";

function App() {
  return (
    <BrowserRouter>
      <header className="header">
        <h1 className="header-logo">
          <img 
            src="/gis-de-cera.png" 
            alt="Logo Brincadeiras" 
            className="logo-img" 
          />
          Brincadeiras
        </h1>
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
            <Route path="/nova" element={<NovaAtividade />} />
            <Route path="/atividades/:id" element={<DetalheAtividade />} />
          </Routes>
        </div>
      </main>
    </BrowserRouter>
  );
}

export default App;