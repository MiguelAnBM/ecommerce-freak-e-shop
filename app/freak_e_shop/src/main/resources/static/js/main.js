// ── Funcionalidad Principal de Freak-E Shop ────────

document.addEventListener('DOMContentLoaded', function () {
    initNavbarSearch();

    // ── Animación de Desvanecimiento ───────────────────
    const fadeElements = document.querySelectorAll('.fade-in-section');
    if (fadeElements.length > 0) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('visible');
                }
            });
        }, { threshold: 0.1, rootMargin: '0px 0px -50px 0px' });
        fadeElements.forEach(el => observer.observe(el));
    }

    // ── Efecto de Desplazamiento del Navbar ────────────
    const navbar = document.querySelector('.stride-navbar');
    if (navbar) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 50) navbar.classList.add('scrolled');
            else navbar.classList.remove('scrolled');
        });
    }

    // Actualización del icono de tema
    const savedTheme = localStorage.getItem('stride-theme');
    if (savedTheme === 'light') {
        const icon = document.getElementById('themeIcon');
        if (icon) { icon.classList.remove('bi-sun'); icon.classList.add('bi-moon-stars'); }
    }

    // ── Inicialización de Base de Datos Simulada (Usuarios) ───
    function getStoredUsers() {
        const stored = localStorage.getItem('stride_db_users');
        let users = stored ? JSON.parse(stored) : [];
        
        const defaults = [
            { email: 'admin', password: 'admin123', role: 'admin', name: 'Administrador' },
            { email: 'user@ejemplo.com', password: 'user123', role: 'user', name: 'Usuario Prueba' }
        ];

        // Asegurar que el admin y el usuario de prueba existan
        defaults.forEach(def => {
            if (!users.some(u => u.email === def.email)) {
                users.push(def);
            }
        });

        localStorage.setItem('stride_db_users', JSON.stringify(users));
        return users;
    }

    // Simulación de autenticación
    async function authenticateUser(username, password, role) {
        const users = getStoredUsers();
        const found = users.find(u => u.email === username.trim() && u.password === password.trim() && u.role === role);
        
        if (found) {
            try {
                const syncUrl = role === 'admin' ? '/api/login' : '/api/login-usuario';
                const syncResponse = await fetch(syncUrl, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(found)
                });
                if (!syncResponse.ok) {
                    throw new Error("No se pudo iniciar la sesión en el servidor.");
                }
            } catch(e) {
                console.error("Error sincronizando sesión en el servidor:", e);
                throw new Error("Error de conexión con el servidor. Inténtalo de nuevo.");
            }
            return { success: true, user: found };
        } else {
            throw new Error('Credenciales incorrectas');
        }
    }

    async function registerUser(name, email, password) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                const users = getStoredUsers();
                if (users.some(u => u.email === email)) {
                    reject(new Error('El correo ya está registrado'));
                    return;
                }
                const newUser = { email, password, role: 'user', name };
                users.push(newUser);
                localStorage.setItem('stride_db_users', JSON.stringify(users));
                resolve({ success: true, user: newUser });
            }, 800);
        });
    }

    function handleLoginForm(formId, role, successCallback) {
        const form = document.getElementById(formId);
        if (!form) return;
        form.addEventListener('submit', async function (e) {
            e.preventDefault();
            const btn = form.querySelector('button[type="submit"]');
            const originalText = btn.innerHTML;
            const usernameInput = form.querySelector('input[type="text"], input[type="email"]');
            const passwordInput = form.querySelector('input[type="password"]');
            
            btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Verificando...';
            btn.disabled = true;
            
            try {
                const result = await authenticateUser(usernameInput.value, passwordInput.value, role);
                btn.innerHTML = '<i class="bi bi-check-circle"></i> ¡Éxito!';
                btn.classList.replace('btn-stride-primary', 'btn-success');
                setTimeout(() => {
                    const modal = bootstrap.Modal.getInstance(form.closest('.modal'));
                    if (modal) modal.hide();
                    successCallback(result.user);
                }, 600);
            } catch (error) {
                btn.innerHTML = '<i class="bi bi-x-circle"></i> Error';
                btn.classList.replace('btn-stride-primary', 'btn-danger');
                alert(error.message);
                setTimeout(() => {
                    btn.innerHTML = originalText;
                    btn.disabled = false;
                    btn.classList.replace('btn-danger', 'btn-stride-primary');
                }, 1500);
            }
        });
    }

    function login(user) {
        localStorage.setItem('stride_active_user', JSON.stringify(user));
        checkAuthStatus();
    }

    window.logout = async function() {
        try {
            // Siempre llamamos al servidor para invalidar la sesión de Spring
            await fetch('/api/logout', { method: 'POST' });
        } catch (e) {
            console.error("Error al cerrar sesión en el servidor:", e);
        }
        localStorage.removeItem('stride_active_user');
        window.location.href = '/';
    };

    async function checkAuthStatus() {
        const activeUserStr = localStorage.getItem('stride_active_user');
        if (!activeUserStr) return;
        
        const activeUser = JSON.parse(activeUserStr);

        // Validar sesión con el servidor si es admin
        if (activeUser.role === 'admin') {
            try {
                const response = await fetch('/api/check-session');
                if (!response.ok) {
                    throw new Error("Sesión expirada");
                }
            } catch (e) {
                console.warn("La sesión del servidor no coincide con el estado local. Limpiando...");
                localStorage.removeItem('stride_active_user');
                window.location.reload();
                return;
            }
        }
        
        const icon = document.getElementById('userIcon');
        if (icon) {
            icon.classList.remove('bi-person-circle');
            icon.classList.add('bi-person-check-fill', 'text-success');
        }
        
        const dropdownMenu = document.getElementById('userDropdownMenu');
        if (dropdownMenu) {
            let html = `<li><span class="dropdown-item-text fw-bold">Hola, ${activeUser.name}</span></li><li><hr class="dropdown-divider"></li>`;
            
            if (activeUser.role === 'admin') {
                html += `<li><a class="dropdown-item" href="/admin">Panel de Administración</a></li>`;
                html += `<li><a class="dropdown-item" href="/admin/reportes">Reportes</a></li>`;
            }
            
            html += `<li><a class="dropdown-item text-danger" href="#" onclick="logout()">Cerrar sesión</a></li>`;
            dropdownMenu.innerHTML = html;
        }
    }

    checkAuthStatus();

    handleLoginForm('userLoginForm', 'user', (user) => {
        login(user);
        // Si estamos en la página del carrito, recargamos para que el usuario pueda pagar
        if (window.location.pathname.includes('/cart') || window.location.pathname.includes('/carrito')) {
            window.location.reload();
        }
    });

    handleLoginForm('adminLoginForm', 'admin', (user) => {
        login(user);
        window.location.href = '/admin';
    });

    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', async function (e) {
            e.preventDefault();
            const btn = registerForm.querySelector('button[type="submit"]');
            const originalText = btn.innerHTML;
            const nameInput = document.getElementById('regName');
            const emailInput = document.getElementById('regEmail');
            const passwordInput = document.getElementById('regPassword');
            
            btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Registrando...';
            btn.disabled = true;
            
            try {
                const result = await registerUser(nameInput.value, emailInput.value, passwordInput.value);
                
                // Sincronizar sesión con el servidor tras registro
                await fetch('/api/login-usuario', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(result.user)
                });

                btn.innerHTML = '<i class="bi bi-check-circle"></i> ¡Cuenta Creada!';
                btn.classList.replace('btn-stride-primary', 'btn-success');
                setTimeout(() => {
                    const modal = bootstrap.Modal.getInstance(registerForm.closest('.modal'));
                    if (modal) modal.hide();
                    login(result.user);
                }, 600);
            } catch (error) {
                btn.innerHTML = '<i class="bi bi-x-circle"></i> Error';
                btn.classList.replace('btn-stride-primary', 'btn-danger');
                alert(error.message);
                setTimeout(() => {
                    btn.innerHTML = originalText;
                    btn.disabled = false;
                    btn.classList.replace('btn-danger', 'btn-stride-primary');
                }, 1500);
            }
        });
    }

});

