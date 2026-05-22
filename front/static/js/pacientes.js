
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