const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

const API_URL = "http://localhost:8080/api/avaliacoes";

const params = new URLSearchParams(window.location.search);
const modo = params.get("modo");
const idAvaliacao = params.get("id");

if (modo === "editar" && idAvaliacao) {
    document.getElementById("tituloFormulario").textContent = "Editar Avaliação";
    document.getElementById("subtituloFormulario").textContent = "Altere as respostas do questionário.";
    document.getElementById("btnSalvarAvaliacao").textContent = "Salvar Alterações";

    carregarAvaliacaoParaEditar(idAvaliacao);
} else {
    document.getElementById("tituloFormulario").textContent = "Nova Avaliação";
    document.getElementById("subtituloFormulario").textContent = "Preencha o questionário do paciente selecionado.";
    document.getElementById("btnSalvarAvaliacao").textContent = "Salvar Avaliação";
}

const form = document.getElementById("formAvaliacao");

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

montarSintomas();

if (modo === "editar" && idAvaliacao) {
    carregarAvaliacaoParaEditar(idAvaliacao);
}

async function carregarAvaliacaoParaEditar(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`, {
            headers: {
                Authorization: `Bearer ${localStorage.getItem("token")}`
            }
        });

        if (!response.ok) {
            alert("Erro ao carregar avaliação para edição.");
            return;
        }

        const avaliacao = await response.json();

        preencherFormulario(avaliacao);

    } catch (error) {
        console.error(error);
        alert("Erro ao buscar avaliação.");
    }
}

function preencherFormulario(avaliacao) {
    marcarRadio("testeDna", avaliacao.testeDna);
    marcarRadio("interesseExame", avaliacao.interesseExame);
    marcarRadio("diagnosticoAutismo", avaliacao.diagnosticoAutismo);
    marcarRadio("possuiIrmaos", avaliacao.possuiIrmaos);
    marcarRadio("antecedentesDeficiencia", avaliacao.antecedentesDeficiencia);
    marcarRadio("antecedentesMenopausa", avaliacao.antecedentesMenopausa);
    marcarRadio("antecedentesAtaxia", avaliacao.antecedentesAtaxia);

    document.getElementById("resultadoExame").value = avaliacao.resultadoExame || "";
    document.getElementById("detalhes").value = avaliacao.detalhes || "";

    const checkboxes = document.querySelectorAll("#sintomasContainer input[type='checkbox']");

    checkboxes.forEach(checkbox => {
        checkbox.checked = avaliacao.sintomas?.includes(checkbox.value);
    });
}

function marcarRadio(nome, valor) {
    const radio = document.querySelector(`input[name="${nome}"][value="${valor}"]`);

    if (radio) {
        radio.checked = true;
    }
}

function montarSintomas() {
    const container = document.getElementById("sintomasContainer");

    container.replaceChildren();

    todosSintomas.forEach(nome => {
        const label = document.createElement("label");
        label.classList.add("checkbox-option");

        const input = document.createElement("input");
        input.type = "checkbox";
        input.value = nome;

        label.appendChild(input);
        label.appendChild(document.createTextNode(" " + nome));

        container.appendChild(label);
    });
}

form.addEventListener("submit", async event => {
    event.preventDefault();

    const dados = {
    detalhes: document.getElementById("detalhes").value,
    testeDna: pegarRadio("testeDna"),
    interesseExame: pegarRadio("interesseExame"),
    resultadoExame: document.getElementById("resultadoExame").value || null,
    diagnosticoAutismo: pegarRadio("diagnosticoAutismo"),
    possuiIrmaos: pegarRadio("possuiIrmaos"),
    antecedentesDeficiencia: pegarRadio("antecedentesDeficiencia"),
    antecedentesMenopausa: pegarRadio("antecedentesMenopausa"),
    antecedentesAtaxia: pegarRadio("antecedentesAtaxia"),
    sintomas: pegarSintomas()
    };

    let url = API_URL;
    let metodo = "POST";

    if (modo === "editar" && idAvaliacao) {
        url = `${API_URL}/${idAvaliacao}`;
        metodo = "PATCH";
    } else {
        const cpfPaciente = localStorage.getItem("cpfPacienteAvaliacao");

    if (!cpfPaciente) {
        alert("Nenhum paciente selecionado para avaliação.");
        window.location.href = "pacientes.html";
        return;
    }

    dados.CPF_paciente = cpfPaciente;
}

    try {
        const response = await fetch(url, {
            method: metodo,
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify(dados)
        });

        if (!response.ok) {
            const erro = await tentarLerErro(response);
            alert(erro);
            return;
        }

        alert(modo === "editar" ? "Avaliação editada com sucesso!" : "Avaliação cadastrada com sucesso!");
        window.location.href = "avaliacoes.html";

    } catch (error) {
        console.error(error);
        alert("Erro ao salvar avaliação.");
    }
});

function pegarRadio(nome) {
    const selecionado = document.querySelector(`input[name="${nome}"]:checked`);
    return selecionado ? selecionado.value : null;
}

function pegarSintomas() {
    const checkboxes = document.querySelectorAll("#sintomasContainer input[type='checkbox']");

    return Array.from(checkboxes).map(checkbox => ({
        nome: checkbox.value,
        presente: checkbox.checked
    }));
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