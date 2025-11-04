import { useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

function NovaAtividade() {
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [materiais, setMateriais] = useState("");
  const [faixaEtaria, setFaixaEtaria] = useState("");
  const [erro, setErro] = useState(null);
  const [loading, setLoading] = useState(false); // indica carregamento (react-toastify)

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true); // inicia carregamento (react-toastify)
    setErro(null);

    try {
      const res = await api.post("/atividades", {
        titulo,
        descricao,
        materiais: materiais.split(",").map((m) => m.trim()),
        faixaEtaria,
      });

      // nova atividade: mostra toast de sucesso
      toast.success("Atividade criada com sucesso!");

      // limpa campos
      setTitulo("");
      setDescricao("");
      setMateriais("");
      setFaixaEtaria("");

      // redireciona para Home
      navigate("/", { state: { novaAtividade: res.data } });
    } catch (err) {
      console.error(err);
      setErro("Não foi possível criar a atividade.");

      // novo: toast de erro
      toast.error("Erro ao criar a atividade. Tente novamente.");
    } finally {
      setLoading(false); // finaliza o estado de carregamento(react-toastify)
    }
  };

  return (
    <div className="form-container">
      <h1>Nova Atividade</h1>
      {erro && <p className="error">{erro}</p>}
      <form onSubmit={handleSubmit}>
        <input
          placeholder="Título"
          value={titulo}
          onChange={(e) => setTitulo(e.target.value)}
          required
        />
        <textarea
          placeholder="Descrição"
          value={descricao}
          onChange={(e) => setDescricao(e.target.value)}
          required
        />
        <input
          placeholder="Materiais (separados por vírgula)"
          value={materiais}
          onChange={(e) => setMateriais(e.target.value)}
          required
        />
        <input
          placeholder="Faixa etária"
          value={faixaEtaria}
          onChange={(e) => setFaixaEtaria(e.target.value)}
          required
        />

        {/* botão mostra carregamento visual */}
        <button type="submit" disabled={loading}>
          {loading ? "Salvando..." : "Criar Atividade"}
        </button>
      </form>
    </div>
  );
}

export default NovaAtividade;