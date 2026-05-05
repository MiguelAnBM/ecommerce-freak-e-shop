// ==========================================
// CART FUNCTIONALITY (Local Storage Based)
// ==========================================

let strideCart = JSON.parse(localStorage.getItem('strideCart')) || [];

document.addEventListener('DOMContentLoaded', function () {
    updateCartBadge();
    if (document.getElementById('cartItemsList')) {
        renderCartItems();
    }

    // ==========================================
    // Scroll Fade-In Animation
    // ==========================================
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

    // ==========================================
    // Navbar scroll effect
    // ==========================================
    const navbar = document.querySelector('.stride-navbar');
    if (navbar) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 50) navbar.classList.add('scrolled');
            else navbar.classList.remove('scrolled');
        });
    }

    // ==========================================
    // Quick View Modal
    // ==========================================
    const productPreviewModal = document.getElementById('productPreviewModal');
    if (productPreviewModal) {
        const bsModal = new bootstrap.Modal(productPreviewModal);

        document.querySelectorAll('.product-card').forEach(card => {
            card.addEventListener('click', function (e) {
                if (e.target.closest('button') || e.target.closest('.product-colors')) return;

                const name = this.querySelector('.product-name').textContent;
                const price = this.querySelector('.current-price').textContent;
                const img = this.querySelector('.product-image-wrapper img').src;
                const category = this.querySelector('.product-category').textContent;

                document.getElementById('modalProductTitle').textContent = name;
                document.getElementById('modalProductPrice').textContent = price;
                document.getElementById('modalProductImg').src = img;
                document.getElementById('modalProductCategory').textContent = category;

                // Reset selection
                document.querySelectorAll('.color-option, .size-option').forEach(el => el.classList.remove('active'));
                if (document.querySelector('.color-option')) document.querySelector('.color-option').classList.add('active');
                if (document.querySelector('.size-option')) document.querySelector('.size-option').classList.add('active');

                bsModal.show();
            });
        });

        // Add to cart from Modal
        const modalAddBtn = document.getElementById('modalAddToCartBtn');
        if (modalAddBtn) {
            modalAddBtn.onclick = function () {
                const product = {
                    id: 'prod_' + Date.now(),
                    name: document.getElementById('modalProductTitle').textContent,
                    price: parseFloat(document.getElementById('modalProductPrice').textContent.replace('$', '')),
                    img: document.getElementById('modalProductImg').src,
                    variant: 'Size: ' + (document.querySelector('.size-option.active')?.textContent || 'M') + ' | Color: ' + (document.getElementById('modalSelectedColor')?.textContent || 'Default')
                };
                addToCart(product);

                // Visual feedback in modal
                const originalContent = modalAddBtn.innerHTML;
                modalAddBtn.innerHTML = '<i class="bi bi-check-lg"></i> Added to Cart!';
                modalAddBtn.classList.add('btn-added-success'); // Custom style for green

                setTimeout(() => {
                    bsModal.hide();
                    // Reset button after modal is gone
                    setTimeout(() => {
                        modalAddBtn.innerHTML = originalContent;
                        modalAddBtn.classList.remove('btn-added-success');
                    }, 500);
                }, 800);
            };
        }

        // Color/Size selection in modal
        document.querySelectorAll('.color-option, .size-option').forEach(el => {
            el.addEventListener('click', function () {
                const siblings = this.parentElement.children;
                for (let sibling of siblings) sibling.classList.remove('active');
                this.classList.add('active');
                if (this.classList.contains('color-option')) {
                    const colorDisplay = document.getElementById('modalSelectedColor');
                    if (colorDisplay) colorDisplay.textContent = this.getAttribute('title') || 'Selected';
                }
            });
        });
    }

    // Theme icon update (class is now added in <head> to prevent flashing)
    const savedTheme = localStorage.getItem('stride-theme');
    if (savedTheme === 'light') {
        const icon = document.getElementById('themeIcon');
        if (icon) { icon.classList.remove('bi-sun'); icon.classList.add('bi-moon-stars'); }
    }
});

// ==========================================
// Core Cart Functions
// ==========================================

// Global listener for all Add to Cart buttons
document.addEventListener('click', function (e) {
    const btn = e.target.closest('.btn-add-to-cart');
    if (btn) {
        e.preventDefault();
        e.stopPropagation();
        handleAddToCart(btn);
    }
});

function handleAddToCart(btn) {
    const card = btn.closest('.product-card');
    if (!card) return;

    const product = {
        id: 'prod_' + Date.now(),
        name: card.querySelector('.product-name').textContent,
        price: parseFloat(card.querySelector('.current-price').textContent.replace('$', '')),
        img: card.querySelector('.product-image-wrapper img').src,
        variant: 'Standard Edition'
    };
    addToCart(product);

    // Visual feedback
    const originalContent = btn.innerHTML;
    btn.innerHTML = '<i class="bi bi-check-lg"></i> Added!';
    btn.classList.add('btn-added-success');

    // Add a bounce effect class if desired
    btn.style.transform = 'scale(1.05)';

    setTimeout(() => {
        btn.innerHTML = originalContent;
        btn.classList.remove('btn-added-success');
        btn.style.transform = '';
    }, 2000);
}

