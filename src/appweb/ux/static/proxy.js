// =================================================
// Archivo: proxy.js
// Descripción: Función genérica para invocar endpoints REST
// Autor: Hugo Belda Revert
// Fecha creación: 30/09/2025
// Última modificación: 30/09/2025
// =================================================

// ---------------------------------------------------
// llamar(nombreFuncion, parametrosLlamada, cb)
//
// nombreFuncion -> string con la URL/endpoint
// parametrosLlamada -> objeto con los datos a enviar
// cb -> callback(err, resultado)
// ---------------------------------------------------
function llamar(nombreFuncion, parametrosLlamada, cb) {
    var xmlhttp = new XMLHttpRequest();
  
    xmlhttp.onreadystatechange = function () {
      if (this.readyState === 4 && this.status === 200) {
        let resultado = this.responseText;
  
        try {
          resultado = JSON.parse(resultado);
        } catch (error) {
          console.log("Respuesta no es JSON, devuelvo texto plano");
        }
  
        if (resultado.error) {
          cb(resultado.error, null);
        } else {
          cb(null, resultado);
        }
      } else if (this.readyState === 4 && this.status !== 200) {
        cb("Error HTTP " + this.status + ": " + this.statusText, null);
      }
    };
  
    xmlhttp.onerror = function () {
      cb("Error de red al conectar con el servidor", null);
    };
  
    // Decidimos GET o POST según si hay parámetros
    if (parametrosLlamada && Object.keys(parametrosLlamada).length > 0) {
      xmlhttp.open("POST", nombreFuncion, true);
      xmlhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
      xmlhttp.send(JSON.stringify(parametrosLlamada));
    } else {
      xmlhttp.open("GET", nombreFuncion, true);
      xmlhttp.send();
    }
  }
  