const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

function pegarPayloadToken() {
    try {
        return JSON.parse(atob(token.split(".")[1]));
    } catch (error) {
        console.log("Erro ao ler token:", error);
        return {};
    }
}

const payload = pegarPayloadToken();

function usuarioEhAdmin() {
    return payload.role === "ADMIN";
}

// Nome usuário
const nomeUser = document.getElementById("info-user");
nomeUser.innerText = payload.sub || "Usuário";

// Logout
const btnLogout = document.getElementById("logout");

btnLogout.addEventListener("click", logout);

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("cpfPacienteAvaliacao");
    localStorage.removeItem("cpfPacienteEditar");

    window.location.href = "login.html";
}

// Elementos da busca
const btnBuscar = document.getElementById("btnBuscar");
const btnLimparBusca = document.getElementById("btnLimparBusca");
const cpfInput = document.getElementById("cpfPaciente");
const buscaContainer = document.querySelector(".busca-container");

const resultado = document.getElementById("resultado");
const mensagemBusca = document.getElementById("mensagemBusca");

const listaPacientesContainer = document.getElementById("listaPacientesContainer");
const listaPacientes = document.getElementById("listaPacientes");
const totalPacientes = document.getElementById("totalPacientes");

let pacienteAtual = null;
let cpfAntigoPaciente = null;

btnBuscar.addEventListener("click", buscarPaciente);

cpfInput.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
        buscarPaciente();
    }
});

cpfInput.addEventListener("input", function () {
    if (cpfInput.value.trim() === "") {
        voltarParaLista();
    }
});

btnLimparBusca.addEventListener("click", function () {
    cpfInput.value = "";
    voltarParaLista();
});

// Carrega lista assim que abre a página
carregarListaPacientes();

async function carregarListaPacientes() {
    try {
        const response = await fetch("http://localhost:8080/api/pacientes", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("Erro ao carregar lista de pacientes");
        }

        const dados = await response.json();

        const pacientes = Array.isArray(dados)
            ? dados
            : dados.content || [];

        renderizarListaPacientes(pacientes);

    } catch (error) {
        console.log("Erro ao carregar pacientes:", error);

        totalPacientes.innerText = "Não foi possível carregar os pacientes.";

        listaPacientes.innerHTML = `
            <div class="lista-vazia">
                Erro ao buscar a lista de pacientes.
            </div>
        `;
    }
}

function renderizarListaPacientes(pacientes) {
    listaPacientes.innerHTML = "";

    if (!pacientes || pacientes.length === 0) {
        totalPacientes.innerText = "Nenhum paciente cadastrado.";

        listaPacientes.innerHTML = `
            <div class="lista-vazia">
                Nenhum paciente encontrado.
            </div>
        `;

        return;
    }

    totalPacientes.innerText = `${pacientes.length} paciente(s) cadastrado(s).`;

    pacientes.forEach(function (paciente) {
        const cpfDoPaciente = pegarCpfPaciente(paciente);

        const item = document.createElement("div");
        item.className = cpfDoPaciente
            ? "paciente-item clicavel"
            : "paciente-item";

        const ativo = paciente.ativo !== false;

        item.innerHTML = `
            <div class="paciente-avatar">
                ${pegarIniciais(paciente.nome)}
            </div>

            <div class="paciente-info">
                <h3>${escaparHTML(paciente.nome || "Nome não informado")}</h3>
                <p>
                    ${escaparHTML(formatarGenero(paciente.genero))}
                    ${paciente.nomeMae ? ` • Mãe: ${escaparHTML(paciente.nomeMae)}` : ""}
                </p>
            </div>

            <span class="paciente-status ${ativo ? "" : "inativo"}">
                ${ativo ? "Ativo" : "Inativo"}
            </span>
        `;

        if (cpfDoPaciente) {
            item.addEventListener("click", function () {
                cpfInput.value = cpfDoPaciente;
                buscarPaciente();
            });
        }

        listaPacientes.appendChild(item);
    });
}

