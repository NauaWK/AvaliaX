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



//sistema de busca
const input = document.getElementById("buscarPaciente");
const resultado = document.getElementById("resultado");

let pacientes = [];

async function carregarPacientes() {
  try {

    const response = await fetch("http://localhost:8080/api/pacientes", {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });


    pacientes = await response.json();

    mostrarPacientes(pacientes);

  } catch (erro) {
    console.error("Erro ao buscar pacientes:", erro);
  }
}

function mostrarPacientes(lista) {

  resultado.innerHTML = "";

  if (lista.length === 0) {

    const aviso = document.createElement("p");
    aviso.textContent = "Nenhum paciente encontrado.";

    resultado.appendChild(aviso);

    return;
  }

  lista.forEach(paciente => {

    const card = document.createElement("div");
    card.classList.add("paciente-card");

    const nome = document.createElement("h2");
    nome.textContent = paciente.nome;

    const info = document.createElement("div");
    info.classList.add("paciente-info");

    const idade = document.createElement("p");
    idade.textContent = `Idade: ${paciente.idade}`;

    const genero = document.createElement("p");
    genero.textContent = `Gênero: ${paciente.genero}`;

    const guardiao = document.createElement("p");
    guardiao.textContent = `Guardião: ${paciente.guardiao}`;

    info.appendChild(idade);
    info.appendChild(genero);
    info.appendChild(guardiao);

    card.appendChild(nome);
    card.appendChild(info);

    resultado.appendChild(card);
  });
}

input.addEventListener("input", () => {

  const texto = input.value.toLowerCase();

  const filtrados = pacientes.filter(paciente =>
    paciente.nome.toLowerCase().includes(texto)
  );

  mostrarPacientes(filtrados);
});

carregarPacientes();