// ── Búsqueda de Navbar ─────────────────────────────

function initNavbarSearch() {
    const btnOpen = document.getElementById('searchBtnOpenOverlay');
    const btnClose = document.getElementById('searchBtnCloseOverlay');
    const overlay = document.getElementById('searchOverlay');
    const input = document.getElementById('navSearchInput');

    if (!btnOpen || !overlay) return;

    btnOpen.addEventListener('click', () => {
        overlay.classList.add('is-visible');
        void overlay.offsetHeight;
        overlay.classList.add('is-active');
        document.body.style.overflow = 'hidden';
        setTimeout(() => input.focus(), 300);
    });

    const closeOverlay = () => {
        overlay.classList.remove('is-active');
        document.body.style.overflow = '';
        overlay.addEventListener('transitionend', function handler() {
            overlay.removeEventListener('transitionend', handler);
            if (!overlay.classList.contains('is-active')) {
                overlay.classList.remove('is-visible');
            }
        });
    };

    if (btnClose) btnClose.addEventListener('click', closeOverlay);

    overlay.addEventListener('click', (e) => {
        if (e.target === overlay) closeOverlay();
    });

    window.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && overlay.classList.contains('is-active')) {
            closeOverlay();
        }
    });

    // Búsqueda en vivo (Autocomplete)
    const dropdown = document.getElementById('searchDropdown');
    let searchTimeout = null;

    if (input && dropdown) {
        input.addEventListener('input', function() {
            const query = this.value.trim();
            
            clearTimeout(searchTimeout);
            
            if (query.length < 2) {
                dropdown.innerHTML = '';
                dropdown.style.display = 'none';
                return;
            }
            
            searchTimeout = setTimeout(() => {
                fetch(`/api/productos/buscar?q=${encodeURIComponent(query)}`)
                    .then(response => response.json())
                    .then(data => {
                        if (data.length === 0) {
                            dropdown.innerHTML = `<div class="search-results-container p-3 text-muted text-center border-secondary bg-dark text-light" style="border-radius: 12px; background-color: var(--stride-bg-card) !important; color: var(--stride-text-primary) !important;">No se encontraron productos para "${escapeHtml(query)}"</div>`;
                            dropdown.style.display = 'block';
                            return;
                        }
                        
                        let html = '<div class="search-results-wrapper"><div class="list-group list-group-flush search-results-container">';
                        data.forEach(p => {
                            html += `
                                <a href="/producto/${p.id}" class="list-group-item list-group-item-action search-result-item d-flex align-items-center gap-3">
                                    <img src="${p.imagen}" alt="${escapeHtml(p.nombre)}" class="search-result-img">
                                    <div class="search-result-text">
                                        <h6 class="mb-0 text-truncate" style="max-width: 250px;">${escapeHtml(p.nombre)}</h6>
                                        <small class="text-accent">${formatearPrecioCOP(p.precio)}</small>
                                    </div>
                                </a>
                            `;
                        });
                        
                        // Añadir opción para ver todos los resultados si hay
                        html += `
                            <div class="search-result-footer text-center">
                                <button type="button" class="btn btn-sm btn-link text-accent text-decoration-none w-100 fw-bold" onclick="document.getElementById('navSearchForm').submit();">
                                    Ver todos los resultados
                                </button>
                            </div>
                        `;
                        
                        // Close the container and wrapper
                        html += '</div></div>';
                        
                        dropdown.innerHTML = html;
                        dropdown.style.display = 'block';
                    })
                    .catch(error => {
                        console.error('Error buscando productos:', error);
                    });
            }, 300); // 300ms debounce
        });
    }

    // Ocultar dropdown al hacer click fuera
    document.addEventListener('click', (e) => {
        if (dropdown && !dropdown.contains(e.target) && e.target !== input) {
            dropdown.style.display = 'none';
        }
    });

    // El formulario de búsqueda ya envía a /catalog?q=... via form action
}

// Helper para formatear precio si no existe globalmente
function formatearPrecioCOP(precio) {
    const valorTruncado = Math.trunc(precio * 1000) / 1000;
    return '$ ' + valorTruncado.toLocaleString('es-CO', { 
        minimumFractionDigits: 0,
        maximumFractionDigits: 3 
    });
}

// ── Utilidades ─────────────────────────────────────

function escapeHtml(value) {
    const element = document.createElement('div');
    element.textContent = value;
    return element.innerHTML;
}

function toggleTheme() {
    document.body.classList.toggle('light-theme');
    const icon = document.getElementById('themeIcon');
    const isLight = document.body.classList.contains('light-theme');
    if (icon) {
        icon.className = isLight ? 'bi bi-moon-stars' : 'bi bi-sun';
    }
    localStorage.setItem('stride-theme', isLight ? 'light' : 'dark');
}
