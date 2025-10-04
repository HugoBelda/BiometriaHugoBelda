package com.example.hbelrev.receptorbeaconshugo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Clase para realizar peticiones HTTP REST de manera asíncrona en Android.
 * Extiende AsyncTask para ejecutar la petición en un hilo separado y devolver
 * la respuesta mediante un callback.
 */
public class PeticionarioREST extends AsyncTask<Void, Void, Boolean> {

    /**
     * Interfaz para recibir la respuesta de la petición REST.
     * Se llama al finalizar la ejecución en el hilo principal.
     */
    public interface RespuestaREST {
        void callback(int codigo, String cuerpo);
    }

    // --------------------------------------------------------------------
    // Campos internos de la clase
    // --------------------------------------------------------------------
    private String elMetodo;
    private String urlDestino;
    private String elCuerpo = null;
    private RespuestaREST laRespuesta;

    private int codigoRespuesta;
    private String cuerpoRespuesta = "";

    // --------------------------------------------------------------------
    // Constructor
    // --------------------------------------------------------------------
    public PeticionarioREST() {
        Log.d("clienterestandroid", "constructor()");
    }

    //Configura y ejecuta la petición REST de manera asíncrona.
    public void hacerPeticionREST(String metodo, String urlDestino, String cuerpo, RespuestaREST laRespuesta) {
        this.elMetodo = metodo;
        this.urlDestino = urlDestino;
        this.elCuerpo = cuerpo;
        this.laRespuesta = laRespuesta;

        this.execute(); // Ejecuta doInBackground() en otro hilo
    }

    // --------------------------------------------------------------------
    // Método principal ejecutado en segundo plano
    // --------------------------------------------------------------------
    @Override
    protected Boolean doInBackground(Void... params) {
        Log.d("clienterestandroid", "doInBackground()");

        try {
            Log.d("clienterestandroid", "doInBackground() me conecto a >" + urlDestino + "<");

            // Creamos la conexión HTTP
            URL url = new URL(urlDestino);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json; charset-utf-8");
            connection.setRequestMethod(this.elMetodo);
            connection.setDoInput(true); // Permitimos leer la respuesta

            // Si no es GET y hay cuerpo, lo enviamos
            if (!this.elMetodo.equals("GET") && this.elCuerpo != null) {
                Log.d("clienterestandroid", "doInBackground(): no es GET, pongo cuerpo");
                connection.setDoOutput(true);
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.writeBytes(this.elCuerpo);
                dos.flush();
                dos.close();
            }

            Log.d("clienterestandroid", "doInBackground(): petición enviada");

            // Obtenemos el código y mensaje de respuesta
            int rc = connection.getResponseCode();
            String rm = connection.getResponseMessage();
            Log.d("clienterestandroid", "doInBackground() recibo respuesta = " + rc + " : " + rm);
            this.codigoRespuesta = rc;

            try {
                // Leemos el cuerpo de la respuesta si existe
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder acumulador = new StringBuilder();
                String linea;
                while ((linea = br.readLine()) != null) {
                    Log.d("clienterestandroid", linea);
                    acumulador.append(linea);
                }
                this.cuerpoRespuesta = acumulador.toString();
                Log.d("clienterestandroid", "cuerpo recibido=" + this.cuerpoRespuesta);

                connection.disconnect();

            } catch (IOException ex) {
                // Ocurre si no hay cuerpo en la respuesta
                Log.d("clienterestandroid", "doInBackground(): parece que no hay cuerpo en la respuesta");
            }

            return true; // Todo salió bien

        } catch (Exception ex) {
            Log.d("clienterestandroid", "doInBackground(): ocurrió una excepción: " + ex.getMessage());
        }

        return false; // Hubo un error
    }

    // --------------------------------------------------------------------
    // Método que se ejecuta en el hilo principal tras doInBackground()
    // --------------------------------------------------------------------
    @Override
    protected void onPostExecute(Boolean comoFue) {
        Log.d("clienterestandroid", "onPostExecute() comoFue = " + comoFue);

        // Llamamos al callback con el código HTTP y el cuerpo recibido
        if (this.laRespuesta != null) {
            this.laRespuesta.callback(this.codigoRespuesta, this.cuerpoRespuesta);
        }
    }
}
