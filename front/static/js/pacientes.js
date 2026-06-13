const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

const API_PACIENTES = "http://localhost:8080/api/pacientes";

// Rotas baseadas no PatientController novo
const ROTAS_PACIENTE = {
    listar: API_PACIENTES,

    buscarPorId(id) {
        return `${API_PACIENTES}/id/${encodeURIComponent(id)}`;
    },

    buscarPorCpf(cpf) {
        if (usuarioEhAdmin()) {
            return `${API_PACIENTES}/admin/${encodeURIComponent(cpf)}`;
        }

        return `${API_PACIENTES}/cpf/${encodeURIComponent(cpf)}`;
    },

    editarPorCpf(cpf) {
        if (usuarioEhAdmin()) {
            return `${API_PACIENTES}/admin/${encodeURIComponent(cpf)}`;
        }

        return `${API_PACIENTES}/${encodeURIComponent(cpf)}`;
    },

    excluirPorCpf(cpf) {
            return `${API_PACIENTES}/cpf/${encodeURIComponent(cpf)}`;
    }
};

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
    return payload.role === "ADMIN" || payload.role === "ROLE_ADMIN";
}

// Nome usuário
const nomeUser = document.getElementById("info-user");

if (nomeUser) {
    nomeUser.innerText = payload.sub || "Usuário";
}

// Logout
const btnLogout = document.getElementById("logout");

if (btnLogout) {
    btnLogout.addEventListener("click", logout);
}

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

if (btnBuscar) {
    btnBuscar.addEventListener("click", buscarPacientePorCpf);
}

if (cpfInput) {
    cpfInput.addEventListener("keydown", function (event) {
        if (event.key === "Enter") {
            buscarPacientePorCpf();
        }
    });

    cpfInput.addEventListener("input", function () {
        if (cpfInput.value.trim() === "") {
            voltarParaLista();
        }
    });
}

if (btnLimparBusca) {
    btnLimparBusca.addEventListener("click", function () {
        cpfInput.value = "";
        voltarParaLista();
    });
}

// Carrega lista assim que abre a página
carregarListaPacientes();

async function carregarListaPacientes() {
    try {
        const response = await fetch(ROTAS_PACIENTE.listar, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            console.log("Status ao carregar lista:", response.status);
            throw new Error("Erro ao carregar lista de pacientes");
        }

        const dados = await response.json();

        const pacientes = Array.isArray(dados)
            ? dados
            : dados.content || [];

        renderizarListaPacientes(pacientes);

    } catch (error) {
        console.log("Erro ao carregar pacientes:", error);

        if (totalPacientes) {
            totalPacientes.innerText = "Não foi possível carregar os pacientes.";
        }

        if (listaPacientes) {
            listaPacientes.innerHTML = `
                <div class="lista-vazia">
                    Erro ao buscar a lista de pacientes.
                </div>
            `;
        }
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
        const idDoPaciente = pegarIdPaciente(paciente);
        const ativo = pacienteEstaAtivo(paciente);

        const item = document.createElement("div");
        item.className = idDoPaciente
            ? "paciente-item clicavel"
            : "paciente-item";

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

        // Agora o clique na lista busca por ID e abre só o modal básico
        if (idDoPaciente) {
            item.addEventListener("click", function () {
                buscarPacientePorId(idDoPaciente);
            });
        }

        listaPacientes.appendChild(item);
    });
}