async function buscarPaciente() {
    const cpf = cpfInput.value.trim();

    if (!cpf) {
        alert("Digite um CPF");
        return;
    }

    const endpoint = usuarioEhAdmin()
        ? `http://localhost:8080/api/pacientes/admin/${cpf}`
        : `http://localhost:8080/api/pacientes/${cpf}`;

    try {
        const response = await fetch(endpoint, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            console.log("Status da busca:", response.status);
            throw new Error("Paciente não encontrado");
        }

        const paciente = await response.json();

        pacienteAtual = paciente;
        cpfAntigoPaciente = cpf;

        mostrarPaciente(paciente);
        esconderLista();
        esconderMensagemBusca();

        buscaContainer.classList.add("buscou");
        btnLimparBusca.style.display = "inline-block";

    } catch (erro) {
        console.error(erro);

        esconderLista();
        esconderPaciente();

        mostrarMensagemBusca("Paciente não encontrado.");
        btnLimparBusca.style.display = "inline-block";
    }
}

function mostrarPaciente(paciente) {
    resultado.classList.remove("mostrar");

    document.getElementById("nomePaciente").innerText =
        paciente.nome || "Nome não informado";

    document.getElementById("idadePaciente").innerText =
        paciente.idade || calcularIdade(paciente.dataNascimento) || "Não informado";

    document.getElementById("generoPaciente").innerText =
        formatarGenero(paciente.genero);

    document.getElementById("maePaciente").innerText =
        paciente.nomeMae || "Não informado";

    document.getElementById("paiPaciente").innerText =
        paciente.nomePai || "Não informado";

    resultado.style.display = "flex";

    setTimeout(() => {
        resultado.classList.add("mostrar");
    }, 10);
}

function esconderPaciente() {
    resultado.classList.remove("mostrar");
    resultado.style.display = "none";
}

function esconderLista() {
    listaPacientesContainer.classList.add("escondido");
}

function mostrarLista() {
    listaPacientesContainer.classList.remove("escondido");
}

function mostrarMensagemBusca(texto) {
    mensagemBusca.innerText = texto;
    mensagemBusca.style.display = "block";
}

function esconderMensagemBusca() {
    mensagemBusca.innerText = "";
    mensagemBusca.style.display = "none";
}

function voltarParaLista() {
    pacienteAtual = null;
    cpfAntigoPaciente = null;

    esconderPaciente();
    esconderMensagemBusca();
    mostrarLista();

    btnLimparBusca.style.display = "none";
}

function pegarCpfPaciente(paciente) {
    return (
        paciente.cpf ||
        paciente.CPF ||
        paciente.cpfPaciente ||
        paciente.CPF_paciente ||
        paciente.cpf_paciente ||
        null
    );
}

function pegarIniciais(nome) {
    if (!nome) return "?";

    const partes = nome.trim().split(" ");

    if (partes.length === 1) {
        return partes[0].charAt(0).toUpperCase();
    }

    return (
        partes[0].charAt(0) + partes[partes.length - 1].charAt(0)
    ).toUpperCase();
}

function formatarGenero(genero) {
    if (genero === "M") return "Masculino";
    if (genero === "F") return "Feminino";
    return genero || "Não informado";
}

function calcularIdade(dataNascimento) {
    if (!dataNascimento) return null;

    const nascimento = new Date(dataNascimento);
    const hoje = new Date();

    let idade = hoje.getFullYear() - nascimento.getFullYear();

    const mesAtual = hoje.getMonth();
    const diaAtual = hoje.getDate();

    const mesNascimento = nascimento.getMonth();
    const diaNascimento = nascimento.getDate();

    if (
        mesAtual < mesNascimento ||
        (mesAtual === mesNascimento && diaAtual < diaNascimento)
    ) {
        idade--;
    }

    return idade;
}

