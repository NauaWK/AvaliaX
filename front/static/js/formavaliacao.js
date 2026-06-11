const token = localStorage.getItem("token");
console.log(token)
const cpfPaciente = localStorage.getItem("cpfPacienteAvaliacao");

if (!token) {
    window.location.href = "login.html";
}



if (!cpfPaciente) {
    alert("Nenhum paciente selecionado para avaliação.");
    window.location.href = "pacientes.html";
}

const formAvaliacao = document.getElementById("formAvaliacao");

function pegarRadio(name) {
    const selecionado = document.querySelector(`input[name="${name}"]:checked`);
    return selecionado ? converterValor(selecionado.value) : null;
}

function converterValor(valor) {
    if (valor === "true") return true;
    if (valor === "false") return false;
    return valor;
}

function pegarSintomas() {
    const checkboxes = document.querySelectorAll('input[name="sintomas"]:checked');

    return Array.from(checkboxes).map(checkbox => checkbox.value);
}

function mostrarErro(mensagem) {
    alert(mensagem);
}

function converterResposta(valor) {
    if (valor === true) return "SIM";
    if (valor === false) return "NAO";
    if (valor === "NAO_SEI") return "NAO_SEI";
    return null;
}

formAvaliacao.addEventListener("submit", async function (event) {
    event.preventDefault();

    const sintomas = pegarSintomas();

    if (sintomas.length === 0) {
        mostrarErro("Selecione pelo menos uma opção em sinais ou sintomas.");
        return;
    }

    const avaliacao = {
        CPF_paciente: cpfPaciente,
        detalhes: null,

        testeDna: converterResposta(pegarRadio("fezExameDNA")),
        interesseExame: converterResposta(pegarRadio("interesseExameDNA")),
        resultadoExame: formAvaliacao.resultadoDNA.value || null,

        diagnosticoAutismo: converterResposta(pegarRadio("autismo")),
        possuiIrmaos: converterResposta(pegarRadio("irmaos")),
        antecedentesDeficiencia: converterResposta(pegarRadio("historicoFamiliarNeuro")),
        antecedentesMenopausa: converterResposta(pegarRadio("menopausaPrecoce")),
        antecedentesAtaxia: converterResposta(pegarRadio("ataxiaTremores")),

        sintomas: pegarSintomas().map(sintoma => ({
            nome: sintoma
        }))
    };


    console.log("CPF do paciente:", cpfPaciente);
    console.log("Avaliação enviada:", avaliacao);
    console.log("Authorization enviado:", `Bearer ${token}`);
    try {
        const response = await fetch("http://localhost:8080/api/avaliacoes", {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(avaliacao)
    });

        if (!response.ok) {
            let erro;

            try {
                erro = await response.json();
            } catch {
                erro = { message: "Erro ao salvar avaliação." };
            }

            console.log("Erro do backend:", erro);
            alert(erro.message || erro.erro || "Erro ao salvar avaliação.");
            return;
        }

        const resposta = await response.json();

        console.log("Avaliação salva:", resposta);

        alert("Avaliação salva com sucesso!");

        localStorage.removeItem("cpfPacienteAvaliacao");

        window.location.href = "pacientes.html";

    } catch (error) {
        console.log("Erro no fetch:", error);
        alert("Erro ao conectar com o servidor.");
    }
});