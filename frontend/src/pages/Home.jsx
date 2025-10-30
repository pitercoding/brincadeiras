// src/pages/Home.jsx
function Home() {
  const atividades = [
    {
      id: 1,
      titulo: "Pintura com guache",
      descricao: "Atividade de pintura livre para estimular a criatividade.",
      materiais: ["Guache", "Papel", "Pincel"],
      faixaEtaria: "3-6 anos",
    },
    {
      id: 2,
      titulo: "Massinha de modelar",
      descricao: "Exploração sensorial e coordenação motora fina.",
      materiais: ["Massinha", "Espátula"],
      faixaEtaria: "2-5 anos",
    },
  ];

  return (
    <div className="grid-container">
      {atividades.map((a) => (
        <div key={a.id} className="card-atividade">
          <h2 className="card-title">{a.titulo}</h2>
          <p className="card-desc">{a.descricao}</p>
          <p className="card-meta">🎨 Materiais: {a.materiais.join(", ")}</p>
          <p className="card-meta">👶 Faixa etária: {a.faixaEtaria}</p>
        </div>
      ))}
    </div>
  );
}

export default Home;