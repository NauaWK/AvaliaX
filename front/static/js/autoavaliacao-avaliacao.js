const API_BASE = "http://localhost:8080";

function pegarRadio(name) {
    const selecionado = document.querySelector(`input[name="${name}"]:checked`);
    return selecionado ? selecionado.value : null;
}

function pegarSintomas() {
    const checkboxes = document.querySelectorAll('input[name="sintomas"]:checked');
    return Array.from(checkboxes)
        .map(cb => cb.value)
        .filter(s => s !== "NENHUM");
}

document.getElementById("formAvaliacao").addEventListener("submit", async (e) => {
    e.preventDefault();

    const dadosPaciente = sessionStorage.getItem("dadosPaciente");

    if (!dadosPaciente) {
        alert("Dados do paciente não encontrados. Volte e preencha novamente.");
        window.location.href = "autoavaliacao-paciente.html";
        return;
    }

    const sintomas = pegarSintomas();

    if (sintomas.length === 0) {
        alert("Selecione pelo menos uma opção em sinais ou sintomas.");
        return;
    }

    const campos = [
        "fezExameDNA", "autismo", "irmaos",
        "historicoFamiliarNeuro", "menopausaPrecoce", "ataxiaTremores"
    ];

    for (const campo of campos) {
        if (!pegarRadio(campo)) {
            alert("Preencha todas as perguntas obrigatórias.");
            return;
        }
    }

    const paciente = JSON.parse(dadosPaciente);

    const body = {
        paciente: paciente,
        responsavel: paciente.responsavel,
        avaliacao: {
            CPF_paciente: paciente.CPF_paciente,
            detalhes: null,
            testeDna: pegarRadio("fezExameDNA"),
            interesseExame: pegarRadio("interesseExameDNA"),
            resultadoExame: document.querySelector('select[name="resultadoDNA"]').value || null,
            diagnosticoAutismo: pegarRadio("autismo"),
            possuiIrmaos: pegarRadio("irmaos"),
            antecedentesDeficiencia: pegarRadio("historicoFamiliarNeuro"),
            antecedentesMenopausa: pegarRadio("menopausaPrecoce"),
            antecedentesAtaxia: pegarRadio("ataxiaTremores"),
            sintomas: sintomas.map(s => ({ nome: s, presente: true }))
        }
    };

    const btn = document.querySelector(".btn-salvar");
    btn.disabled = true;
    btn.textContent = "Enviando...";

    try {
        const res = await fetch(`${API_BASE}/api/avaliacoes/autoavaliacao`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });

        if (!res.ok) {
            const erro = await res.json();
            alert(erro.message || "Erro ao enviar avaliação.");
            return;
        }

        sessionStorage.removeItem("dadosPaciente");

        document.getElementById("formActions").style.display = "none";
        document.getElementById("msgConfirmacao").style.display = "block";

    } catch (err) {
        alert("Não foi possível conectar ao servidor.");
    } finally {
        btn.disabled = false;
        btn.textContent = "Enviar Avaliação";
    }
});