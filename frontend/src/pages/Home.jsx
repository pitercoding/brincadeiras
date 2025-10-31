// src/pages/Home.jsx
import { useEffect, useState } from "react";
import api from "../services/api"; // importa o Axios configurado

function Home() {
  const [atividades, setAtividades] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState(null);

  useEffect(() => {
    // busca as atividades do backend ao carregar a página
    api.get("/atividades")
      .then((res) => {
        setAtividades(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Erro ao buscar atividades:", err);
        setErro("Não foi possível carregar as atividades 😢");
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Carregando atividades...</p>;
  if (erro) return <p>{erro}</p>;

  return (
    <div className="grid-container">
      {atividades.length > 0 ? (
        atividades.map((a) => (
          <div key={a.id} className="card-atividade">
            <h2 className="card-title">{a.titulo || a.nome}</h2>
            <p className="card-desc">{a.descricao}</p>
            {a.materiais && (
              <p className="card-meta">🎨 Materiais: {a.materiais.join(", ")}</p>
            )}
            {a.faixaEtaria && (
              <p className="card-meta">👶 Faixa etária: {a.faixaEtaria}</p>
            )}
          </div>
        ))
      ) : (
        <p>Nenhuma atividade encontrada.</p>
      )}
    </div>
  );
}

export default Home;