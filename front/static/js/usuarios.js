const API_BASE = 'http://localhost:8080';

const abrirModal  = document.getElementById('btn');
const fecharModal = document.getElementById('fecharModal');
const overlay     = document.getElementById('overlay');

abrirModal.addEventListener('click', () => {
    overlay.style.display = 'flex';
});

fecharModal.addEventListener('click', fechar);

overlay.addEventListener('click', (e) => {
    if (e.target === overlay) fechar();
});

function fechar() {
    overlay.style.display = 'none';
    document.getElementById('formUsuario').reset();
    clearMsg();
}

function showMsg(texto, tipo) {
    const el = document.getElementById('msgModal');
    el.textContent = texto;
    el.className = 'msg ' + tipo;
}

function clearMsg() {
    const el = document.getElementById('msgModal');
    el.textContent = '';
    el.className = 'msg';
}

function getToken() {
    return localStorage.getItem('token');
}

function headers() {
    return {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getToken()
    };
}

async function carregarUsuarios() {
    try {
        const res = await fetch(`${API_BASE}/api/usuarios`, { headers: headers() });

        if (!res.ok) {
            document.getElementById('listaUsuarios').textContent = 'Erro ao carregar usuários.';
            return;
        }

        const usuarios = await res.json();
        renderizarUsuarios(usuarios);
    } catch (err) {
        document.getElementById('listaUsuarios').textContent = 'Não foi possível conectar ao servidor.';
    }
}

function renderizarUsuarios(lista) {
    const container = document.getElementById('listaUsuarios');
    const ativos = lista.filter(u => u.ativo !== false);

    if (lista.length === 0) {
        container.innerHTML = '<p class="vazio">Nenhum usuário cadastrado.</p>';
        return;
    }

    container.innerHTML = `
        <table>
            <thead>
                <tr>
                    <th>Nome</th>
                    <th>Login</th>
                    <th>Email</th>
                    <th>Perfil</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                ${ativos.map(u => `
                    <tr>
                        <td>${u.nome}</td>
                        <td>${u.login}</td>
                        <td>${u.email ?? '—'}</td>
                        <td><span class="badge ${u.perfil === 'ADMIN' ? 'badge-admin' : 'badge-user'}">${u.perfil}</span></td>
                        <td>
                            <button class="btn-excluir" onclick="excluirUsuario(${u.id})">Excluir</button>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;
}

document.getElementById('busca').addEventListener('input', (e) => {
    const termo = e.target.value.toLowerCase();
    const linhas = document.querySelectorAll('table tbody tr');
    linhas.forEach(linha => {
        linha.style.display = linha.textContent.toLowerCase().includes(termo) ? '' : 'none';
    });
});

document.getElementById('formUsuario').addEventListener('submit', async (e) => {
    e.preventDefault();
    clearMsg();

    const nome   = document.getElementById('nome').value.trim();
    const login  = document.getElementById('login').value.trim();
    const email  = document.getElementById('email').value.trim();
    const senha  = document.getElementById('senha').value;
    const perfil = document.querySelector('input[name="perfil"]:checked').value;

    const btn = document.querySelector('.btn-submit');
    btn.disabled = true;
    btn.textContent = 'Salvando...';

    try {
        const res = await fetch(`${API_BASE}/api/usuarios`, {
            method: 'POST',
            headers: headers(),
            body: JSON.stringify({ nome, login, email: email || null, senha, perfil })
        });

        const data = await res.json();

        if (!res.ok) {
            showMsg(data.message || 'Erro ao cadastrar usuário.', 'error');
            return;
        }

        fechar();
        carregarUsuarios();
    } catch (err) {
        showMsg('Não foi possível conectar ao servidor.', 'error');
    } finally {
        btn.disabled = false;
        btn.textContent = 'Salvar';
    }
});

async function excluirUsuario(id) {
    if (!confirm('Deseja realmente excluir este usuário?')) return;

    try {
        const res = await fetch(`${API_BASE}/api/usuarios/${id}`, {
            method: 'DELETE',
            headers: headers()
        });

        if (!res.ok) {
            alert('Erro ao excluir usuário.');
            return;
        }

        carregarUsuarios();
    } catch (err) {
        alert('Não foi possível conectar ao servidor.');
    }
}

carregarUsuarios();