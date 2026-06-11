const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html"
}


function usuarioEhAdmin() {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.role === "ADMIN";
}


//Nome usuario
const payload = JSON.parse(atob(token.split('.')[1]));

const nome = payload.sub;

const nomeUser = document.getElementById("info-user")

nomeUser.innerText = nome

//logout
const btnLogout = document.getElementById("logout")

btnLogout.addEventListener("click", logout)

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("cpfPacienteAvaliacao");
    localStorage.removeItem("cpfPacienteEditar");

    window.location.href = "login.html";
}

function calcularIdade(dataNascimento) {
  const hoje = new Date();
  const nascimento = new Date(dataNascimento);

  let idade = hoje.getFullYear() - nascimento.getFullYear();
  const m = hoje.getMonth() - nascimento.getMonth();

  if (m < 0 || (m === 0 && hoje.getDate() < nascimento.getDate())) {
    idade--;
  }

  return idade;
}



const btnBuscar = document.getElementById("btnBuscar");
const cpfInput = document.getElementById("cpfPaciente");
const buscaContainer = document.querySelector(".busca-container");

btnBuscar.addEventListener("click", buscarPaciente);

async function buscarPaciente() {

    const cpf = cpfInput.value.trim();

    if (!cpf) {
        alert("Digite um CPF");
        return;
    }

    try {

        const response = await fetch(
            `http://localhost:8080/api/pacientes/${cpf}`,
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        );
        

        if (!response.ok) {
            throw new Error("Paciente não encontrado");
        }

        const paciente = await response.json();

        mostrarPaciente(paciente);

        buscaContainer.classList.add("buscou");
        pacienteAtual = paciente;
        cpfAntigoPaciente = cpf;

    } catch (erro) {

        resultado.innerHTML = `
            <p>Paciente não encontrado.</p>
        `;

        console.error(erro);
    }
}

function mostrarPaciente(paciente) {
    resultado.classList.remove("mostrar");

    document.getElementById("resultado").style.display = "block";

    document.getElementById("nomePaciente").innerText =
        paciente.nome;

    document.getElementById("idadePaciente").innerText =
        calcularIdade(paciente.dataNascimento);

    document.getElementById("generoPaciente").innerText =
        paciente.genero;

    document.getElementById("maePaciente").innerText =
        paciente.nomeMae;

    document.getElementById("paiPaciente").innerText =
        paciente.nomePai;

    resultado.style.display = "flex";

    setTimeout(() => {
        resultado.classList.add("mostrar");
    }, 10);
  }

const btnEditar = document.getElementById("btnEditar");

btnEditar.addEventListener("click", function () {
    if (!pacienteAtual) {
        alert("Busque um paciente primeiro.");
        return;
    }

    abrirModalEditarPaciente(pacienteAtual);
});

const btnAvaliacao = document.getElementById("btnAvaliacao")
btnAvaliacao.addEventListener("click" , iniciarAvaliacao)

function iniciarAvaliacao() {
    if (!pacienteAtual) {
        alert("Busque um paciente primeiro.");
        return;
    }

    localStorage.setItem("cpfPacienteAvaliacao", cpfAntigoPaciente);

    window.location.href = "formavaliacao.html";
}
let pacienteAtual = null;
let cpfAntigoPaciente = null;

// model 
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
    cpfAntigoPaciente = paciente.CPF_paciente || paciente.cpfPaciente || paciente.cpf || cpfAntigoPaciente;

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

// form da edição 
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
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(pacienteEditado)
        });

        if (!response.ok) {
            const erro = await response.json();
            console.log("Erro ao editar:", erro);
            alert("Erro ao editar paciente.");
            return;
        }

        const pacienteAtualizado = await response.json();

        alert("Paciente atualizado com sucesso!");
        fecharModal();

        pacienteAtual = {
            ...pacienteAtual,
            ...pacienteAtualizado
        };

    } catch (error) {
        console.log("Erro no fetch:", error);
        alert("Erro ao conectar com o servidor.");
    }
});