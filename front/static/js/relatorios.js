const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}
//nome
const payload = JSON.parse(atob(token.split('.')[1]));

const nome = payload.sub;

const nomeUser = document.getElementById("info-user")

nomeUser.innerText = nome

//logout
const btnLogout = document.getElementById("logout")

btnLogout.addEventListener("click", logout)

function logout() {
    localStorage.removeItem("token")

    window.location.href = "login.html"
}

const API_BASE = "http://localhost:8080/api";

let graficoSintomas = null;
let graficoResultados = null;
let relatorioAtual = null;

document.addEventListener("DOMContentLoaded", () => {
    carregarRelatorioGeral();

    document.getElementById("btnBuscarPaciente").addEventListener("click", buscarRelatorioPaciente);
    document.getElementById("btnImprimir").addEventListener("click", imprimirRelatorio);
    document.getElementById("btnPDF").addEventListener("click", baixarPDF);

    document.getElementById("cpfPaciente").addEventListener("input", (event) => {
        event.target.value = mascaraCPF(event.target.value);
    });

    document.getElementById("cpfPaciente").addEventListener("keydown", (event) => {
        if (event.key === "Enter") {
            buscarRelatorioPaciente();
        }
    });

    const logoutBtn = document.getElementById("logoutBtn");

    if (logoutBtn) {
        logoutBtn.addEventListener("click", () => {
            localStorage.removeItem("token");
            window.location.href = "login.html";
        });
    }
});

async function carregarRelatorioGeral() {
    try {
        mostrarMensagem("Carregando relatórios...", "info");

        const response = await fetch(`${API_BASE}/relatorios`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            mostrarMensagem("Erro ao carregar relatórios gerais.", "erro");
            return;
        }

        const dados = await response.json();

        preencherResumo(dados);
        preencherGenero(dados.sintomaMaisMarcadoPorGenero);
        preencherTabela(dados.rankingSintomas);

        criarGraficoSintomas(dados.rankingSintomas);
        criarGraficoResultados(dados);

        esconderMensagem();

    } catch (error) {
        console.error(error);
        mostrarMensagem("Erro de conexão com o servidor.", "erro");
    }
}

async function buscarRelatorioPaciente() {
    const cpfInput = document.getElementById("cpfPaciente");
    const cpf = cpfInput.value.replace(/\D/g, "");

    if (cpf.length !== 11) {
        mostrarMensagem("Digite um CPF válido com 11 números.", "erro");
        cpfInput.focus();
        return;
    }

    try {
        mostrarMensagem("Buscando relatório do paciente...", "info");

        const response = await fetch(`${API_BASE}/relatorios/paciente/${cpf}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            mostrarMensagem("Paciente não encontrado ou erro ao buscar relatório.", "erro");
            return;
        }

        const dados = await response.json();
        console.log("JSON relatório paciente:", dados);

        relatorioAtual = dados;
        preencherRelatorioPaciente(dados);

        esconderMensagem();

        document.getElementById("areaRelatorio").scrollIntoView({
            behavior: "smooth"
        });

    } catch (error) {
        console.error(error);
        mostrarMensagem("Erro de conexão ao buscar paciente.", "erro");
    }
}

function preencherResumo(dados) {
    document.getElementById("totalAvaliacoes").textContent = formatarNumero(dados.totalAvaliacoes);
    document.getElementById("porcentagemTesteIndicado").textContent = formatarPorcentagem(dados.porcentagemTesteIndicado);
    document.getElementById("porcentagemInconclusivo").textContent = formatarPorcentagem(dados.porcentagemInconclusivo);
    document.getElementById("scoreMedioAvaliacoes").textContent = formatarDecimal(dados.scoreMedioAvaliacoes);
}

function preencherGenero(dados) {
    if (!dados) {
        return;
    }

    document.getElementById("sintomaHomens").textContent = dados.sintomaHomens || "-";
    document.getElementById("quantidadeHomens").textContent = `${formatarNumero(dados.quantidadeHomens)} marcações`;

    document.getElementById("sintomaMulheres").textContent = dados.sintomaMulheres || "-";
    document.getElementById("quantidadeMulheres").textContent = `${formatarNumero(dados.quantidadeMulheres)} marcações`;
}

function preencherTabela(ranking) {
    const tbody = document.getElementById("tabelaSintomas");
    tbody.innerHTML = "";

    if (!ranking || ranking.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="2">Nenhum sintoma encontrado.</td>
            </tr>
        `;
        return;
    }

    ranking.forEach(item => {
        tbody.innerHTML += `
            <tr>
                <td>${item.nome}</td>
                <td>${formatarNumero(item.quantidade)}</td>
            </tr>
        `;
    });
}

function criarGraficoSintomas(ranking) {
    const ctx = document.getElementById("graficoSintomas");

    if (graficoSintomas) {
        graficoSintomas.destroy();
    }

    const labels = ranking && ranking.length > 0
        ? ranking.map(item => item.nome)
        : ["Sem dados"];

    const valores = ranking && ranking.length > 0
        ? ranking.map(item => item.quantidade)
        : [0];

    graficoSintomas = new Chart(ctx, {
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: "Quantidade",
                data: valores,
                backgroundColor: "#2196f3",
                borderColor: "#1976d2",
                borderWidth: 1,
                borderRadius: 8,
                maxBarThickness: 45
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,

            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    backgroundColor: "#0f172a",
                    titleColor: "#ffffff",
                    bodyColor: "#ffffff",
                    padding: 12,
                    callbacks: {
                        title: function(context) {
                            return context[0].label;
                        },
                        label: function(context) {
                            return `Quantidade: ${context.raw}`;
                        }
                    }
                }
            },

            scales: {
                x: {
                    grid: {
                        display: false
                    },
                    ticks: {
                        display: false
                    }
                },
                y: {
                    beginAtZero: true,
                    grid: {
                        color: "rgba(148, 163, 184, 0.2)"
                    },
                    ticks: {
                        precision: 0,
                        color: "#475569"
                    }
                }
            }
        }
    });
}

