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


// abrir e fechar o modal (tela do formulario)
const abrirModal = document.getElementById("btn");
const fecharModal = document.getElementById("fecharModal");
const overlay = document.getElementById("overlay");

abrirModal.addEventListener("click", () => {
    overlay.style.display = "flex";
});

fecharModal.addEventListener("click", () => {
    overlay.style.display = "none";
});

// Fecha ao clicar fora do formulário
overlay.addEventListener("click", (e) => {
    if(e.target === overlay){
        overlay.style.display = "none";
    }
});
//adicionar paciente
const formPaciente = document.getElementById("formPaciente");

formPaciente.addEventListener("submit", async (e) => {

    e.preventDefault();

    const nome = document.getElementById("nome").value;
    const idade = document.getElementById("idade").value;
    const cpf = document.getElementById("cpf").value;
    const guardiao = document.getElementById("guardiao").value;

    const generoSelecionado = document.querySelector(
        'input[name="genero"]:checked'
    );

   

    const genero = generoSelecionado
        ? generoSelecionado.value
        : null;

    const paciente = {
        nome: nome,
        CPF: cpf,
        genero: genero,
        idade: Number(idade),
        guardiao: guardiao
    };

    console.log("Paciente enviado:", paciente);

    try {

            
            const response = await fetch(
            "http://localhost:8080/api/pacientes",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },

                body: JSON.stringify(paciente)
            }
        );

        if (!response.ok) {

          const texto = await response.text();

          console.log("Status:", response.status);
          console.log("Resposta:", texto);

          alert(`Erro ${response.status}`);

          return;
        }
        const novoPaciente = await response.json();

        console.log("Paciente cadastrado:", novoPaciente);

        overlay.style.display = "none";

        formPaciente.reset();

        carregarPacientes();

    } catch (erro) {

        console.error(erro);

        alert("Erro ao cadastrar paciente");
    }
});


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