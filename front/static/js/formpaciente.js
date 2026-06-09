
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