function criarGraficoResultados(dados) {
    const ctx = document.getElementById("graficoResultados");

    if (graficoResultados) {
        graficoResultados.destroy();
    }

    const testeIndicado = Number(dados.porcentagemTesteIndicado || 0);
    const inconclusivo = Number(dados.porcentagemInconclusivo || 0);
    const outros = Math.max(0, 100 - testeIndicado - inconclusivo);

    graficoResultados = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: ["Teste indicado", "Inconclusivo", "Outros"],
            datasets: [{
                data: [testeIndicado, inconclusivo, outros],
                backgroundColor: [
                    "#22c55e",
                    "#f59e0b",
                    "#cbd5e1"
                ],
                borderColor: "#ffffff",
                borderWidth: 4,
                hoverOffset: 8
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: "62%",
            plugins: {
                legend: {
                    position: "bottom",
                    labels: {
                        color: "#334155",
                        usePointStyle: true,
                        pointStyle: "circle",
                        padding: 18,
                        font: {
                            size: 12,
                            weight: "600"
                        }
                    }
                },
                tooltip: {
                    backgroundColor: "#0f172a",
                    titleColor: "#ffffff",
                    bodyColor: "#ffffff",
                    callbacks: {
                        label: function(context) {
                            return `${context.label}: ${formatarDecimal(context.raw)}%`;
                        }
                    }
                }
            }
        }
    });
}

