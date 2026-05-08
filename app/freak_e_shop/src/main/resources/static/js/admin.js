// ── Lógica CRUD del Panel de Administración ────────────
document.addEventListener('DOMContentLoaded', () => {
    loadProductsTable();

    // Vincular envío de formulario
    const form = document.getElementById('productForm');
    if (form) {
        form.addEventListener('submit', async function (e) {
            e.preventDefault();

            const btn = document.getElementById('saveProductBtn');
            const originalText = btn.innerHTML;
            btn.innerHTML = '<i class="bi bi-hourglass-split"></i> Guardando...';
            btn.disabled = true;

            const prodId = document.getElementById('prodId').value;
            const productData = {
                name: document.getElementById('prodName').value,
                category: document.getElementById('prodCategory').value,
                price: document.getElementById('prodPrice').value,
                stock: parseInt(document.getElementById('prodStock').value),
                img: document.getElementById('prodImage').value,
                description: document.getElementById('prodDescription').value
            };

            try {
                if (prodId) {
                    await updateProduct(parseInt(prodId), productData);
                } else {
                    await createProduct(productData);
                }

                const modal = bootstrap.Modal.getInstance(document.getElementById('productModal'));
                if (modal) modal.hide();

                await loadProductsTable();

            } catch (error) {
                alert('Error al guardar el producto: ' + error.message);
            } finally {
                btn.innerHTML = originalText;
                btn.disabled = false;
            }
        });
    }
});

// Carga productos y renderiza la tabla.
async function loadProductsTable() {
    const tbody = document.getElementById('adminProductList');
    if (!tbody) return;

    tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4">Cargando productos...</td></tr>';

    try {
        const products = await fetchProducts();

        if (products.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center py-4 text-muted">No hay productos en el catálogo.</td></tr>';
            return;
        }

        tbody.innerHTML = products.map(p => `
            <tr>
                <td>
                    <img src="${p.img}" alt="${escapeHtml(p.name)}" class="rounded" style="width: 50px; height: 50px; object-fit: cover;">
                </td>
                <td>
                    <div class="font-weight-bold">${escapeHtml(p.name)}</div>
                    <small class="text-muted d-block text-truncate" style="max-width: 200px;" title="${escapeHtml(p.description)}">${escapeHtml(p.description)}</small>
                </td>
                <td><span class="badge bg-secondary">${escapeHtml(p.category)}</span></td>
                <td class="text-accent fw-bold">${escapeHtml(p.price)}</td>
                <td>${p.stock} un.</td>
                <td class="text-end">
                    <button class="btn btn-sm btn-outline-light me-1" onclick="openProductForm(${p.id})" title="Editar">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger" onclick="handleDeleteProduct(${p.id})" title="Eliminar">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-danger py-4">Error al cargar el catálogo.</td></tr>';
    }
}

// Abre el modal de producto para añadir (si el id es nulo) o editar.
async function openProductForm(id = null) {
    const form = document.getElementById('productForm');
    form.reset();
    document.getElementById('prodId').value = '';
    document.getElementById('productModalLabel').textContent = 'Añadir Nuevo Producto';

    if (id !== null) {
        try {
            document.getElementById('productModalLabel').textContent = 'Editar Producto';
            const products = await fetchProducts();
            const product = products.find(p => p.id === id);

            if (product) {
                document.getElementById('prodId').value = product.id;
                document.getElementById('prodName').value = product.name;
                document.getElementById('prodCategory').value = product.category;
                document.getElementById('prodPrice').value = product.price;
                document.getElementById('prodStock').value = product.stock;
                document.getElementById('prodImage').value = product.img;
                document.getElementById('prodDescription').value = product.description;

                // Mostrar explícitamente el modal para edición
                const modalEl = document.getElementById('productModal');
                let modal = bootstrap.Modal.getInstance(modalEl);
                if (!modal) modal = new bootstrap.Modal(modalEl);
                modal.show();
            }
        } catch (error) {
            console.error('Error al obtener los detalles del producto:', error);
        }
    } else {
        // Mostrar modal para añadir nuevo producto
        const modalEl = document.getElementById('productModal');
        let modal = bootstrap.Modal.getInstance(modalEl);
        if (!modal) modal = new bootstrap.Modal(modalEl);
        modal.show();
    }
}

// Maneja la eliminación con confirmación.
async function handleDeleteProduct(id) {
    if (confirm('¿Estás seguro de que deseas eliminar este producto? Esta acción no se puede deshacer.')) {
        try {
            await deleteProduct(id);
            await loadProductsTable();
        } catch (error) {
            alert('Error al eliminar el producto: ' + error.message);
        }
    }
}

// ── API Simulada de Base de Datos ────────────────────
// Estas funciones simulan llamadas asíncronas a una 
// API para una base de datos en el backend.

async function createProduct(productData) {
    return new Promise(async (resolve, reject) => {
        setTimeout(async () => {
            try {
                const products = await fetchProducts();
                // Generar un ID pseudo-aleatorio
                const newId = products.length > 0 ? Math.max(...products.map(p => p.id)) + 1 : 1;
                const newProduct = { ...productData, id: newId };
                products.push(newProduct);
                localStorage.setItem('stride_db_products', JSON.stringify(products));
                resolve(newProduct);
            } catch (error) {
                reject(error);
            }
        }, 500); // 500ms de retraso para simular latencia de red
    });
}

async function updateProduct(id, productData) {
    return new Promise(async (resolve, reject) => {
        setTimeout(async () => {
            try {
                const products = await fetchProducts();
                const index = products.findIndex(p => p.id === id);
                if (index !== -1) {
                    products[index] = { ...products[index], ...productData };
                    localStorage.setItem('stride_db_products', JSON.stringify(products));
                    resolve(products[index]);
                } else {
                    reject(new Error('Producto no encontrado.'));
                }
            } catch (error) {
                reject(error);
            }
        }, 500);
    });
}

async function deleteProduct(id) {
    return new Promise(async (resolve, reject) => {
        setTimeout(async () => {
            try {
                let products = await fetchProducts();
                const initialLength = products.length;
                products = products.filter(p => p.id !== id);

                if (products.length < initialLength) {
                    localStorage.setItem('stride_db_products', JSON.stringify(products));
                    resolve(true);
                } else {
                    reject(new Error('Producto no encontrado.'));
                }
            } catch (error) {
                reject(error);
            }
        }, 500);
    });
}
