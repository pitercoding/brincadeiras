function CardAtividade({ atividade }) {
  return (
    <div className="card-atividade">
      <h2 className="card-title">{atividade.titulo}</h2>
      <p className="card-desc">{atividade.descricao}</p>
      <p className="card-meta">🎨 Materiais: {atividade.materiais.join(", ")}</p>
      <p className="card-meta">👶 Faixa etária: {atividade.faixaEtaria}</p>
    </div>
  );
}

export default CardAtividade;
