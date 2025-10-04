// =================================================
// Archivo: ultimaMedicion.js
// Descripción: Proxy lógico para /api/ultima
// Autor: Hugo Belda Revert
// Fecha creación: 30/09/2025
// Última modificación: 30/09/2025
// =================================================

// -------------------------------------------------
// ultimaMedicion() -> Promise
// Descripción:
//     Devuelve la última medición desde el servidor
// Salida:
//     Promise que resuelve con la medida
// -------------------------------------------------
function ultimaMedicion() {
  return llamar("/api/ultima", {});
}