function addToCart(product) {
    const existing = strideCart.find(item => item.name === product.name && item.variant === product.variant);
    if (existing) {
        existing.qty++;
    } else {
        product.qty = 1;
        strideCart.push(product);
    }
    saveCart();
    updateCartBadge();
    showToast(`Added ${product.name} to cart`);
}

function saveCart() {
    localStorage.setItem('strideCart', JSON.stringify(strideCart));
}

function updateCartBadge() {
    const count = strideCart.reduce((acc, item) => acc + item.qty, 0);
    document.querySelectorAll('.cart-badge').forEach(b => {
        b.textContent = count;
        b.style.display = count > 0 ? 'flex' : 'none';
    });
}

function showToast(msg) {
    // Simple alert for now, or could be a pretty toast
    console.log('Toast:', msg);
}

// ==========================================
// Cart Page UI Rendering
// ==========================================

function renderCartItems() {
    const container = document.getElementById('cartItemsList');
    if (!container) return;

    if (strideCart.length === 0) {
        showEmptyCartMsg();
        return;
    }

    container.innerHTML = '';
    strideCart.forEach((item, index) => {
        const itemHtml = `
            <div class="cart-item" id="item-${index}">
                <div class="cart-product">
                    <div class="cart-product-image">
                        <img src="${item.img}" alt="${item.name}"/>
                    </div>
                    <div>
                        <div class="cart-product-name">${item.name}</div>
                        <div class="cart-product-variant">${item.variant}</div>
                    </div>
                </div>
                <div class="cart-price">
                    <span class="current">$${item.price}</span>
                </div>
                <div>
                    <div class="qty-control">
                        <button class="qty-btn" onclick="updateItemQty(${index}, -1)"><i class="bi bi-dash"></i></button>
                        <span class="qty-value">${item.qty}</span>
                        <button class="qty-btn" onclick="updateItemQty(${index}, 1)"><i class="bi bi-plus"></i></button>
                    </div>
                </div>
                <div class="cart-total">$${(item.price * item.qty).toFixed(0)}</div>
                <button class="cart-remove" onclick="removeCartItem(${index})"><i class="bi bi-x-lg"></i></button>
            </div>
        `;
        container.innerHTML += itemHtml;
    });

    updateOrderSummary();
}

function updateItemQty(index, delta) {
    strideCart[index].qty += delta;
    if (strideCart[index].qty < 1) strideCart[index].qty = 1;
    saveCart();
    renderCartItems();
    updateCartBadge();
}

function removeCartItem(index) {
    strideCart.splice(index, 1);
    saveCart();
    renderCartItems();
    updateCartBadge();
}

function clearCart() {
    strideCart = [];
    saveCart();
    renderCartItems();
    updateCartBadge();
}

function updateOrderSummary() {
    const subtotal = strideCart.reduce((acc, item) => acc + (item.price * item.qty), 0);
    const tax = Math.round(subtotal * 0.08);
    const shipping = subtotal > 75 || subtotal === 0 ? 0 : 9.99;
    const total = subtotal + tax + shipping;

    const els = {
        sub: document.getElementById('summarySubtotal'),
        tax: document.getElementById('summaryTax'),
        ship: document.getElementById('summaryShipping'),
        total: document.getElementById('summaryTotal'),
        msg: document.getElementById('freeShippingMsg')
    };

    if (els.sub) els.sub.textContent = '$' + subtotal;
    if (els.tax) els.tax.textContent = '$' + tax;
    if (els.ship) {
        els.ship.textContent = shipping === 0 ? 'FREE' : '$' + shipping.toFixed(2);
        els.ship.className = shipping === 0 ? 'value free' : 'value';
    }
    if (els.total) els.total.textContent = '$' + total;
    if (els.msg) els.msg.style.display = (subtotal > 75) ? 'block' : 'none';
}

function showEmptyCartMsg() {
    const container = document.getElementById('cartItemsList');
    container.innerHTML = `
        <div class="text-center py-5">
            <i class="bi bi-bag-x" style="font-size:4rem;color:var(--stride-text-muted);"></i>
            <h3 class="mt-3">¡Tu carrito está vacío! </h3>
            <p class="text-muted">Explora nuestra colección y encuentra algo genial en Frik-E Shop</p>
            <a href="catalog.html" class="btn-stride btn-stride-primary mt-3">Start Shopping</a>
        </div>
    `;
    const actions = document.querySelector('.cart-actions');
    if (actions) actions.style.display = 'none';
    updateOrderSummary();
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

function applyPromo() {
    const input = document.getElementById('promoInput');
    if (input && input.value.toUpperCase() === 'STRIDE20') {
        alert('Promo code STRIDE20 applied! (UI Simulation)');
    } else {
        alert('Invalid promo code');
    }
}

function proceedToCheckout() {
    alert('Proceeding to checkout... (Simulation)');
}
