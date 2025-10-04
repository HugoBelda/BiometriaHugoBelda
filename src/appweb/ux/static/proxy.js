// =================================================
// Archivo: proxy.js
// Descripción: Función genérica para invocar endpoints REST
// Autor: Hugo Belda Revert
// Fecha creación: 30/09/2025
// =================================================

// -------------------------------------------------
// llamar() -> Promise
// Descripción:
//     Realiza petición HTTP GET o POST y devuelve un Promise
// Entrada:
//     nombreFuncion: String -> URL o endpoint
//     parametrosLlamada: Object -> parámetros opcionales
// Salida:
//     Promise que resuelve con el resultado del servidor o rechaza con error
// -------------------------------------------------
function llamar(nombreFuncion, parametrosLlamada) {
  return new Promise((resolve, reject) => {
    var xmlhttp = new XMLHttpRequest();

    // Se ejecuta cada vez que cambia el estado de la petición
    xmlhttp.onreadystatechange = function () {
      if (this.readyState === 4 && this.status === 200) {
        let resultado = this.responseText; // obtenemos el texto de respuesta

        try {
          // Intentamos parsear la respuesta a JSON
          resultado = JSON.parse(resultado);
        } catch (error) {
          // Si no es JSON, lo dejamos como texto plano
          console.log("Respuesta no es JSON, devuelvo texto plano");
        }

        // Si el servidor devuelve un objeto con error, rechazamos el Promise
        if (resultado.error) {
          reject(resultado.error);
        } else {
          // Si todo está bien, resolvemos el Promise con la respuesta
          resolve(resultado);
        }

      } else if (this.readyState === 4 && this.status !== 200) {
        // Si la petición terminó pero no fue 200, rechazamos con error HTTP
        reject("Error HTTP " + this.status + ": " + this.statusText);
      }
    };
    xmlhttp.onerror = function () {
      reject("Error de red al conectar con el servidor");
    };

    // Decidimos si hacer GET o POST según si hay parámetros
    if (parametrosLlamada && Object.keys(parametrosLlamada).length > 0) {
      // POST → enviamos datos JSON
      xmlhttp.open("POST", nombreFuncion, true);
      xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      xmlhttp.send(JSON.stringify(parametrosLlamada));
    } else {
      // GET → sin enviar datos
      xmlhttp.open("GET", nombreFuncion, true);
      xmlhttp.send();
    }
  });
}
