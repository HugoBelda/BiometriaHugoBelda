package com.example.hbelrev.receptorbeaconshugo;

import android.util.Log;

/**
 * =================================================
 * Clase: LogicaFake
 * Autor: Hugo Belda Revert
 * =================================================
 */
public class LogicaFake {

    // -------------------------------------------------
    //
    // guardarMedicion(jsonMedida, urlServidor) -> void
    //
    // Entrada:
    //     jsonMedida: String -> medición en formato JSON
    //     urlServidor: String -> URL del servidor al que enviar la medición
    //
    // Salida: ninguna
    // Efecto:
    //     Hace una petición HTTP POST al servidor usando PeticionarioREST.
    //
    // -------------------------------------------------
    public void guardarMedicion(String jsonMedida, String urlServidor) {
        PeticionarioREST elPeticionario = new PeticionarioREST();

        elPeticionario.hacerPeticionREST("POST", urlServidor, jsonMedida,
                new PeticionarioREST.RespuestaREST() {
                    @Override
                    public void callback(int codigo, String cuerpo) {
                        if (codigo >= 200 && codigo < 300) {
                            Log.d("LogicaFake", "Éxito: codigo=" + codigo + ", cuerpo=" + cuerpo);
                        } else {
                            Log.e("LogicaFake", "Error: codigo=" + codigo + ", cuerpo=" + cuerpo);
                        }
                    }
                });
    }
}
