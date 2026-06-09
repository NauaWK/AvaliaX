const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html"
}

//modo edição
const params = new URLSearchParams(window.location.search);
const modo = params.get("modo");

const cpfEditar = localStorage.getItem("cpfPacienteEditar");

if (modo === "editar" && cpfEditar) {
    carregarPacienteParaEditar(cpfEditar);
}

async function carregarPacienteParaEditar(cpf) {
    const response = await fetch(`http://localhost:8080/api/pacientes/${cpf}`, {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) {
        alert("Erro ao carregar paciente.");
        return;
    }

    const paciente = await response.json();

    document.getElementById("nomePaciente").value = paciente.nome;
    document.getElementById("cpfPaciente").value = cpf;
    document.getElementById("dataNascimento").value = paciente.dataNascimento;
    document.getElementById("nomeMae").value = paciente.nomeMae;
    document.getElementById("nomePai").value = paciente.nomePai || "";

    document.getElementById("sexo").value = paciente.genero;

    const textoSexo = paciente.genero === "M" ? "Masculino" : "Feminino";
    document.querySelector("#sexoSelect .selected-value").textContent = textoSexo;

    document.querySelector(".btn-salvar").textContent = "Salvar alterações";
}


//dropdow sexobiologico
const customSelect = document.querySelector("#sexoSelect");
const button = customSelect.querySelector(".select-btn");
const options = customSelect.querySelector(".options");
const selectedValue = customSelect.querySelector(".selected-value");
const hiddenInput = customSelect.querySelector("input");

button.addEventListener("click", () => {
    options.classList.toggle("show");
    button.classList.toggle("active");
});

options.querySelectorAll("li").forEach(option => {

    option.addEventListener("click", () => {

        selectedValue.textContent = option.textContent;
        hiddenInput.value = option.dataset.value;

        options.classList.remove("show");
        button.classList.remove("active");
    });

});

document.addEventListener("click", (event) => {

    if (!customSelect.contains(event.target)) {

        options.classList.remove("show");
        button.classList.remove("active");

    }

});

//dropdow estado
const estadoSelect = document.getElementById("estadoSelect");

const btn = estadoSelect.querySelector(".select-btn");

const dropdown = estadoSelect.querySelector(".dropdown");

const searchInput = estadoSelect.querySelector(".search-input");

const estadooptions = estadoSelect.querySelectorAll("li");

const estadoSelectedValue = estadoSelect.querySelector(".selected-value");

const estadoHiddenInput = estadoSelect.querySelector("input[type='hidden']");

btn.addEventListener("click", () => {
    dropdown.classList.toggle("show");
    searchInput.focus();
});

estadooptions.forEach(option => {
    option.addEventListener("click", () => {
        estadoSelectedValue.textContent = option.textContent
        estadoHiddenInput.value = option.dataset.value;
        dropdown.classList.remove("show");

    });

});

console.log(estadooptions);

searchInput.addEventListener("input", () => {

    const value = searchInput.value.toLowerCase();

    estadooptions.forEach(option => {

        const text = option.textContent.toLowerCase();

        option.style.display =
            text.includes(value)
                ? "block"
                : "none";

    });

});

document.addEventListener("click", e => {

    if (!estadoSelect.contains(e.target)) {

        dropdown.classList.remove("show");

    }

});

const formulario = document.querySelector(".formulario");

formulario.addEventListener("submit", async (e) => {
    e.preventDefault();

    const paciente = {
        nome: document.getElementById("nomePaciente").value,
        CPF_paciente: document.getElementById("cpfPaciente").value,
        genero: document.getElementById("sexo").value,
        dataNascimento: document.getElementById("dataNascimento").value,
        nomeMae: document.getElementById("nomeMae").value,
        nomePai: document.getElementById("nomePai").value,

        responsavel: {
            nome: document.getElementById("responsavel").value,
            CPF_responsavel: document.getElementById("cpf").value,
            grauParentesco: document.getElementById("parentesco").value,
            cidade: document.getElementById("cidade").value,
            estado: document.getElementById("estado").value,
            pais: document.getElementById("pais").value,
            whatsapp: document.getElementById("whatsapp").value,
            telefone1: document.getElementById("telefone1").value,
            telefone2: document.getElementById("telefone2").value,
            email: document.getElementById("email").value
        }
    };

    console.log(paciente);
try {

    const url = modo === "editar"
        ? `http://localhost:8080/api/pacientes/${cpfEditar}`
        : "http://localhost:8080/api/pacientes";

    const metodo = modo === "editar"
        ? "PATCH"
        : "POST";

    const response = await fetch(url, {
        method: metodo,
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(paciente)
    });

    if (!response.ok) {

        const erros = await response.json();

        console.log("ERROS DO BACK:");
        console.log(erros);

        mostrarErros(erros);

        return;
    }

    const data = await response.json();

    console.log(data);
    localStorage.removeItem("cpfPacienteEditar");
    window.location.href = "pacientes.html";

} catch (erro) {

    console.error("ERRO NO FETCH:");
    console.error(erro);

}           
});


function limparErros() {

    document.querySelectorAll(".erro").forEach(el => {
        el.textContent = "";
    });

    document.querySelectorAll(".input-erro").forEach(el => {
        el.classList.remove("input-erro");
    });

}

function mostrarErros(erros) {

    limparErros();

    erros.field_errors.forEach(erro => {

        console.log(erro);

        const campoErro = document.getElementById(
            `erro-${erro.field}`
        );

        if (campoErro) {
            campoErro.textContent =
                erro.defaultErrorMessage;
        }

    });

}