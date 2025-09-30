// =================================================
// Archivo: ultimaMedicion.js
// Descripción: Proxy lógico para /api/ultima
// Autor: Hugo Belda Revert
// Fecha creación: 30/09/2025
// Última modificación: 30/09/2025
// =================================================

// ---------------------------------------------------
// ultimaMedicion(cb)
// Llama al endpoint /api/ultima
// ---------------------------------------------------
function ultimaMedicion(cb) {
    llamar("/api/ultima", {}, cb);
  }
  