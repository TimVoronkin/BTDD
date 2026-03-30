/**
 * Cinema Background Floating Items v2
 * - No distortion (fixed aspect ratio)
 * - Individual mouse parallax (each item follows/escapes mouse at its own speed)
 * - Individual scroll parallax
 * - Brighter visibility
 */
(function () {
    const ITEMS_COUNT = 50;          // total icons to scatter
    const IMAGES = 18;          // item-01 … item-18
    const SIZE_MIN = 30;          // px
    const SIZE_MAX = 65;          // px

    // ── 1. Create container ──────────────────────────────────────
    const bg = document.createElement('div');
    bg.id = 'cinema-bg';
    document.body.prepend(bg);

    // ── 2. Spawn items ───────────────────────────────────────────
    const itemsData = [];

    for (let i = 0; i < ITEMS_COUNT; i++) {
        const img = document.createElement('img');

        const idx = Math.floor(Math.random() * IMAGES) + 1;
        const num = String(idx).padStart(2, '0');
        img.src = `/imgs/items/item-${num}.png`;
        img.alt = '';

        const x = Math.random() * 105 - 2.5;      // % slightly outside bounds
        const y = Math.random() * 105 - 2.5;
        const rot = (Math.random() - 0.5) * 60;     // -30 … +30 deg
        const size = SIZE_MIN + Math.random() * (SIZE_MAX - SIZE_MIN);
        const speed = 0.05 + Math.random() * 0.2;     // speed factor for parallax

        img.style.cssText = `
            left: ${x}%;
            top:  ${y}%;
            width: ${size}px;
            height: auto;
            transform: rotate(${rot}deg);
        `;

        bg.appendChild(img);

        itemsData.push({
            el: img,
            baseRot: rot,
            speed: speed,
            currentX: 0,
            currentY: 0,
            targetX: 0,
            targetY: 0
        });
    }

    // ── 3. Interaction Logic ─────────────────────────────────────
    let scrollY = window.scrollY;
    let mouseX = 0;
    let mouseY = 0;

    window.addEventListener('scroll', () => {
        scrollY = window.scrollY;
    }, { passive: true });

    window.addEventListener('mousemove', (e) => {
        // Offset from center of screen
        mouseX = e.clientX - window.innerWidth / 2;
        mouseY = e.clientY - window.innerHeight / 2;
    }, { passive: true });

    // ── 4. Unified Animation Loop ────────────────────────────────
    function update() {
        itemsData.forEach(item => {
            // Mouse Parallax: each item has its own sensitivity based on its speed
            // Items move opposite to mouse (simulating depth)
            const targetMX = -mouseX * item.speed * 0.5;
            const targetMY = -mouseY * item.speed * 0.5;

            // Scroll Parallax
            const scrollOffset = scrollY * item.speed;

            // Smooth interpolation (easing)
            item.currentX += (targetMX - item.currentX) * 0.1;
            item.currentY += (targetMY - item.currentY) * 0.1;

            // Apply final transform
            const totalY = item.currentY + scrollOffset;
            item.el.style.transform = `translate(${item.currentX}px, ${totalY}px) rotate(${item.baseRot}deg)`;
        });

        requestAnimationFrame(update);
    }

    requestAnimationFrame(update);

})();