function escaparHTML(texto) {
    return String(texto)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

// Botões do card
const btnEditar = document.getElementById("btnEditar");

btnEditar.addEventListener("click", function () {
    if (!pacienteAtual) {
        alert("Busque um paciente primeiro.");
        return;
    }

    abrirModalEditarPaciente(pacienteAtual);
});

const btnAvaliacao = document.getElementById("btnAvaliacao");

btnAvaliacao.addEventListener("click", iniciarAvaliacao);

function iniciarAvaliacao() {
    if (!pacienteAtual) {
        alert("Busque um paciente primeiro.");
        return;
    }

    localStorage.setItem("cpfPacienteAvaliacao", cpfAntigoPaciente);

    window.location.href = "formavaliacao.html";
}

// Modal
const modalEditarPaciente = document.getElementById("modalEditarPaciente");
const formEditarPaciente = document.getElementById("formEditarPaciente");

const fecharModalEditar = document.getElementById("fecharModalEditar");
const cancelarEdicaoPaciente = document.getElementById("cancelarEdicaoPaciente");

const editNomePaciente = document.getElementById("editNomePaciente");
const editDataNascimento = document.getElementById("editDataNascimento");
const editGeneroPaciente = document.getElementById("editGeneroPaciente");
const editNomeMae = document.getElementById("editNomeMae");
const editNomePai = document.getElementById("editNomePai");

const campoAtivoContainer = document.getElementById("campoAtivoContainer");
const editAtivo = document.getElementById("editAtivo");

function abrirModalEditarPaciente(paciente) {
    pacienteAtual = paciente;
    console.log("Paciente que chegou no modal:", paciente);
    cpfAntigoPaciente =
        pegarCpfPaciente(paciente) ||
        cpfAntigoPaciente;

    editNomePaciente.value = paciente.nome || "";
    editDataNascimento.value = paciente.dataNascimento || "";
    editGeneroPaciente.value = paciente.genero || "";
    editNomeMae.value = paciente.nomeMae || "";
    editNomePai.value = paciente.nomePai || "";

    campoAtivoContainer.style.display = usuarioEhAdmin() ? "block" : "none";

    if (usuarioEhAdmin()) {
        editAtivo.value = String(paciente.ativo);
    }

    modalEditarPaciente.classList.add("ativo");
}

function fecharModal() {
    modalEditarPaciente.classList.remove("ativo");
}

fecharModalEditar.addEventListener("click", fecharModal);
cancelarEdicaoPaciente.addEventListener("click", fecharModal);

modalEditarPaciente.addEventListener("click", function (event) {
    if (event.target === modalEditarPaciente) {
        fecharModal();
    }
});

// Form da edição
formEditarPaciente.addEventListener("submit", async function (event) {
    event.preventDefault();

    const pacienteEditado = {
        nome: editNomePaciente.value,
        genero: editGeneroPaciente.value,
        dataNascimento: editDataNascimento.value,
        nomeMae: editNomeMae.value,
        nomePai: editNomePai.value
    };

    if (usuarioEhAdmin()) {
        pacienteEditado.ativo = editAtivo.value === "true";
    }

    try {
        const endpoint = usuarioEhAdmin()
            ? `http://localhost:8080/api/pacientes/admin/${cpfAntigoPaciente}`
            : `http://localhost:8080/api/pacientes/${cpfAntigoPaciente}`;

        const response = await fetch(endpoint, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`
            },
            body: JSON.stringify(pacienteEditado)
        });

        if (!response.ok) {
            const erro = await response.json();
            console.log("Erro ao editar:", erro);
            alert("Erro ao editar paciente.");
            return;
        }

        let pacienteAtualizado = {};

        if (response.status !== 204) {
            pacienteAtualizado = await response.json();
        }

        alert("Paciente atualizado com sucesso!");
        fecharModal();

        pacienteAtual = {
            ...pacienteAtual,
            ...pacienteEditado,
            ...pacienteAtualizado
        };

        mostrarPaciente(pacienteAtual);
        carregarListaPacientes();

    } catch (error) {
        console.log("Erro no fetch:", error);
        alert("Erro ao conectar com o servidor.");
    }
});