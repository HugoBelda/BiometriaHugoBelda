// =================================================
// Archivo: scripts.js
// Autor: Hugo Belda Revert
// Fecha creación: 30/09/2025
// =================================================

// -------------------------------------------------
// DOMContentLoaded -> obtiene y muestra la última medición
// -------------------------------------------------
document.addEventListener("DOMContentLoaded", async function () {
  const cont = document.getElementById("contenido");

  try {
      const res = await ultimaMedicion(); // ya tenemos la medida
      cont.innerHTML = `
          <div class="medicion-card">
              <h3>Última Medición</h3>
              <p><b>ID:</b> ${res.id}</p>
              <p><b>Tipo:</b> ${res.tipo}</p>
              <p><b>Valor:</b> ${res.valor}</p>
          </div>
      `;
  } catch (err) {
      cont.innerHTML = `<p class="error">Error: ${err}</p>`;
  }
});
