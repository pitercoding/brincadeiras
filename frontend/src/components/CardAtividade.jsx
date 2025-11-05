function CardAtividade({ atividade }) {
  return (
    <div className="card-atividade">
      <div className="card-header">
        <h2 className="card-title">{atividade.titulo}</h2>
      </div>

      <p className="card-desc">{atividade.descricao}</p>

      <div className="card-info">
        <p className="card-meta">
          <span className="icon">🎨</span>
          {atividade.materiais.join(", ")}
        </p>
        <p className="card-meta">
          <span className="icon">👶</span>
          {atividade.faixaEtaria}
        </p>
      </div>
    </div>
  );
}

export default CardAtividade;

