import { useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

function NovaAtividade() {
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [materiais, setMateriais] = useState("");
  const [faixaEtaria, setFaixaEtaria] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  // Validação
  const validarCampos = () => {
    if (!titulo.trim()) {
      toast.error("Por favor, informe um título 📝");
      return false;
    }
    if (!descricao.trim()) {
      toast.error("A descrição não pode estar vazia 💡");
      return false;
    }
    if (!materiais.trim()) {
      toast.error("Adicione pelo menos um material 🎨");
      return false;
    }
    if (!faixaEtaria.trim()) {
      toast.error("Informe a faixa etária 🧒");
      return false;
    }
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validarCampos()) return;

    setLoading(true);

    try {
      const res = await api.post("/atividades", {
        titulo: titulo.trim(),
        descricao: descricao.trim(),
        materiais: materiais.split(",").map(m => m.trim()).filter(m => m),
        faixaEtaria: faixaEtaria.trim(),
      });

      toast.success("Atividade criada com sucesso!");
      setTitulo("");
      setDescricao("");
      setMateriais("");
      setFaixaEtaria("");

      navigate("/", { state: { novaAtividade: res.data } });
    } catch (err) {
      console.error(err.response?.data);
      toast.error(err.response?.data?.message || "Erro ao criar a atividade");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-container">
      <h1>Nova Atividade</h1>

      <form onSubmit={handleSubmit}>
        <input
          placeholder="Título"
          value={titulo}
          onChange={(e) => setTitulo(e.target.value)}
        />

        <textarea
          placeholder="Descrição"
          value={descricao}
          onChange={(e) => setDescricao(e.target.value)}
          rows={4}
        />

        <input
          placeholder="Materiais (separados por vírgula)"
          value={materiais}
          onChange={(e) => setMateriais(e.target.value)}
        />

        {/* SELECT DE FAIXA ETÁRIA */}
        <select
          value={faixaEtaria}
          onChange={(e) => setFaixaEtaria(e.target.value)}
        >
          <option value="">Selecione a faixa etária</option>
          <option value="0-2 anos">0-2 anos</option>
          <option value="2-4 anos">2-4 anos</option>
          <option value="4-6 anos">4-6 anos</option>
          <option value="6-8 anos">6-8 anos</option>
          <option value="8-10 anos">8-10 anos</option>
          <option value="10-14 anos">10-14 anos</option>
        </select>

        <button type="submit" disabled={loading}>
          {loading ? "Salvando..." : "Criar Atividade"}
        </button>
      </form>
    </div>
  );
}

export default NovaAtividade;
