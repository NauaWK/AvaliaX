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