function preencherRelatorioPaciente(dados) {
    document.getElementById("areaRelatorio").classList.remove("hidden");

    console.log("Dados recebidos no preencherRelatorioPaciente:", dados);

    const relatorio = dados.data || dados.relatorio || dados;

    const paciente = relatorio.dadosPaciente || {};
    const avaliacoes = relatorio.dadosAvaliacoesPaciente || {};
    const sintomasMaisPresentes = relatorio.sintomasMaisPresentes || [];
    const avaliacoesRecentes = relatorio.avaliacoesRecentes || [];

    setTexto("dataEmissao", new Date().toLocaleDateString("pt-BR"));

    // Dados do paciente
    setTexto("nomePaciente", paciente.nome || "-");
    setTexto("dataNascimentoPaciente", formatarData(paciente.dataNascimento));
    setTexto("idadePaciente", paciente.idade ? `${paciente.idade} anos` : "-");
    setTexto("generoPaciente", formatarGenero(paciente.genero));
    setTexto("nomeMae", paciente.nomeMae || "-");
    setTexto("nomePai", paciente.nomePai || "-");

    // Dados novos
    setTexto("responsavelPaciente", paciente.responsavel || "-");
    setTexto("telefonePaciente", formatarTelefone(paciente.telefone));
    setTexto("emailPaciente", paciente.email || "-");

    // Questionário clínico
    setTexto("testeDna", formatarResposta(paciente.testeDna));
    setTexto("interesseExame", formatarResposta(paciente.interesseExame));
    setTexto("resultadoExame", formatarResposta(paciente.resultadoExame));
    setTexto("diagnosticoAutismo", formatarResposta(paciente.diagnosticoAutismo));
    setTexto("possuiIrmaos", formatarResposta(paciente.possuiIrmaos));
    setTexto("antecedentesDeficiencia", formatarResposta(paciente.antecedentesDeficiencia));
    setTexto("antecedentesMenopausa", formatarResposta(paciente.antecedentesMenopausa));
    setTexto("antecedentesAtaxia", formatarResposta(paciente.antecedentesAtaxia));

    // Resumo das avaliações
    setTexto("totalAvaliacoesPaciente", formatarNumero(avaliacoes.totalAvaliacoes));
    setTexto("avaliacoesProfissional", formatarNumero(avaliacoes.avaliacoesProfissional));
    setTexto("avaliacoesResponsavel", formatarNumero(avaliacoes.avaliacoesResponsavel));
    setTexto("avaliacoesTesteIndicado", formatarNumero(avaliacoes.avaliacoesTesteIndicado));
    setTexto("avaliacoesInconclusivas", formatarNumero(avaliacoes.avaliacoesInconclusivas));
    setTexto("mediaScore", formatarDecimal(avaliacoes.mediaScore));

    preencherTop3(sintomasMaisPresentes);
    preencherAvaliacoesRecentes(avaliacoesRecentes);
}
function preencherTop3(top3) {
    const container = document.getElementById("top3Sintomas");
    container.innerHTML = "";

    if (!top3 || top3.length === 0) {
        container.innerHTML = "<p>Nenhum sintoma encontrado.</p>";
        return;
    }

    top3.slice(0, 3).forEach((item, index) => {
        container.innerHTML += `
            <div class="sintoma-item">
                <span>${index + 1}. ${escaparHTML(item.nome)}</span>
                <strong>${formatarNumero(item.quantidade)} marcações</strong>
            </div>
        `;
    });
}
function preencherAvaliacoesRecentes(avaliacoes) {
    const container = document.getElementById("avaliacoesRecentes");

    if (!container) {
        console.warn('Elemento com id "avaliacoesRecentes" não encontrado no HTML.');
        return;
    }

    container.innerHTML = "";

    if (!avaliacoes || avaliacoes.length === 0) {
        container.innerHTML = "<p>Nenhuma avaliação encontrada.</p>";
        return;
    }

    avaliacoes.forEach((avaliacao) => {
        const sintomas = avaliacao.sintomas || [];

        const sintomasHTML = sintomas.length > 0
            ? sintomas.map(sintoma => `
                <span class="sintoma-tag">${escaparHTML(sintoma)}</span>
            `).join("")
            : "<span class='sintoma-tag'>Nenhum sintoma informado</span>";

        const classeResultado = avaliacao.resultado === "TESTE_INDICADO"
            ? "indicado"
            : "inconclusivo";

        container.innerHTML += `
            <div class="avaliacao-card">
                <div class="avaliacao-topo">
                    <div>
                        <strong>${escaparHTML(avaliacao.profissional || "Profissional não informado")}</strong>
                        <span>${formatarOrigem(avaliacao.origem)}</span>
                    </div>

                    <span class="badge-resultado ${classeResultado}">
                        ${formatarResultado(avaliacao.resultado)}
                    </span>
                </div>

                <div class="avaliacao-info">
                    <div>
                        <span>Data</span>
                        <strong>${formatarData(avaliacao.data)}</strong>
                    </div>

                    <div>
                        <span>Score</span>
                        <strong>${formatarDecimal(avaliacao.score)}</strong>
                    </div>

                    <div>
                        <span>Resultado</span>
                        <strong>${formatarResultado(avaliacao.resultado)}</strong>
                    </div>
                </div>

                <strong>Sintomas marcados</strong>

                <div class="sintomas-lista">
                    ${sintomasHTML}
                </div>

                ${
                    avaliacao.detalhes
                        ? `<div class="detalhes-avaliacao">${escaparHTML(avaliacao.detalhes)}</div>`
                        : ""
                }
            </div>
        `;
    });
}

function imprimirRelatorio() {
    if (!relatorioAtual) {
        mostrarMensagem("Busque um paciente antes de imprimir.", "erro");
        return;
    }

    window.print();
}

