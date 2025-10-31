import { useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

function NovaAtividade() {
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [materiais, setMateriais] = useState("");
  const [faixaEtaria, setFaixaEtaria] = useState("");
  const [erro, setErro] = useState(null);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/atividades", {
        titulo,
        descricao,
        materiais: materiais.split(",").map((m) => m.trim()),
        faixaEtaria,
      });

      // Redireciona para Home
      navigate("/", { state: { novaAtividade: res.data } });
    } catch (err) {
      console.error(err);
      setErro("Não foi possível criar a atividade.");
    }
  };

  return (
    <div className="form-container">
      <h1>Nova Atividade</h1>
      {erro && <p className="error">{erro}</p>}
      <form onSubmit={handleSubmit}>
        <input placeholder="Título" value={titulo} onChange={e => setTitulo(e.target.value)} />
        <textarea placeholder="Descrição" value={descricao} onChange={e => setDescricao(e.target.value)} />
        <input placeholder="Materiais (separados por vírgula)" value={materiais} onChange={e => setMateriais(e.target.value)} />
        <input placeholder="Faixa etária" value={faixaEtaria} onChange={e => setFaixaEtaria(e.target.value)} />
        <button type="submit">Criar Atividade</button>
      </form>
    </div>
  );
}

export default NovaAtividade;