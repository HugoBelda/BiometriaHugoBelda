document.addEventListener("DOMContentLoaded", function () {
    ultimaMedicion(function (err, res) {
      const cont = document.getElementById("contenido");
      if (err) {
        cont.innerHTML = `<p>Error: ${err}</p>`;
      } else {
        cont.innerHTML = `
          <p><b>ID:</b> ${res.id}</p>
          <p><b>Tipo:</b> ${res.tipo}</p>
          <p><b>Valor:</b> ${res.valor}</p>
        `;
      }
    });
  });