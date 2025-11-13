import { useState } from "react";

function ModalGerarIA({ isOpen, onClose, onConfirm }) {
  const [faixaEtaria, setFaixaEtaria] = useState("4-6");
  const [tipo, setTipo] = useState("brincadeira criativa");
  const [materiais, setMateriais] = useState("papel, lápis de cor");

  if (!isOpen) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    const listaMateriais = materiais
      .split(",")
      .map((m) => m.trim())
      .filter(Boolean);

    onConfirm({
      faixaEtaria,
      tipo,
      materiais: listaMateriais,
    });
    onClose();
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>✨ Gerar Ideia com IA</h2>
        <form onSubmit={handleSubmit}>
          <label>Faixa Etária:</label>
          <select
            value={faixaEtaria}
            onChange={(e) => setFaixaEtaria(e.target.value)}
          >
            <option value="2-4">2-4 anos</option>
            <option value="4-6">4-6 anos</option>
            <option value="6-8">6-8 anos</option>
            <option value="8-10">8-10 anos</option>
          </select>

          <label>Tipo de Atividade:</label>
          <input
            type="text"
            value={tipo}
            onChange={(e) => setTipo(e.target.value)}
            placeholder="ex: atividade artística"
          />

          <label>Materiais disponíveis:</label>
          <input
            type="text"
            value={materiais}
            onChange={(e) => setMateriais(e.target.value)}
            placeholder="ex: papel, cola, tesoura"
          />

          <div className="modal-actions">
            <button type="button" onClick={onClose} className="btn-cancelar">
              Cancelar
            </button>
            <button type="submit" className="btn-gerar">
              Gerar 🎨
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default ModalGerarIA;
