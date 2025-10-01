package com.example.hbelrev.receptorbeaconshugo;
import android.util.Log;

public class LogicaFake {

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