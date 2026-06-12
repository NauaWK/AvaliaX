const token = localStorage.getItem("token");
if (!token) {
    window.location.href = "login.html"
}

const btnLogout = document.getElementById("logout")

btnLogout.addEventListener("click", logout)

function logout() {
    localStorage.removeItem("token")

    window.location.href = "login.html"
}


//Nome usuario
const payload = JSON.parse(atob(token.split('.')[1]));

const nome = payload.sub;

const nomeUser = document.getElementById("info-user")

nomeUser.innerText = nome

const API_URL = "http://localhost:8080/api/avaliacoes";

let avaliacoes = [];
let avaliacaoAtual = null;

const listaAvaliacoes = document.getElementById("listaAvaliacoes");
const buscarAvaliacao = document.getElementById("buscarAvaliacao");
const filtroResultado = document.getElementById("filtroResultado");

const modal = document.getElementById("modalAvaliacao");
const fecharModal = document.getElementById("fecharModal");

const modoDetalhes = document.getElementById("modoDetalhes");
const btnEditar = document.getElementById("btnEditar");

const respostas = [
  { valor: "SIM", texto: "Sim" },
  { valor: "NAO", texto: "Não" },
  { valor: "NAO_SEI", texto: "Não sei" }
];

/*
  IMPORTANTE:
  Coloque aqui exatamente os nomes que existem no banco na coluna descricao da tabela sintomas.
*/
const todosSintomas = [
    "Deficiência intelectual",
    "Face alongada/orelhas",
    "Macroorquidismo",
    "Hipermobilidade articular",
    "Dificuldades de aprendizagem",
    "Déficit de atenção",
    "Movimentos repetitivos",
    "Atraso na fala",
    "Hiperatividade",
    "Evita contato visual",
    "Evita contato físico",
    "Agressividade"
];

carregarAvaliacoes();

function pegarDataAvaliacao(avaliacao) {
    return (
        avaliacao.dataAvaliacao ||
        avaliacao.data ||
        avaliacao.createdAt ||
        avaliacao.criadoEm ||
        avaliacao.created_at ||
        avaliacao.date ||
        ""
    );
}

function ordenarAvaliacoesPorData(lista) {
    return lista.sort((a, b) => {
        const dataA = new Date(pegarDataAvaliacao(a));
        const dataB = new Date(pegarDataAvaliacao(b));

        return dataB - dataA;
    });
}
async function carregarAvaliacoes() {
    try {
        const tokenAtual = localStorage.getItem("token");

        const response = await fetch(API_URL, {
            headers: {
                Authorization: `Bearer ${tokenAtual}`
            }
        });

        console.log("Status GET lista:", response.status);

        if (response.status === 401) {
            alert("Sua sessão expirou. Faça login novamente.");
            localStorage.removeItem("token");
            window.location.href = "login.html";
            return;
        }

        if (!response.ok) {
            throw new Error("Erro ao buscar avaliações.");
        }

        avaliacoes = await response.json();

        ordenarAvaliacoesPorData(avaliacoes);

        atualizarResumo();
        renderizarAvaliacoes(avaliacoes);

    } catch (error) {
        console.error(error);
        listaAvaliacoes.textContent = "Erro ao carregar avaliações.";
    }
}

