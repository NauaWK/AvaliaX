const API_BASE = 'http://localhost:8080'; // ajuste para a URL do seu backend

/* ── Alternância de abas ── */
function switchTab(tab) {
  const isRegister = tab === 'register';
  document.getElementById('tab-login').classList.toggle('active', !isRegister);
  document.getElementById('tab-register').classList.toggle('active', isRegister);
  document.querySelectorAll('.register-only').forEach(el =>
    el.classList.toggle('show', isRegister)
  );
  document.getElementById('btn-submit').textContent = isRegister ? 'Criar conta' : 'Entrar';
  clearMsg();
}

/* ── Mensagens de feedback ── */
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

/* ── Envio do formulário ── */
async function handleSubmit() {
  clearMsg();

  const isRegister = document.getElementById('tab-register').classList.contains('active');
  const email  = document.getElementById('email').value.trim();
  const senha  = document.getElementById('senha').value;
  const btn    = document.getElementById('btn-submit');

  if (!email || !senha) {
    showMsg('Preencha todos os campos.', 'error');
    return;
  }

  if (isRegister) {
    const nome     = document.getElementById('name').value.trim();
    const confirma = document.getElementById('confirmar').value;

    if (!nome) {
      showMsg('Informe seu nome completo.', 'error');
      return;
    }
    if (senha !== confirma) {
      showMsg('As senhas não coincidem.', 'error');
      return;
    }

    btn.disabled = true;
    btn.textContent = 'Criando conta...';

    try {
      const res = await fetch(`${API_BASE}/api/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: nome, login: email, senha: senha })
      });

      const data = await res.json();

      if (!res.ok) {
        showMsg(data.message || 'Erro ao criar conta.', 'error');
        return;
      }

      showMsg('Conta criada! Faça login para continuar.', 'success');
      switchTab('login');
    } catch (err) {
      showMsg('Não foi possível conectar ao servidor.', 'error');
    } finally {
      btn.disabled = false;
      btn.textContent = 'Criar conta';
    }

  } else {
    btn.disabled = true;
    btn.textContent = 'Entrando...';

    try {
      const res = await fetch(`${API_BASE}/api/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ login: email, senha: senha })
      });

      const data = await res.json();

      if (!res.ok) {
        showMsg(data.message || 'Email ou senha incorretos.', 'error');
        return;
      }

      // Armazena o token e redireciona para o painel
      localStorage.setItem('token', data.token);
      window.location.href = 'index.html';
    } catch (err) {
      showMsg('Não foi possível conectar ao servidor.', 'error');
    } finally {
      btn.disabled = false;
      btn.textContent = 'Entrar';
    }
  }
}
