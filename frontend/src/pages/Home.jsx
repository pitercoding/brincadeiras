import { useEffect, useState } from "react";
import { useLocation, Link } from "react-router-dom";
import api from "../services/api";
import FiltroAtividades from "../components/FiltroAtividades";
import ModalGerarIA from "../components/ModalGerarIA";
import { errorToast, infoToast } from "../utils/toast";

function Home() {
  const [atividades, setAtividades] = useState([]);
  const [filtradas, setFiltradas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loadingIA, setLoadingIA] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const location = useLocation();

  useEffect(() => {
    api
      .get("/atividades")
      .then((res) => {
        let dados = res.data;
        if (location.state?.novaAtividade) {
          dados = [location.state.novaAtividade, ...dados];
        }
        setAtividades(dados);
        setFiltradas(dados);
        setLoading(false);
      })
      .catch((err) => {
        console.error(err);
        errorToast("Não foi possível carregar as atividades 😢");
        setLoading(false);
      });
  }, [location.state]);

  const filtrarAtividades = ({ busca, faixaEtaria, material }) => {
    const resultado = atividades.filter((a) => {
      const tituloMatch = a.titulo
        .toLowerCase()
        .includes(busca.toLowerCase());
      const materialMatch = material
        ? a.materiais.some((m) =>
            m.toLowerCase().includes(material.toLowerCase())
          )
        : true;

      const faixaMatch = faixaEtaria
        ? (() => {
            const [min, max] = faixaEtaria.split("-").map(Number);
            const [faixaMin, faixaMax] = a.faixaEtaria
              .replace(" anos", "")
              .split("-")
              .map(Number);
            return faixaMax >= min && faixaMin <= max;
          })()
        : true;

      return tituloMatch && materialMatch && faixaMatch;
    });

    setFiltradas(resultado);

    if (resultado.length === 0) {
      infoToast("Nenhuma atividade encontrada com esses filtros.");
    }
  };

  const limparFiltros = () => {
    setFiltradas(atividades);
  };

  const gerarAtividadeIA = async (dadosIA) => {
    setLoadingIA(true);
    try {
      const res = await api.post("/atividades/gerar", dadosIA);
      const novaAtividade = res.data;

      setAtividades((prev) => [novaAtividade, ...prev]);
      setFiltradas((prev) => [novaAtividade, ...prev]);
      infoToast("Atividade gerada com sucesso! 🎉");
    } catch (err) {
      console.error(err);
      errorToast("Falha ao gerar atividade com IA 😢");
    } finally {
      setLoadingIA(false);
    }
  };

  if (loading) return <p>Carregando atividades...</p>;

  return (
    <div className="home-container">
      <button
        onClick={() => setShowModal(true)}
        className="btn-gerar-ia"
        disabled={loadingIA}
      >
        {loadingIA ? "Gerando..." : "✨ Gerar Ideia com IA"}
      </button>

      <FiltroAtividades onFiltrar={filtrarAtividades} onLimpar={limparFiltros} />

      <ModalGerarIA
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        onConfirm={gerarAtividadeIA}
      />

      <div className="grid-container">
        {filtradas.length > 0 ? (
          filtradas.map((a) => (
            <Link to={`/atividades/${a.id}`} className="card-atividade" key={a.id}>
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
    </div>
  );
}

export default Home;