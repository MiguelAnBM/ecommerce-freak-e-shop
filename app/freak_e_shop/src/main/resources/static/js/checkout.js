// ── Flujo de Pago — Modal de Checkout ──────────────────
// Controla la lógica completa del modal de pago desde el carrito.

(function () {

    // ── Variables de estado del modal ──────────────────
    let datosPago = {
        direccion: '',
        telefono: '',
        numeroPedido: '',
        fecha: '',
        metodoPago: null // 'NEQUI' | 'CONTRAENTREGA'
    };
    let qrInstance = null;
    let cartItems = [];
    let cartTotal = 0;

    // ── Inicialización ─────────────────────────────────
    document.addEventListener('DOMContentLoaded', function () {
        if (window.__cartData) {
            try {
                var raw = window.__cartData.items;
                cartItems = typeof raw === 'string' ? JSON.parse(raw) : (Array.isArray(raw) ? raw : []);
                cartTotal = parseFloat(window.__cartData.total) || 0;
            } catch (e) {
                cartItems = [];
                cartTotal = 0;
            }
        }

        // Eventos de validación en tiempo real
        const dirInput = document.getElementById('direccionEnvio');
        const telInput = document.getElementById('telefonoContacto');
        if (dirInput) {
            dirInput.addEventListener('input', function () {
                ocultarError('direccionEnvio');
            });
        }
        if (telInput) {
            telInput.addEventListener('input', function () {
                ocultarError('telefonoContacto');
            });
        }
    });

    // ── Funciones públicas ─────────────────────────────

    // Abre el modal de pago (Paso 1) 
    window.abrirModalPago = function () {
        const modal = document.getElementById('pagoModal');
        if (!modal) return;

        // Resetear al paso 1
        resetearModal();
        modal.style.display = 'flex';
        document.body.style.overflow = 'hidden';
    };

    // Cierra el modal y limpia campos
    window.cerrarModalPago = function () {
        const modal = document.getElementById('pagoModal');
        if (!modal) return;

        modal.style.display = 'none';
        document.body.style.overflow = '';
        resetearModal();
    };

    // Valida paso 1 y avanza al paso 2
    window.avanzarPaso2 = function () {
        if (!validarPaso1()) return;

        const dirInput = document.getElementById('direccionEnvio');
        const telInput = document.getElementById('telefonoContacto');

        datosPago.direccion = dirInput.value.trim();
        datosPago.telefono = telInput.value.trim();
        datosPago.numeroPedido = 'PED-' + Date.now();
        datosPago.fecha = obtenerFechaColombia();

        construirResumenFactura();

        document.getElementById('pagoStep1').style.display = 'none';
        document.getElementById('pagoStep2').style.display = 'block';
        document.getElementById('pagoModalTitulo').textContent = 'Resumen de tu pedido';
    };

    // Selecciona un método de pago 
    window.seleccionarMetodoPago = function (metodo) {
        datosPago.metodoPago = metodo;

        const cardNequi = document.getElementById('cardNequi');
        const cardContraentrega = document.getElementById('cardContraentrega');
        const qrContainer = document.getElementById('qrNequiContainer');
        const btnDescargar = document.getElementById('btnDescargarFactura');

        // Resetear selección visual
        cardNequi.classList.remove('seleccionado');
        cardContraentrega.classList.remove('seleccionado');

        if (metodo === 'NEQUI') {
            cardNequi.classList.add('seleccionado');
            // Generar QR
            qrContainer.style.display = 'flex';
            destruirQR();
            var qrDiv = document.getElementById('qrNequi');
            qrDiv.innerHTML = '';
            if (typeof QRCode !== 'undefined') {
                qrInstance = new QRCode(qrDiv, {
                    text: 'https://dotfic.com/zDyk',
                    width: 180,
                    height: 180,
                    colorDark: '#200040',
                    colorLight: '#ffffff',
                    correctLevel: QRCode.CorrectLevel.M
                });
            }
        } else {
            cardContraentrega.classList.add('seleccionado');
            // Destruir QR si existía
            destruirQR();
            qrContainer.style.display = 'none';
        }

        // Habilitar botón de descarga
        btnDescargar.disabled = false;
        btnDescargar.classList.remove('pago-btn-disabled');
    };

    // Descarga la factura como .txt y confirma el pedido 
    window.descargarFactura = function () {
        const btnDescargar = document.getElementById('btnDescargarFactura');
        if (btnDescargar.disabled) return;

        const contenido = generarContenidoTxt();
        const blob = new Blob([contenido], { type: 'text/plain;charset=utf-8' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'factura-' + datosPago.numeroPedido + '.txt';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        URL.revokeObjectURL(url);

        // Confirmar pedido en el servidor
        confirmarPedido();
    };

    // ── Funciones internas ─────────────────────────────

    function resetearModal() {
        const step1 = document.getElementById('pagoStep1');
        const step2 = document.getElementById('pagoStep2');
        const titulo = document.getElementById('pagoModalTitulo');
        const dirInput = document.getElementById('direccionEnvio');
        const telInput = document.getElementById('telefonoContacto');
        const mensajeEstado = document.getElementById('pagoMensajeEstado');
        const btnDescargar = document.getElementById('btnDescargarFactura');
        const btnCerrar = document.getElementById('btnCerrarFinal');

        if (step1) step1.style.display = 'block';
        if (step2) step2.style.display = 'none';
        if (titulo) titulo.textContent = 'Datos de envío';
        if (dirInput) dirInput.value = '';
        if (telInput) telInput.value = '';
        if (mensajeEstado) { mensajeEstado.style.display = 'none'; mensajeEstado.textContent = ''; }
        if (btnDescargar) { btnDescargar.style.display = 'inline-flex'; btnDescargar.disabled = true; btnDescargar.classList.add('pago-btn-disabled'); }
        if (btnCerrar) btnCerrar.style.display = 'none';

        ocultarError('direccionEnvio');
        ocultarError('telefonoContacto');
        destruirQR();

        datosPago.metodoPago = null;
        datosPago.numeroPedido = '';
        datosPago.fecha = '';
        datosPago.direccion = '';
        datosPago.telefono = '';

        var cardNequi = document.getElementById('cardNequi');
        var cardContraentrega = document.getElementById('cardContraentrega');
        if (cardNequi) cardNequi.classList.remove('seleccionado');
        if (cardContraentrega) cardContraentrega.classList.remove('seleccionado');
        var qrContainer = document.getElementById('qrNequiContainer');
        if (qrContainer) qrContainer.style.display = 'none';
    }

    function validarPaso1() {
        let valido = true;
        const dirInput = document.getElementById('direccionEnvio');
        const telInput = document.getElementById('telefonoContacto');

        // Validar dirección
        if (!dirInput.value.trim() || dirInput.value.trim().length < 5) {
            mostrarError('direccionEnvio', 'La dirección debe tener al menos 5 caracteres.');
            valido = false;
        } else {
            ocultarError('direccionEnvio');
        }

        // Validar teléfono
        const telRegex = /^[0-9]{7,10}$/;
        if (!telRegex.test(telInput.value.trim())) {
            mostrarError('telefonoContacto', 'Ingresa un número de 7 a 10 dígitos numéricos.');
            valido = false;
        } else {
            ocultarError('telefonoContacto');
        }

        return valido;
    }

    function mostrarError(inputId, mensaje) {
        const errorEl = document.getElementById(inputId + 'Error');
        if (errorEl) {
            errorEl.textContent = mensaje;
            errorEl.style.display = 'block';
        }
        const inputEl = document.getElementById(inputId);
        if (inputEl) inputEl.classList.add('pago-input-error');
    }

    function ocultarError(inputId) {
        const errorEl = document.getElementById(inputId + 'Error');
        if (errorEl) {
            errorEl.textContent = '';
            errorEl.style.display = 'none';
        }
        const inputEl = document.getElementById(inputId);
        if (inputEl) inputEl.classList.remove('pago-input-error');
    }

    function destruirQR() {
        if (qrInstance) {
            qrInstance.clear();
            qrInstance = null;
        }
        var qrDiv = document.getElementById('qrNequi');
        if (qrDiv) qrDiv.innerHTML = '';
    }

    function obtenerFechaColombia() {
        const ahora = new Date();
        const opciones = {
            timeZone: 'America/Bogota',
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            hour12: false
        };
        const partes = new Intl.DateTimeFormat('es-CO', opciones).formatToParts(ahora);
        let d = '', m = '', y = '', h = '', min = '';
        partes.forEach(p => {
            if (p.type === 'day') d = p.value;
            if (p.type === 'month') m = p.value;
            if (p.type === 'year') y = p.value;
            if (p.type === 'hour') h = p.value;
            if (p.type === 'minute') min = p.value;
        });
        return d + '/' + m + '/' + y + ' ' + h + ':' + min;
    }

    function formatearCOP(precio) {
        const num = Math.round(precio);
        return '$ ' + num.toLocaleString('es-CO', { maximumFractionDigits: 0 });
    }

    function construirResumenFactura() {
        const pre = document.getElementById('facturaResumen');
        if (!pre) return;

        let lineas = [];
        lineas.push(centrar('FREAK-E SHOP', 45));
        lineas.push(centrar('Fecha: ' + datosPago.fecha, 45));
        lineas.push(centrar('Pedido: ' + datosPago.numeroPedido, 45));
        lineas.push('─'.repeat(45));
        lineas.push(' DATOS DE ENVÍO');
        lineas.push('  Dirección : ' + truncar(datosPago.direccion, 30));
        lineas.push('  Teléfono  : ' + datosPago.telefono);
        lineas.push('─'.repeat(45));
        lineas.push(' PRODUCTOS');

        cartItems.forEach(function (item) {
            lineas.push('  ' + truncar(item.nombre, 40));
            lineas.push('    Cant.: ' + item.cantidad + '   Precio: ' + formatearCOP(item.precio));
            lineas.push('    Subtotal: ' + formatearCOP(item.subtotal));
        });

        lineas.push('─'.repeat(45));
        lineas.push('  TOTAL A PAGAR: ' + formatearCOP(cartTotal));

        pre.textContent = lineas.join('\n');
    }

    function generarContenidoTxt() {
        const W = 50;
        const sep = '='.repeat(W);
        const lin = '-'.repeat(W);
        let txt = '';

        txt += sep + '\n';
        txt += centrarPad('FREAK-E SHOP', W) + '\n';
        txt += centrarPad('Tu compra, confirmada.', W) + '\n';
        txt += sep + '\n';
        txt += rellenar('Número de pedido : ' + datosPago.numeroPedido, W) + '\n';
        txt += rellenar('Fecha y hora     : ' + datosPago.fecha + ' (hora Colombia)', W) + '\n';
        txt += sep + '\n';
        txt += rellenar('DATOS DE ENVÍO', W) + '\n';
        txt += lin + '\n';
        txt += rellenar('Dirección  : ' + datosPago.direccion, W) + '\n';
        txt += rellenar('Teléfono   : ' + datosPago.telefono, W) + '\n';
        txt += sep + '\n';
        txt += rellenar('PRODUCTOS COMPRADOS', W) + '\n';
        txt += lin + '\n';

        cartItems.forEach(function (item) {
            var nombre = item.nombre.length > 40 ? item.nombre.substring(0, 37) + '...' : item.nombre;
            txt += rellenar(nombre, W) + '\n';
            txt += rellenar('  Cant.: ' + item.cantidad + '   |   Precio: ' + formatearCOP(item.precio), W) + '\n';
            txt += rellenar('  Subtotal  : ' + formatearCOP(item.subtotal), W) + '\n';
            txt += lin + '\n';
        });

        var metodoStr = datosPago.metodoPago === 'NEQUI' ? 'Transferencia Nequi' : 'Contraentrega';
        txt += sep + '\n';
        txt += rellenar('MÉTODO DE PAGO : ' + metodoStr, W) + '\n';
        txt += sep + '\n';
        txt += rellenar('TOTAL A PAGAR  : ' + formatearCOP(cartTotal), W) + '\n';
        txt += sep + '\n';
        txt += centrarPad('Gracias por tu compra. ¡Hasta pronto!', W) + '\n';
        txt += sep + '\n';

        return txt;
    }

    function confirmarPedido() {
        const itemsPayload = cartItems.map(function (item) {
            return {
                id: item.id,
                nombre: item.nombre,
                precio: item.precio,
                cantidad: item.cantidad
            };
        });

        const body = {
            numeroPedido: datosPago.numeroPedido,
            fecha: datosPago.fecha,
            direccion: datosPago.direccion,
            telefono: datosPago.telefono,
            metodoPago: datosPago.metodoPago,
            items: itemsPayload,
            total: cartTotal
        };

        fetch('/pedido/confirmar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        })
            .then(function (response) {
                if (!response.ok) throw new Error('Respuesta no OK');
                return response.json();
            })
            .then(function (data) {
                if (data.estado === 'OK') {
                    // Pedido exitoso
                    var mensajeEl = document.getElementById('pagoMensajeEstado');
                    mensajeEl.className = 'pago-mensaje-exito';
                    mensajeEl.textContent = '¡Pedido confirmado! Tu número de pedido es ' + data.numeroPedido + '.';
                    mensajeEl.style.display = 'block';

                    // Actualizar badge del carrito a 0
                    var badge = document.querySelector('.cart-badge');
                    if (badge) badge.textContent = '0';

                    // Reemplazar botón descargar por botón cerrar
                    var btnDescargar = document.getElementById('btnDescargarFactura');
                    var btnCerrar = document.getElementById('btnCerrarFinal');
                    if (btnDescargar) btnDescargar.style.display = 'none';
                    if (btnCerrar) btnCerrar.style.display = 'inline-flex';
                } else {
                    throw new Error('Estado no OK');
                }
            })
            .catch(function () {
                var mensajeEl = document.getElementById('pagoMensajeEstado');
                mensajeEl.className = 'pago-mensaje-error';
                mensajeEl.textContent = 'Hubo un problema al confirmar tu pedido. Por favor intenta de nuevo.';
                mensajeEl.style.display = 'block';
            });
    }

    // ── Utilidades de formato de texto ─────────────────

    function centrar(texto, ancho) {
        if (texto.length >= ancho) return texto;
        var pad = Math.floor((ancho - texto.length) / 2);
        return ' '.repeat(pad) + texto;
    }

    function centrarPad(texto, ancho) {
        if (texto.length >= ancho) return texto.substring(0, ancho);
        var pad = Math.floor((ancho - texto.length) / 2);
        var result = ' '.repeat(pad) + texto;
        return result + ' '.repeat(Math.max(0, ancho - result.length));
    }

    function rellenar(texto, ancho) {
        if (texto.length >= ancho) return texto.substring(0, ancho);
        return texto + ' '.repeat(ancho - texto.length);
    }

    function truncar(texto, max) {
        if (!texto) return '';
        if (texto.length <= max) return texto;
        return texto.substring(0, max - 3) + '...';
    }

})();
