import axios from "axios";

/* URL backend: pega do .env da Vercel */
const api = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_URL,
});

/* Chama o endpoint no Spring Boot para gerar uma atividade com a IA. */
export const gerarAtividade = async (dados) => {
  try {
    const response = await api.post("/atividades/gerar", dados);
    const atividade = response.data;

    // Garantir ID temporário caso backend não retorne
    if (!atividade.id) {
      atividade.id = atividade.titulo + Date.now();
    }

    if (!atividade.materiais || atividade.materiais.length === 0) {
      atividade.materiais = ["Materiais comuns em casa"];
    }

    if (!atividade.tipo) {
      atividade.tipo = "IA";
    }

    return atividade;
  } catch (error) {
    throw new Error(
      error.response?.data?.message ||
        `Falha ao gerar atividade com IA: ${error.message}`
    );
  }
};

export default api;