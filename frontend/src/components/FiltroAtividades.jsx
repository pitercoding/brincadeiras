import { useState } from "react";
import { infoToast } from "../utils/toast";

function FiltroAtividades({ onFiltrar, onLimpar }) {
  const [busca, setBusca] = useState("");
  const [faixaEtaria, setFaixaEtaria] = useState("");
  const [material, setMaterial] = useState("");

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      aplicarFiltros();
    }
  };

  const aplicarFiltros = () => {
    if (!busca && !faixaEtaria && !material) {
      infoToast("Adicione pelo menos um filtro para pesquisar 😉");
    }
    onFiltrar({ busca, faixaEtaria, material });
  };

  const limparFiltros = () => {
    setBusca("");
    setFaixaEtaria("");
    setMaterial("");
    onLimpar();
  };

  return (
    <div className="filtro-container">

      <select
        value={faixaEtaria}
        onChange={(e) => setFaixaEtaria(e.target.value)}
        className="filtro-select"
      >
        <option value="">Todas as idades</option>
        <option value="0-2">0-2 anos</option>
        <option value="2-4">2-4 anos</option>
        <option value="4-6">4-6 anos</option>
        <option value="6-8">6-8 anos</option>
        <option value="8-10">8-10 anos</option>
        <option value="10-14">10-14 anos</option>
      </select>

      <input
        type="text"
        placeholder="Filtrar por título..."
        value={busca}
        onChange={(e) => setBusca(e.target.value)}
        onKeyDown={handleKeyPress}
        className="filtro-input"
      />
    
      <input
        type="text"
        placeholder="Filtrar por material..."
        value={material}
        onChange={(e) => setMaterial(e.target.value)}
        onKeyDown={handleKeyPress}
        className="filtro-input"
      />

      <button onClick={aplicarFiltros} className="filtro-btn">
        Aplicar
      </button>

      <button onClick={limparFiltros} className="filtro-btn limpar">
        Limpar
      </button>
    </div>
  );
}

export default FiltroAtividades;