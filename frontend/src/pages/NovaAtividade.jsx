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
        materiais: materiais.split(",").map((m) => m.trim()),
        faixaEtaria: faixaEtaria.trim(),
      });

      toast.success("Atividade criada com sucesso!");
      setTitulo("");
      setDescricao("");
      setMateriais("");
      setFaixaEtaria("");

      navigate("/", { state: { novaAtividade: res.data } });
    } catch (err) {
      console.error(err);
      toast.error("Erro ao criar a atividade. Tente novamente.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-container max-w-md mx-auto p-6 bg-white rounded-2xl shadow-md mt-8">
      <h1 className="text-2xl font-bold mb-6 text-center text-gray-800">
        Nova Atividade
      </h1>

      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        <input
          placeholder="Título"
          value={titulo}
          onChange={(e) => setTitulo(e.target.value)}
          className="p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <textarea
          placeholder="Descrição"
          value={descricao}
          onChange={(e) => setDescricao(e.target.value)}
          rows={4}
          className="p-3 border border-gray-300 rounded-lg resize-none focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <input
          placeholder="Materiais (separados por vírgula)"
          value={materiais}
          onChange={(e) => setMateriais(e.target.value)}
          className="p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <input
          placeholder="Faixa etária"
          value={faixaEtaria}
          onChange={(e) => setFaixaEtaria(e.target.value)}
          className="p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <button
          type="submit"
          disabled={loading}
          className={`p-3 text-white rounded-lg transition-all duration-200 ${
            loading
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-blue-500 hover:bg-blue-600"
          }`}
        >
          {loading ? "Salvando..." : "Criar Atividade"}
        </button>
      </form>
    </div>
  );
}

export default NovaAtividade;
