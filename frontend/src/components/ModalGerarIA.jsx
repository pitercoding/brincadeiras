import { useState } from "react";
import { gerarAtividade } from "../services/api";

function ModalGerarIA({ isOpen, onClose }) {
  const [faixaEtaria, setFaixaEtaria] = useState("");
  const [descricaoUsuario, setDescricaoUsuario] = useState("");
  const [atividadeGerada, setAtividadeGerada] = useState(null);
  const [loadingIA, setLoadingIA] = useState(false);
  const [error, setError] = useState("");

  if (!isOpen) return null;

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setAtividadeGerada(null);

    if (!faixaEtaria) {
      setError("⚠️ Selecione uma faixa etária para continuar!");
      return;
    }

    setLoadingIA(true);
    try {
      const novaAtividade = await gerarAtividade({ faixaEtaria, descricaoUsuario });
      setAtividadeGerada(novaAtividade);
    } catch (err) {
      console.error(err);
      setError(err.message || "Falha ao gerar atividade com IA 😢");
    } finally {
      setLoadingIA(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2>✨ Gerar ideia com IA</h2>

        {error && (
          <div className="modal-error-message">
            <p>{error}</p>
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <label>Faixa Etária:</label>
          <select value={faixaEtaria} onChange={(e) => setFaixaEtaria(e.target.value)}>
            <option value="">Selecione...</option>
            <option value="0-2">0-2 anos</option>
            <option value="2-4">2-4 anos</option>
            <option value="4-6">4-6 anos</option>
            <option value="6-8">6-8 anos</option>
            <option value="8-10">8-10 anos</option>
            <option value="10-14">10-14 anos</option>
          </select>

          <label>Alguma sugestão extra (opcional):</label>
          <textarea
            value={descricaoUsuario}
            onChange={(e) => setDescricaoUsuario(e.target.value)}
            placeholder="ex: quero algo calmo ou só com papel e lápis"
          />

          <div className="modal-actions">
            <button type="button" onClick={onClose} className="btn-cancelar" disabled={loadingIA}>
              Cancelar
            </button>
            <button type="submit" className="btn-gerar" disabled={loadingIA}>
              {loadingIA ? "Gerando..." : "Gerar 🎨"}
            </button>
          </div>
        </form>

        {atividadeGerada && (
          <div className="card-atividade card-ia" style={{ marginTop: "20px" }}>
            <h3 className="card-title">{atividadeGerada.titulo}</h3>
            <p className="card-desc">{atividadeGerada.descricao}</p>
            <p className="card-meta">🎨 Materiais: {atividadeGerada.materiais.join(", ")}</p>
            <p className="card-meta">👶 Faixa etária: {atividadeGerada.faixaEtaria}</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default ModalGerarIA;