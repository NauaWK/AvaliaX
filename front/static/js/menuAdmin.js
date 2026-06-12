(function () {
    const token = localStorage.getItem("token");

    const itensAdmin = document.querySelectorAll(".admin-only");

    itensAdmin.forEach(item => {
        item.style.display = "none";
    });

    if (!token) {
        return;
    }

    const payload = decodificarToken(token);

    if (!payload) {
        return;
    }

    console.log("Payload do token:", payload);

    if (usuarioEhAdmin(payload)) {
        itensAdmin.forEach(item => {
            item.style.display = "block";
        });
    }
})();

function decodificarToken(token) {
    try {
        const partePayload = token.split(".")[1];

        const payloadBase64 = partePayload
            .replace(/-/g, "+")
            .replace(/_/g, "/");

        const payloadJson = decodeURIComponent(
            atob(payloadBase64)
                .split("")
                .map(caractere => {
                    return "%" + ("00" + caractere.charCodeAt(0).toString(16)).slice(-2);
                })
                .join("")
        );

        return JSON.parse(payloadJson);

    } catch (error) {
        console.error("Erro ao decodificar token:", error);
        return null;
    }
}

function usuarioEhAdmin(payload) {
    if (!payload) {
        return false;
    }

    if (payload.role === "ADMIN" || payload.role === "ROLE_ADMIN") {
        return true;
    }

    if (payload.perfil === "ADMIN" || payload.perfil === "ROLE_ADMIN") {
        return true;
    }

    if (payload.authority === "ADMIN" || payload.authority === "ROLE_ADMIN") {
        return true;
    }

    if (Array.isArray(payload.roles)) {
        return payload.roles.includes("ADMIN") || payload.roles.includes("ROLE_ADMIN");
    }

    if (Array.isArray(payload.authorities)) {
        return payload.authorities.includes("ADMIN") || payload.authorities.includes("ROLE_ADMIN");
    }

    return false;
}