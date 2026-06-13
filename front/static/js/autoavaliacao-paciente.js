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
        limparErro("erro-genero");
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
        limparErro("erro-responsavel.estado");
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

function mostrarErro(id, mensagem) {
    const el = document.getElementById(id);
    if (el) {
        el.textContent = mensagem;
    }
}

function limparErro(id) {
    const el = document.getElementById(id);
    if (el) el.textContent = "";
}

function limparTodosErros() {
    document.querySelectorAll(".erro").forEach(el => el.textContent = "");
}

function validar() {
    limparTodosErros();
    let valido = true;

    const campos = [
        { id: "nomePaciente",   erroId: "erro-nome",                        msg: "Nome do paciente é obrigatório." },
        { id: "dataNascimento", erroId: "erro-dataNascimento",              msg: "Data de nascimento é obrigatória." },
        { id: "cpfPaciente",    erroId: "erro-CPF_paciente",                msg: "CPF do paciente é obrigatório." },
        { id: "nomeMae",        erroId: "erro-nomeMae",                     msg: "Nome da mãe é obrigatório." },
        { id: "responsavel",    erroId: "erro-responsavel.nome",            msg: "Nome do responsável é obrigatório." },
        { id: "parentesco",     erroId: "erro-responsavel.grauParentesco",  msg: "Grau de parentesco é obrigatório." },
        { id: "cidade",         erroId: "erro-responsavel.cidade",          msg: "Cidade é obrigatória." },
        { id: "pais",           erroId: "erro-responsavel.pais",            msg: "País é obrigatório." },
        { id: "telefone1",      erroId: "erro-responsavel.telefone1",       msg: "Telefone para ligações é obrigatório." },
        { id: "email",          erroId: "erro-responsavel.email",           msg: "E-mail é obrigatório." },
    ];

    campos.forEach(({ id, erroId, msg }) => {
        const el = document.getElementById(id);
        if (!el || !el.value.trim()) {
            mostrarErro(erroId, msg);
            valido = false;
        }
    });

    if (!document.getElementById("sexo").value) {
        mostrarErro("erro-genero", "Sexo biológico é obrigatório.");
        valido = false;
    }

    if (!document.getElementById("estado").value) {
        mostrarErro("erro-responsavel.estado", "Estado é obrigatório.");
        valido = false;
    }

    return valido;
}

const formPaciente = document.getElementById("formPaciente");

formPaciente.addEventListener("submit", (e) => {
    e.preventDefault();

    if (!validar()) return;

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

    sessionStorage.setItem("dadosPaciente", JSON.stringify(paciente));
    window.location.href = "autoavaliacao-avaliacao.html";
});