async function baixarPDF() {
    if (!relatorioAtual) {
        mostrarMensagem("Busque um paciente antes de baixar o PDF.", "erro");
        return;
    }

    const original = document.getElementById("relatorioPDF");

    if (!original) {
        mostrarMensagem("Relatório não encontrado.", "erro");
        return;
    }

    if (typeof html2canvas === "undefined" || typeof window.jspdf === "undefined") {
        mostrarMensagem("As bibliotecas de PDF não carregaram corretamente.", "erro");
        return;
    }

    const clone = original.cloneNode(true);

    const areaTemporaria = document.createElement("div");
    areaTemporaria.classList.add("pdf-render");
    areaTemporaria.appendChild(clone);

    document.body.appendChild(areaTemporaria);

    await new Promise(resolve => setTimeout(resolve, 300));

    try {
        const canvas = await html2canvas(clone, {
            scale: 2,
            useCORS: true,
            backgroundColor: "#ffffff",
            logging: true
        });

        const imgData = canvas.toDataURL("image/png");

        const { jsPDF } = window.jspdf;

        const pdf = new jsPDF("p", "mm", "a4");

        const pageWidth = 210;
        const pageHeight = 297;

        const imgWidth = pageWidth;
        const imgHeight = (canvas.height * imgWidth) / canvas.width;

        let heightLeft = imgHeight;
        let position = 0;

        pdf.addImage(imgData, "PNG", 0, position, imgWidth, imgHeight);

        heightLeft -= pageHeight;

        while (heightLeft > 0) {
            position = heightLeft - imgHeight;
            pdf.addPage();
            pdf.addImage(imgData, "PNG", 0, position, imgWidth, imgHeight);
            heightLeft -= pageHeight;
        }

        const nome = relatorioAtual.dadosPaciente?.nome || "paciente";
        const nomeArquivo = `relatorio-${nome.replaceAll(" ", "-").toLowerCase()}.pdf`;

        pdf.save(nomeArquivo);

    } catch (error) {
        console.error("Erro ao gerar PDF:", error);
        mostrarMensagem("Erro ao gerar o PDF. Veja o console.", "erro");
    } finally {
        document.body.removeChild(areaTemporaria);
    }
}
function mostrarMensagem(texto, tipo) {
    const mensagem = document.getElementById("mensagem");
    mensagem.textContent = texto;
    mensagem.className = `mensagem show ${tipo}`;
}

function esconderMensagem() {
    const mensagem = document.getElementById("mensagem");
    mensagem.textContent = "";
    mensagem.className = "mensagem";
}

function formatarNumero(valor) {
    return Number(valor || 0).toLocaleString("pt-BR");
}

function formatarDecimal(valor) {
    return Number(valor || 0).toLocaleString("pt-BR", {
        minimumFractionDigits: 1,
        maximumFractionDigits: 1
    });
}

function formatarPorcentagem(valor) {
    return `${formatarDecimal(valor)}%`;
}

function formatarGenero(genero) {
    if (!genero) {
        return "-";
    }

    if (genero.toUpperCase() === "M") {
        return "Masculino";
    }

    if (genero.toUpperCase() === "F") {
        return "Feminino";
    }

    return genero;
}

function mascaraCPF(valor) {
    valor = valor.replace(/\D/g, "");
    valor = valor.slice(0, 11);

    valor = valor.replace(/(\d{3})(\d)/, "$1.$2");
    valor = valor.replace(/(\d{3})(\d)/, "$1.$2");
    valor = valor.replace(/(\d{3})(\d{1,2})$/, "$1-$2");

    return valor;
}

function formatarData(data) {
    if (!data) {
        return "-";
    }

    const partes = data.split("-");

    if (partes.length !== 3) {
        return data;
    }

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

function formatarTelefone(telefone) {
    if (!telefone) {
        return "-";
    }

    const numeros = String(telefone).replace(/\D/g, "");

    if (numeros.length === 11) {
        return `(${numeros.slice(0, 2)}) ${numeros.slice(2, 7)}-${numeros.slice(7)}`;
    }

    if (numeros.length === 10) {
        return `(${numeros.slice(0, 2)}) ${numeros.slice(2, 6)}-${numeros.slice(6)}`;
    }

    return telefone;
}

function formatarResposta(valor) {
    if (!valor) {
        return "-";
    }

    const respostas = {
        SIM: "Sim",
        NAO: "Não",
        NAO_SEI: "Não sei",
        TESTE_INDICADO: "Teste indicado",
        INCONCLUSIVO: "Inconclusivo",
        PROFISSIONAL: "Profissional",
        RESPONSAVEL: "Responsável",
        MUTACAO_COMPLETA: "Mutação completa",
        PRE_MUTACAO: "Pré-mutação",
        ZONA_GRAY: "Zona gray/intermediária",
        MOSAICISMO: "Mosaicismo",
        NEGATIVO: "Negativo",
    };

    return respostas[valor] || String(valor).replaceAll("_", " ");
}

function formatarResultado(resultado) {
    if (!resultado) {
        return "-";
    }

    if (resultado === "TESTE_INDICADO") {
        return "Teste indicado";
    }

    if (resultado === "INCONCLUSIVO") {
        return "Inconclusivo";
    }

    return formatarResposta(resultado);
}

function formatarOrigem(origem) {
    if (!origem) {
        return "-";
    }

    if (origem === "PROFISSIONAL") {
        return "Profissional";
    }

    if (origem === "RESPONSAVEL") {
        return "Responsável";
    }

    return formatarResposta(origem);
}

function escaparHTML(texto) {
    return String(texto || "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function setTexto(id, valor) {
    const elemento = document.getElementById(id);

    if (!elemento) {
        console.warn(`Elemento com id "${id}" não encontrado no HTML.`);
        return;
    }

    elemento.textContent = valor ?? "-";
}