// Clique na lista: busca por ID e abre modal simples
async function buscarPacientePorId(id) {
    try {
        const response = await fetch(ROTAS_PACIENTE.buscarPorId(id), {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            console.log("Status da busca por ID:", response.status);
            throw new Error("Paciente não encontrado pelo ID");
        }

        const paciente = await response.json();

        esconderMensagemBusca();
        abrirModalInfoPaciente(paciente);

    } catch (error) {
        console.log("Erro ao buscar paciente por ID:", error);
        mostrarMensagemBusca("Não foi possível carregar as informações desse paciente.");
    }
}

// Busca principal: CPF, abre card completo com editar, avaliação e exclusão
async function buscarPacientePorCpf() {
    const cpf = cpfInput.value.trim();

    if (!cpf) {
        alert("Digite um CPF");
        return;
    }

    try {
        const response = await fetch(ROTAS_PACIENTE.buscarPorCpf(cpf), {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        if (!response.ok) {
            console.log("Status da busca por CPF:", response.status);
            throw new Error("Paciente não encontrado");
        }

        const paciente = await response.json();

        pacienteAtual = paciente;
        cpfAntigoPaciente = pegarCpfPaciente(paciente) || cpf;

        mostrarPaciente(paciente);
        esconderLista();
        esconderMensagemBusca();

        if (buscaContainer) {
            buscaContainer.classList.add("buscou");
        }

        if (btnLimparBusca) {
            btnLimparBusca.style.display = "inline-block";
        }

    } catch (erro) {
        console.log("Erro ao buscar paciente por CPF:", erro);

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
        paciente.idade || "Não informado";

    document.getElementById("generoPaciente").innerText =
        formatarGenero(paciente.genero);

    document.getElementById("maePaciente").innerText =
        paciente.nomeMae || "Não informado";

    document.getElementById("paiPaciente").innerText =
        paciente.nomePai || "Não informado";

    resultado.style.display = "flex";

    setTimeout(function () {
        resultado.classList.add("mostrar");
    }, 10);
}

function esconderPaciente() {
    if (!resultado) return;

    resultado.classList.remove("mostrar");
    resultado.style.display = "none";
}

function esconderLista() {
    if (listaPacientesContainer) {
        listaPacientesContainer.classList.add("escondido");
    }
}

function mostrarLista() {
    if (listaPacientesContainer) {
        listaPacientesContainer.classList.remove("escondido");
    }
}

function mostrarMensagemBusca(texto) {
    if (!mensagemBusca) return;

    mensagemBusca.innerText = texto;
    mensagemBusca.style.display = "block";
}

function esconderMensagemBusca() {
    if (!mensagemBusca) return;

    mensagemBusca.innerText = "";
    mensagemBusca.style.display = "none";
}

function voltarParaLista() {
    pacienteAtual = null;
    cpfAntigoPaciente = null;

    esconderPaciente();
    esconderMensagemBusca();
    mostrarLista();

    if (buscaContainer) {
        buscaContainer.classList.remove("buscou");
    }

    if (btnLimparBusca) {
        btnLimparBusca.style.display = "none";
    }
}

function pegarIdPaciente(paciente) {
    return (
        paciente.id ||
        paciente.idPaciente ||
        paciente.pacienteId ||
        paciente.ID ||
        null
    );
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

function pacienteEstaAtivo(paciente) {
    return (
        paciente.ativo !== false &&
        paciente.ativo !== 0 &&
        paciente.ativo !== "false" &&
        paciente.ativo !== "0"
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
function formatarDataNascimento(dataNascimento) {
    if (!dataNascimento) return "Não informado";

    const partes = dataNascimento.split("-");

    if (partes.length !== 3) {
        return dataNascimento;
    }

    const ano = partes[0];
    const mes = partes[1];
    const dia = partes[2];

    return `${dia}/${mes}/${ano}`;
}


function escaparHTML(texto) {
    return String(texto)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

// Botões do card completo
const btnEditar = document.getElementById("btnEditar");
const btnAvaliacao = document.getElementById("btnAvaliacao");
const btnExcluir = document.getElementById("btnExcluir");

if (btnEditar) {
    btnEditar.addEventListener("click", function () {

        abrirModalEditarPaciente(pacienteAtual);
    });
}

if (btnAvaliacao) {
    btnAvaliacao.addEventListener("click", iniciarAvaliacao);
}

if (btnExcluir) {
    btnExcluir.addEventListener("click", abrirModalExcluirPaciente);
}

function iniciarAvaliacao() {
    if (!pacienteAtual) {
        alert("Busque um paciente por CPF primeiro.");
        return;
    }

    const cpfPaciente = cpfAntigoPaciente || pegarCpfPaciente(pacienteAtual);


    localStorage.setItem("cpfPacienteAvaliacao", cpfPaciente);

    window.location.href = "formavaliacao.html";
}

// Modal de informações básicas
const modalInfoPaciente = document.getElementById("modalInfoPaciente");
const fecharModalInfoPaciente = document.getElementById("fecharModalInfoPaciente");
const fecharInfoPaciente = document.getElementById("fecharInfoPaciente");
const infoDataNascimentoPaciente = document.getElementById("infoDataNascimentoPaciente");
const infoNomePaciente = document.getElementById("infoNomePaciente");
const infoIdPaciente = document.getElementById("infoIdPaciente");
const infoIdadePaciente = document.getElementById("infoIdadePaciente");
const infoGeneroPaciente = document.getElementById("infoGeneroPaciente");
const infoMaePaciente = document.getElementById("infoMaePaciente");
const infoPaiPaciente = document.getElementById("infoPaiPaciente");
const infoStatusPaciente = document.getElementById("infoStatusPaciente");

function abrirModalInfoPaciente(paciente) {
    

    const ativo = pacienteEstaAtivo(paciente);

    if (infoNomePaciente) {
        infoNomePaciente.innerText = paciente.nome || "Nome não informado";
    }

    if (infoIdPaciente) {
        infoIdPaciente.innerText = pegarIdPaciente(paciente) || "Não informado";
    }
    if (infoDataNascimentoPaciente) {
        infoDataNascimentoPaciente.innerText = formatarDataNascimento(paciente.dataNascimento);
    }

    if (infoIdadePaciente) {
        infoIdadePaciente.innerText =
            paciente.idade || paciente.idade || "Não informado";
    }

    if (infoGeneroPaciente) {
        infoGeneroPaciente.innerText = formatarGenero(paciente.genero);
    }

    if (infoMaePaciente) {
        infoMaePaciente.innerText = paciente.nomeMae || "Não informado";
    }

    if (infoPaiPaciente) {
        infoPaiPaciente.innerText = paciente.nomePai || "Não informado";
    }

    if (infoStatusPaciente) {
        infoStatusPaciente.innerText = ativo ? "Ativo" : "Inativo";

        infoStatusPaciente.classList.remove("ativo", "inativo");
        infoStatusPaciente.classList.add(ativo ? "ativo" : "inativo");
    }

    modalInfoPaciente.classList.add("ativo");
}

function fecharModalInfo() {
    if (modalInfoPaciente) {
        modalInfoPaciente.classList.remove("ativo");
    }
}

if (fecharModalInfoPaciente) {
    fecharModalInfoPaciente.addEventListener("click", fecharModalInfo);
}

if (fecharInfoPaciente) {
    fecharInfoPaciente.addEventListener("click", fecharModalInfo);
}

if (modalInfoPaciente) {
    modalInfoPaciente.addEventListener("click", function (event) {
        if (event.target === modalInfoPaciente) {
            fecharModalInfo();
        }
    });
}

// Modal de edição
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

    cpfAntigoPaciente =
        pegarCpfPaciente(paciente) ||
        cpfAntigoPaciente;


    editNomePaciente.value = paciente.nome || "";
    editDataNascimento.value = paciente.dataNascimento || "";
    editGeneroPaciente.value = paciente.genero || "";
    editNomeMae.value = paciente.nomeMae || "";
    editNomePai.value = paciente.nomePai || "";

    // Campo ativo só aparece para admin na edição
    if (campoAtivoContainer) {
        campoAtivoContainer.style.display = usuarioEhAdmin() ? "block" : "none";
    }

    if (usuarioEhAdmin() && editAtivo) {
        editAtivo.value = String(pacienteEstaAtivo(paciente));
    }

    modalEditarPaciente.classList.add("ativo");
}

function fecharModalEditarPaciente() {
    if (modalEditarPaciente) {
        modalEditarPaciente.classList.remove("ativo");
    }
}

if (fecharModalEditar) {
    fecharModalEditar.addEventListener("click", fecharModalEditarPaciente);
}

if (cancelarEdicaoPaciente) {
    cancelarEdicaoPaciente.addEventListener("click", fecharModalEditarPaciente);
}

if (modalEditarPaciente) {
    modalEditarPaciente.addEventListener("click", function (event) {
        if (event.target === modalEditarPaciente) {
            fecharModalEditarPaciente();
        }
    });
}

// Form da edição
if (formEditarPaciente) {
    formEditarPaciente.addEventListener("submit", async function (event) {
        event.preventDefault();

        if (!cpfAntigoPaciente) {
            alert("CPF do paciente não encontrado.");
            return;
        }

        const pacienteEditado = {
            nome: editNomePaciente.value,
            genero: editGeneroPaciente.value,
            dataNascimento: editDataNascimento.value,
            nomeMae: editNomeMae.value,
            nomePai: editNomePai.value
        };

        // O DTO de admin aceita ativo, o DTO de usuário provavelmente não aceita.
        if (usuarioEhAdmin()) {
            pacienteEditado.ativo = editAtivo.value === "true";
        }

        try {
            const response = await fetch(ROTAS_PACIENTE.editarPorCpf(cpfAntigoPaciente), {
                method: "PATCH",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify(pacienteEditado)
            });

            if (!response.ok) {
                const erroTexto = await response.text();
                console.log("Erro ao editar:", erroTexto);
                alert("Erro ao editar paciente.");
                return;
            }

            let pacienteAtualizado = {};

            if (response.status !== 204) {
                pacienteAtualizado = await response.json();
            }

           
            fecharModalEditarPaciente();

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
}

// Modal de excluir paciente
const modalExcluirPaciente = document.getElementById("modalExcluirPaciente");
const fecharModalExcluir = document.getElementById("fecharModalExcluir");
const cancelarExclusao = document.getElementById("cancelarExclusao");
const confirmarExclusao = document.getElementById("confirmarExclusao");

const nomePacienteExcluir = document.getElementById("nomePacienteExcluir");

function abrirModalExcluirPaciente() {
    if (!pacienteAtual) {
        alert("Busque um paciente por CPF primeiro.");
        return;
    }

    if (nomePacienteExcluir) {
        nomePacienteExcluir.innerText = pacienteAtual.nome || "Paciente sem nome";
    }

    modalExcluirPaciente.classList.add("ativo");
}

function fecharModalExcluirPaciente() {
    if (modalExcluirPaciente) {
        modalExcluirPaciente.classList.remove("ativo");
    }
}

if (fecharModalExcluir) {
    fecharModalExcluir.addEventListener("click", fecharModalExcluirPaciente);
}

if (cancelarExclusao) {
    cancelarExclusao.addEventListener("click", fecharModalExcluirPaciente);
}

if (modalExcluirPaciente) {
    modalExcluirPaciente.addEventListener("click", function (event) {
        if (event.target === modalExcluirPaciente) {
            fecharModalExcluirPaciente();
        }
    });
}

if (confirmarExclusao) {
    confirmarExclusao.addEventListener("click", excluirPaciente);
}

async function excluirPaciente() {
    if (!pacienteAtual) {
        alert("Nenhum paciente selecionado.");
        return;
    }

    const cpfPaciente = cpfAntigoPaciente || pegarCpfPaciente(pacienteAtual);

    if (!cpfPaciente) {
        alert("CPF do paciente não encontrado.");
        return;
    }

    try {
        if (confirmarExclusao) {
            confirmarExclusao.disabled = true;
            confirmarExclusao.innerText = "Excluindo...";
        }

        const response = await fetch(ROTAS_PACIENTE.excluirPorCpf(cpfPaciente), {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${token}`
            }
        });

        console.log("Status da exclusão:", response.status);

        if (!response.ok) {
            const erroTexto = await response.text();
            console.log("Erro ao excluir paciente:", erroTexto);

            alert("Erro ao excluir paciente.");
            return;
        }


        fecharModalExcluirPaciente();

        pacienteAtual = null;
        cpfAntigoPaciente = null;
        cpfInput.value = "";

        voltarParaLista();
        carregarListaPacientes();

    } catch (error) {
        console.log("Erro no fetch:", error);
        alert("Erro ao conectar com o servidor.");
    } finally {
        if (confirmarExclusao) {
            confirmarExclusao.disabled = false;
            confirmarExclusao.innerText = "Sim, excluir paciente";
        }
    }
}