function renderizarAvaliacoes(lista) {

    listaAvaliacoes.innerHTML = "";

    if (!lista || lista.length === 0) {

        const mensagem = document.createElement("p");
        mensagem.classList.add("mensagem-vazia");
        mensagem.textContent = "Nenhuma avaliação encontrada.";

        listaAvaliacoes.appendChild(mensagem);
        return;
    }

    lista.forEach(avaliacao => {

        const card = document.createElement("div");
        card.classList.add("avaliacao-card");

        const nome = document.createElement("h3");
        nome.textContent = avaliacao.paciente;

        const data = document.createElement("div");
        data.classList.add("avaliacao-info");

        const dataLabel = document.createElement("span");
        dataLabel.textContent = "Data";

        const dataValor = document.createElement("strong");
        dataValor.textContent = formatarData(avaliacao.dataAvaliacao);

        data.appendChild(dataLabel);
        data.appendChild(dataValor);

        const usuario = document.createElement("div");
        usuario.classList.add("avaliacao-info");

        const usuarioLabel = document.createElement("span");
        usuarioLabel.textContent = "Usuário";

        const usuarioValor = document.createElement("strong");
        usuarioValor.textContent = avaliacao.usuario;

        usuario.appendChild(usuarioLabel);
        usuario.appendChild(usuarioValor);

        const badge = document.createElement("span");
        badge.classList.add("badge");

        if (avaliacao.resultado === "TESTE_INDICADO") {
            badge.classList.add("teste");
        } else {
            badge.classList.add("inconclusivo");
        }

        badge.textContent = formatarTexto(avaliacao.resultado);

        card.appendChild(nome);
        card.appendChild(data);
        card.appendChild(usuario);
        card.appendChild(badge);

        card.addEventListener("click", () => {
            abrirDetalhes(avaliacao.id);
        });

        listaAvaliacoes.appendChild(card);
    });
}
async function abrirDetalhes(id) {
    const tokenAtual = localStorage.getItem("token");

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${tokenAtual}`
            }
        });

        if (response.status === 401) {
            const erro = await response.json();
            console.log("Erro 401:", erro);
            alert("Token inválido ou expirado. Faça login novamente.");
            return;
        }

        if (!response.ok) {
            throw new Error("Erro ao buscar avaliação por ID.");
        }

        avaliacaoAtual = await response.json();

        preencherDetalhes(avaliacaoAtual);

        modoDetalhes.classList.remove("escondido");
        modal.classList.add("ativo");

    } catch (error) {
        console.error(error);
        alert("Erro ao abrir avaliação.");
    }
}

function preencherDetalhes(avaliacao) {
  document.getElementById("detPaciente").textContent = avaliacao.paciente;
  document.getElementById("detUsuario").textContent = avaliacao.usuario;
  document.getElementById("detData").textContent = formatarData(avaliacao.dataAvaliacao);
  document.getElementById("detOrigem").textContent = formatarTexto(avaliacao.origem);
  document.getElementById("detScore").textContent = avaliacao.score;
  document.getElementById("detResultado").textContent = formatarTexto(avaliacao.resultado);

  document.getElementById("detTesteDna").textContent = formatarTexto(avaliacao.testeDna);
  document.getElementById("detInteresseExame").textContent = formatarTexto(avaliacao.interesseExame);
  document.getElementById("detResultadoExame").textContent = formatarTexto(avaliacao.resultadoExame);
  document.getElementById("detAutismo").textContent = formatarTexto(avaliacao.diagnosticoAutismo);
  document.getElementById("detIrmaos").textContent = formatarTexto(avaliacao.possuiIrmaos);
  document.getElementById("detDeficiencia").textContent = formatarTexto(avaliacao.antecedentesDeficiencia);
  document.getElementById("detMenopausa").textContent = formatarTexto(avaliacao.antecedentesMenopausa);
  document.getElementById("detAtaxia").textContent = formatarTexto(avaliacao.antecedentesAtaxia);

  document.getElementById("detDetalhes").textContent =
    avaliacao.detalhes || "Nenhum detalhe informado.";

  const detSintomas = document.getElementById("detSintomas");
  detSintomas.innerHTML = "";

  if (!avaliacao.sintomas || avaliacao.sintomas.length === 0) {
    detSintomas.innerHTML = `<p class="mensagem-vazia">Nenhum sintoma marcado.</p>`;
    return;
  }

  avaliacao.sintomas.forEach(sintoma => {
    const tag = document.createElement("span");
    tag.classList.add("tag");
    tag.textContent = sintoma;
    detSintomas.appendChild(tag);
  });
}


btnEditar.addEventListener("click", () => {
    if (!avaliacaoAtual) return;

    window.location.href = `formAvaliacao.html?modo=editar&id=${avaliacaoAtual.id}`;
});


buscarAvaliacao.addEventListener("input", aplicarFiltros);
filtroResultado.addEventListener("change", aplicarFiltros);

function aplicarFiltros() {
    const texto = buscarAvaliacao.value.toLowerCase();
    const resultado = filtroResultado.value;

    const filtradas = avaliacoes.filter(avaliacao => {
        const nomePaciente = avaliacao.paciente || "";

        const pacienteCombina = nomePaciente.toLowerCase().includes(texto);
        const resultadoCombina = resultado === "" || avaliacao.resultado === resultado;

        return pacienteCombina && resultadoCombina;
    });

    ordenarAvaliacoesPorData(filtradas);
    renderizarAvaliacoes(filtradas);
}

function atualizarResumo() {
  document.getElementById("totalAvaliacoes").textContent = avaliacoes.length;

  document.getElementById("totalTesteIndicado").textContent =
    avaliacoes.filter(a => a.resultado === "TESTE_INDICADO").length;

  document.getElementById("totalInconclusivo").textContent =
    avaliacoes.filter(a => a.resultado === "INCONCLUSIVO").length;
}

fecharModal.addEventListener("click", fecharModalAvaliacao);

modal.addEventListener("click", event => {
  if (event.target === modal) {
    fecharModalAvaliacao();
  }
});

function fecharModalAvaliacao() {
  modal.classList.remove("ativo");
  avaliacaoAtual = null;
}

function formatarData(data) {
    if (!data) return "-";

    const dataLimpa = data.split("T")[0];
    const partes = dataLimpa.split("-");

    if (partes.length !== 3) return data;

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

function formatarTexto(texto) {
  if (!texto) return "-";

  const mapa = {
    SIM: "Sim",
    NAO: "Não",
    NAO_SEI: "Não sei",
    TESTE_INDICADO: "Teste indicado",
    INCONCLUSIVO: "Inconclusivo",
    RESPONSAVEL: "Responsável",
    PROFISSIONAL: "Profissional",
    MUTACAO_COMPLETA: "Mutação completa",
    PRE_MUTACAO: "Pré-mutação",
    ZONA_GRAY: "Zona gray",
    MOSAICISMO: "Mosaicismo",
    NEGATIVO_XF: "Negativo XF"
  };

  return mapa[texto] || texto;
}

async function tentarLerErro(response) {
  try {
    const erro = await response.json();

    if (typeof erro === "string") {
      return erro;
    }

    return Object.values(erro).join("\n");
  } catch {
    return "Erro ao processar a solicitação.";
  }
}
