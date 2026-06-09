const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html"
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
    localStorage.removeItem("token")

    window.location.href = "login.html"
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

const btnEditar = document.getElementById("btnEditar")

btnEditar.addEventListener("click", editarPaciente)

function editarPaciente() {

    localStorage.setItem(
        "cpfPacienteEditar",
        cpfInput.value
    );

    window.location.href =
        "formpaciente.html?modo=editar";
}

function iniciarAvaliacao() {

    localStorage.setItem(
        "cpfPacienteAvaliacao",
        cpfInput.value
    );

    window.location.href =
        "avaliacao.html";
}