import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";
import { errorToast, infoToast, successToast } from "../utils/toast";

function DetalheAtividade() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [atividade, setAtividade] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get(`/atividades/${id}`)
      .then((res) => {
        setAtividade(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Erro ao buscar atividade:", err);
        errorToast("Não foi possível carregar a atividade 😢");
        setLoading(false);
      });
  }, [id]);

  // 🖊️ Placeholder: função para editar (PUT)
  // const editarAtividade = async (dadosAtualizados) => {
  //   try {
  //     const res = await api.put(`/atividades/${id}`, dadosAtualizados);
  //     setAtividade(res.data);
  //     successToast("Atividade atualizada com sucesso!");
  //   } catch (err) {
  //     console.error(err);
  //     errorToast("Erro ao atualizar a atividade 😢");
  //   }
  // };

  // 🗑️ Placeholder: função para deletar (DELETE)
  // const deletarAtividade = async () => {
  //   if (!window.confirm("Deseja realmente excluir esta atividade?")) return;
  //   try {
  //     await api.delete(`/atividades/${id}`);
  //     successToast("Atividade excluída com sucesso!");
  //     navigate("/");
  //   } catch (err) {
  //     console.error(err);
  //     errorToast("Erro ao excluir a atividade 😢");
  //   }
  // };

  if (loading) return <p>Carregando detalhes...</p>;
  if (!atividade) {
    infoToast("Atividade não encontrada.");
    return <p>Atividade não encontrada.</p>;
  }

  return (
    <div className="detalhe-container">
      <div className="detalhe-card">
        <h2 className="detalhe-titulo">{atividade.titulo}</h2>
        <p className="detalhe-desc">{atividade.descricao}</p>

        {atividade.materiais?.length > 0 && (
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
          <p className="detalhe-faixa">
            👶 Faixa etária: {atividade.faixaEtaria}
          </p>
        )}

        <div className="detalhe-botoes">
          <button className="btn-voltar" onClick={() => navigate(-1)}>
            ⬅️ Voltar
          </button>

          {/* 🖊️ Botão futuro para editar */}
          {/* <button className="btn-editar" onClick={() => editarAtividade({...})}>
            Editar
          </button> */}

          {/* 🗑️ Botão futuro para excluir */}
          {/* <button className="btn-excluir" onClick={deletarAtividade}>
            Excluir
          </button> */}
        </div>
      </div>
    </div>
  );
}

export default DetalheAtividade;