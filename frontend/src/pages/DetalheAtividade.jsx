import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";
import { errorToast, infoToast, successToast } from "../utils/toast";

function DetalheAtividade() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [atividade, setAtividade] = useState(null);
  const [editando, setEditando] = useState(false);
  const [form, setForm] = useState({});
  const [loading, setLoading] = useState(true);
  const [showConfirm, setShowConfirm] = useState(false);

  useEffect(() => {
    api
      .get(`/atividades/${id}`)
      .then((res) => {
        setAtividade(res.data);
        setForm(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Erro ao buscar atividade:", err);
        errorToast("Não foi possível carregar a atividade 😢");
        setLoading(false);
      });
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const salvarEdicao = async () => {
    // Validação dos campos obrigatórios
    if (!form.titulo?.trim()) {
      errorToast("O título não pode estar vazio 📝");
      return;
    }

    if (!form.descricao?.trim()) {
      errorToast("A descrição é obrigatória 💡");
      return;
    }

    if (!form.faixaEtaria?.trim()) {
      errorToast("Informe a faixa etária 🧒");
      return;
    }

    if (
      !Array.isArray(form.materiais) ||
      form.materiais.length === 0 ||
      !form.materiais[0].trim()
    ) {
      errorToast("Adicione ao menos um material 🎨");
      return;
    }

    try {
      const res = await api.put(`/atividades/${id}`, form);
      setAtividade(res.data);
      setEditando(false);
      successToast("Atividade atualizada com sucesso! 🎉");
    } catch (err) {
      console.error(err);
      errorToast("Erro ao atualizar a atividade 😢");
    }
  };

  const deletarAtividade = async () => {
    try {
      await api.delete(`/atividades/${id}`);
      successToast("Atividade excluída com sucesso!");
      navigate("/");
    } catch (err) {
      console.error(err);
      errorToast("Erro ao excluir a atividade 😢");
    } finally {
      setShowConfirm(false);
    }
  };

  if (loading) return <p>Carregando detalhes...</p>;
  if (!atividade) {
    infoToast("Atividade não encontrada.");
    return <p>Atividade não encontrada.</p>;
  }

  return (
    <div className="detalhe-container">
      <div className="detalhe-card">
        {editando ? (
          <>
            <input
              type="text"
              name="titulo"
              value={form.titulo}
              onChange={handleChange}
              placeholder="Título"
              className="form-input"
            />
            <textarea
              name="descricao"
              value={form.descricao}
              onChange={handleChange}
              placeholder="Descrição"
              className="form-textarea"
            />
            <input
              type="text"
              name="materiais"
              value={form.materiais.join(", ")}
              onChange={(e) =>
                setForm({ ...form, materiais: e.target.value.split(",") })
              }
              placeholder="Materiais (separe por vírgulas)"
              className="form-input"
            />
            <input
              type="text"
              name="faixaEtaria"
              value={form.faixaEtaria}
              onChange={handleChange}
              placeholder="Faixa Etária"
              className="form-input"
            />

            <div className="detalhe-botoes">
              <button className="btn-voltar" onClick={() => setEditando(false)}>
                Cancelar
              </button>
              <button className="btn-editar" onClick={salvarEdicao}>
                💾 Salvar
              </button>
            </div>
          </>
        ) : (
          <>
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
              <button className="btn-editar" onClick={() => setEditando(true)}>
                ✏️ Editar
              </button>
              <button
                className="btn-excluir"
                onClick={() => setShowConfirm(true)}
              >
                🗑️ Excluir
              </button>
            </div>
          </>
        )}
      </div>

      {/* Modal de confirmação */}
      {showConfirm && (
        <div className="modal-overlay">
          <div className="modal">
            <p>Deseja realmente excluir esta atividade?</p>
            <div className="modal-actions">
              <button className="btn-confirm" onClick={deletarAtividade}>
                Sim, excluir
              </button>
              <button
                className="btn-cancel"
                onClick={() => setShowConfirm(false)}
              >
                Cancelar
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default DetalheAtividade;
