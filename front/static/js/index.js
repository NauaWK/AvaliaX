const token = localStorage.getItem("token");
if (!token) {
    window.location.href = "login.html"
}

//Nome usuario
const payload = JSON.parse(atob(token.split('.')[1]));

const nome = payload.sub;

const nomeUser = document.getElementById("info-user")

nomeUser.innerText = nome


fetch("http://localhost:8080/api/dashboard", {
    headers: {
        "Authorization": `Bearer ${token}`
    }
})


.then(response => {

    if (response.status === 401 || response.status === 403) {

        localStorage.removeItem("token");

        window.location.href = "login.html";

        return;
    }

    return response.json();
})
.then(data => {
    const totalPacientes = document.getElementById("Total-pacientes")
    const totalAvaliacoes = document.getElementById("Total-avaliacoes")
    const totalTestesIndicados = document.getElementById("Total-testes-indicados")
    const totalInconclusivos = document.getElementById("Total-inconclusivo")

    totalPacientes.textContent = data.totalPacientes
    totalAvaliacoes.textContent = data.totalAvaliacoes
    totalTestesIndicados.textContent = data.totalTestesIndicados
    totalInconclusivos.textContent = data.totalInconclusivos

    const divAvaliacaoRecente = document.getElementById("avaliacao-recente")
    const listaElemento = document.getElementById('lista-pacientes');
    const pacientes = data.avaliacoesRecentes
    console.log(pacientes)

    if (pacientes.length == 0) {
        divAvaliacaoRecente.textContent = "Nenhuma avaliação encontrada."
    }else {
        //codigo comentado so para teste
        pacientes.forEach(paciente => {
                const li = document.createElement('li');  
                li.classList.add("card-avaliacao")     
                li.textContent = `${paciente.paciente} Atendido em: ${paciente.dataAvaliacao}`;
                listaElemento.appendChild(li);
            });

}
    }


})
.catch(error => {
    console.error(error);
});

//loout

const btnLogout = document.getElementById("logout")

btnLogout.addEventListener("click", logout)

function logout() {
    localStorage.removeItem("token")

    window.location.href = "login.html"
}

