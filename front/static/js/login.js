const API_BASE = 'http://localhost:8080';

function showMsg(text, type) {
  const el = document.getElementById('msg');
  el.textContent = text;
  el.className = 'msg ' + type;
}

function clearMsg() {
  const el = document.getElementById('msg');
  el.textContent = '';
  el.className = 'msg';
}

async function handleSubmit() {
  clearMsg();

  const login = document.getElementById('login').value.trim();
  const senha = document.getElementById('senha').value;
  const btn   = document.getElementById('btn-submit');

  if (!login || !senha) {
    showMsg('Preencha todos os campos.', 'error');
    return;
  }

  btn.disabled = true;
  btn.textContent = 'Entrando...';

  try {
    const res = await fetch(`${API_BASE}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ login, senha })
    });

    const data = await res.json();

    if (!res.ok) {
      showMsg(data.message || 'Login ou senha incorretos.', 'error');
      return;
    }

    localStorage.setItem('token', data.token);
    window.location.href = 'index.html';
  } catch (err) {
    showMsg('Não foi possível conectar ao servidor.', 'error');
  } finally {
    btn.disabled = false;
    btn.textContent = 'Entrar';

  }
}