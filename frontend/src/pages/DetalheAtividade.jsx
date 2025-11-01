// src/pages/DetalheAtividade.jsx
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";

function DetalheAtividade() {
  const { id } = useParams(); // pega o ID da URL
  const navigate = useNavigate();
  const [atividade, setAtividade] = useState(null);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState(null);

  useEffect(() => {
    api.get(`/atividades/${id}`)
      .then((res) => {
        setAtividade(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Erro ao buscar atividade:", err);
        setErro("Não foi possível carregar a atividade 😢");
        setLoading(false);
      });
  }, [id]);

  if (loading) return <p>Carregando detalhes...</p>;
  if (erro) return <p>{erro}</p>;
  if (!atividade) return <p>Atividade não encontrada.</p>;

  return (
    <div className="detalhe-container">
      <div className="detalhe-card">
        <h2 className="detalhe-titulo">{atividade.titulo}</h2>
        <p className="detalhe-desc">{atividade.descricao}</p>

        {atividade.materiais && atividade.materiais.length > 0 && (
          <div className="detalhe-materiais">
            <h4>🧺 Materiais necessários:</h4>
            <ul>
              {atividade.materiais.map((m, i) => (
                <li key={i}>{m}</li>
              ))}
            </ul>
          </div>
        )}

        {atividade.faixaEtaria && (
          <p className="detalhe-faixa">👶 Faixa etária: {atividade.faixaEtaria}</p>
        )}

        <button className="btn-voltar" onClick={() => navigate(-1)}>
          ⬅️ Voltar
        </button>
      </div>
    </div>
  );
}

export default DetalheAtividade;