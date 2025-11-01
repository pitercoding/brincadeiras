import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import api from "../services/api";
import { Link } from "react-router-dom";

function Home() {
  const [atividades, setAtividades] = useState([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState(null);
  const location = useLocation();

  useEffect(() => {
    api.get("/atividades")
      .then((res) => {
        let dados = res.data;
        if (location.state?.novaAtividade) {
          dados = [location.state.novaAtividade, ...dados];
        }
        setAtividades(dados);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        setErro("Não foi possível carregar as atividades 😢");
        setLoading(false);
      });
  }, [location.state]);

  if (loading) return <p>Carregando atividades...</p>;
  if (erro) return <p>{erro}</p>;

  return (
    <div className="grid-container">
      {atividades.length > 0 ? (
        atividades.map((a) => (
          <Link key={a.id} to={`/atividades/${a.id}`} className="card-atividade">
            <h2 className="card-title">{a.titulo}</h2>
            <p className="card-desc">{a.descricao}</p>
            <p className="card-meta">🎨 Materiais: {a.materiais.join(", ")}</p>
            <p className="card-meta">👶 Faixa etária: {a.faixaEtaria}</p>
          </Link>
        ))
      ) : (
        <p>Nenhuma atividade encontrada.</p>
      )}
    </div>
  );
}

export default Home;