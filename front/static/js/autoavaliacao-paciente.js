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
        estadoSelectedValue.textContent = option.textContent;
        estadoHiddenInput.value = option.dataset.value;
        dropdown.classList.remove("show");
    });
});

searchInput.addEventListener("input", () => {
    const value = searchInput.value.toLowerCase();
    estadooptions.forEach(option => {
        option.style.display = option.textContent.toLowerCase().includes(value) ? "block" : "none";
    });
});

document.addEventListener("click", e => {
    if (!estadoSelect.contains(e.target)) {
        dropdown.classList.remove("show");
    }
});

const formPaciente = document.getElementById("formPaciente");

formPaciente.addEventListener("submit", (e) => {
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

    if (!paciente.genero) {
        alert("Selecione o sexo biológico.");
        return;
    }

    if (!paciente.responsavel.estado) {
        alert("Selecione o estado.");
        return;
    }

    sessionStorage.setItem("dadosPaciente", JSON.stringify(paciente));
    window.location.href = "autoavaliacao-avaliacao.html";
});