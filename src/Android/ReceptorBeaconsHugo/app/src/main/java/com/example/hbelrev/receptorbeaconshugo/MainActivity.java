package com.example.hbelrev.receptorbeaconshugo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView elTexto;
    private TextView datosEnviados; // TextView para mostrar datos enviados
    private Button elBotonEnviar;
    private boolean datoEnviado = false;
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private static final String ETIQUETA_LOG = ">>>>";
    private static final int CODIGO_PETICION_PERMISOS = 11223344;
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private BluetoothLeScanner elEscanner;
    private ScanCallback callbackDelEscaneo = null;
    private LogicaFake logicaFake;
    private static final String URL_SERVIDOR = "http://192.168.1.103:8000/api/guardar";  // URL del API
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private void mostrarInformacionDispositivoBTLE( ScanResult resultado ) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        Log.d(ETIQUETA_LOG, " ****************************************************");
        Log.d(ETIQUETA_LOG, " ****** DISPOSITIVO DETECTADO BTLE ****************** ");
        Log.d(ETIQUETA_LOG, " ****************************************************");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d(ETIQUETA_LOG, " nombre = " + bluetoothDevice.getName());
        Log.d(ETIQUETA_LOG, " toString = " + bluetoothDevice.toString());
        Log.d(ETIQUETA_LOG, " dirección = " + bluetoothDevice.getAddress());
        Log.d(ETIQUETA_LOG, " rssi = " + rssi );
        Log.d(ETIQUETA_LOG, " bytes = " + new String(bytes));
        Log.d(ETIQUETA_LOG, " bytes (" + bytes.length + ") = " + Utilidades.bytesToHexString(bytes));

        TramaIBeacon tib = new TramaIBeacon(bytes);

        Log.d(ETIQUETA_LOG, " ----------------------------------------------------");
        Log.d(ETIQUETA_LOG, " prefijo  = " + Utilidades.bytesToHexString(tib.getPrefijo()));
        Log.d(ETIQUETA_LOG, "          advFlags = " + Utilidades.bytesToHexString(tib.getAdvFlags()));
        Log.d(ETIQUETA_LOG, "          advHeader = " + Utilidades.bytesToHexString(tib.getAdvHeader()));
        Log.d(ETIQUETA_LOG, "          companyID = " + Utilidades.bytesToHexString(tib.getCompanyID()));
        Log.d(ETIQUETA_LOG, "          iBeacon type = " + Integer.toHexString(tib.getiBeaconType()));
        Log.d(ETIQUETA_LOG, "          iBeacon length 0x = " + Integer.toHexString(tib.getiBeaconLength()) + " ( "
                + tib.getiBeaconLength() + " ) ");
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToHexString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " uuid  = " + Utilidades.bytesToString(tib.getUUID()));
        Log.d(ETIQUETA_LOG, " major  = " + Utilidades.bytesToHexString(tib.getMajor()) + "( "
                + Utilidades.bytesToInt(tib.getMajor()) + " ) ");
        Log.d(ETIQUETA_LOG, " minor  = " + Utilidades.bytesToHexString(tib.getMinor()) + "( "
                + Utilidades.bytesToInt(tib.getMinor()) + " ) ");
        Log.d(ETIQUETA_LOG, " txPower  = " + Integer.toHexString(tib.getTxPower()) + " ( " + tib.getTxPower() + " )");
        Log.d(ETIQUETA_LOG, " ****************************************************");

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    /**
     * Procesa la información de un Beacon,
     * extrae sus datos (UUID, Major, Minor, RSSI), interpreta el tipo de medición
     * y envía la información a otra parte del sistema.
     */
    private void mostrarYEnviarInformacionDispositivosBTLE(ScanResult resultado) {

        BluetoothDevice bluetoothDevice = resultado.getDevice();
        byte[] bytes = resultado.getScanRecord().getBytes();
        int rssi = resultado.getRssi();

        // Creamos un objeto TramaIBeacon para interpretar los datos de la trama
        TramaIBeacon tib = new TramaIBeacon(bytes);

        // Convertimos los campos del Beacon
        String uuid = Utilidades.bytesToString(tib.getUUID());
        int major = Utilidades.bytesToInt(tib.getMajor());
        float minor = Utilidades.bytesToInt(tib.getMinor());

        // Descomponemos el campo 'major' en dos bytes:
        int id = (major >> 8) & 0xFF;      // Extrae los bits altos → ID
        int contador = major & 0xFF;       // Extrae los bits bajos → contador

        String tipoMedicion;
        if (id == 12) {
            tipoMedicion = "Temperatura";
        } else if (id == 11) {
            tipoMedicion = "CO2";
        } else {
            tipoMedicion = "Desconocido";
        }

        Log.d(ETIQUETA_LOG, "UUID=" + uuid
                + " tipo=" + tipoMedicion
                + " contador=" + contador
                + " valor=" + minor
                + " rssi=" + rssi);

        // Enviamos los datos
        enviarDatos(uuid, tipoMedicion, minor, rssi);
    }

    // -------------------------------------------------
    // -------------------------------------------------
    /**
     * Construye un objeto JSON con los datos del sensor,
     * actualiza el TextView para mostrar lo enviado,
     * y llama a LogicaFake.guardarMedicion() para simular o realizar
     * el envío de los datos al servidor.
     */
    private void enviarDatos(String uuid, String tipo, float valor, int rssi) {

        // Formateamos un texto con la información que se va a enviar
        String datos = String.format(
                "Datos enviados:\nTipo: %s\nValor: %.2f",
                tipo, valor
        );

        this.datosEnviados.setText(datos);

        // Construimos el cuerpo JSON con los datos de medición
        // que se enviarán al servidor o sistema de almacenamiento.
        String jsonBody = "{"
                + "\"tipo\":\"" + tipo + "\","
                + "\"valor\":" + valor
                + "}";

        // Llamamos al método que realiza el envío de datos
        logicaFake.guardarMedicion(jsonBody, URL_SERVIDOR);

        this.elTexto.setText("Envío en progreso...");
    }

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    /**
     * Inicia la búsqueda de un dispositivo BTLE específico,
     * configurando filtros, ajustes de escaneo y callbacks para procesar los resultados.
     */
    private void buscarEsteDispositivoBTLE(final String dispositivoBuscado) {
        Log.d(ETIQUETA_LOG, "buscarEsteDispositivoBTLE(): empieza");

        // Bloqueamos el botón de envío mientras se realiza el escaneo
        this.elBotonEnviar.setEnabled(false);

        // Definimos el callback que manejará los resultados del escaneo
        this.callbackDelEscaneo = new ScanCallback() {

            @Override
            public void onScanResult(int callbackType, ScanResult resultado) {
                super.onScanResult(callbackType, resultado);

                // Evitamos enviar datos múltiples veces por el mismo escaneo
                if (!datoEnviado) {
                    datoEnviado = true;
                    mostrarYEnviarInformacionDispositivosBTLE(resultado);
                    detenerBusquedaDispositivosBTLE();
                    elBotonEnviar.setEnabled(true);
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, "buscarEsteDispositivoBTLE(): onScanFailed() " + errorCode);
                elBotonEnviar.setEnabled(true);
            }
        };

        // Creamos una lista de filtros para buscar solo el dispositivo deseado por nombre
        List<ScanFilter> filtros = new ArrayList<>();
        filtros.add(new ScanFilter.Builder()
                .setDeviceName(dispositivoBuscado)
                .build());

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

        // Verificamos permisos BLUETOOTH_SCAN (necesarios desde Android 12 - API 31)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED &&
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            Log.d(ETIQUETA_LOG, "Permisos BLUETOOTH_SCAN no concedidos");
            return;
        }

        this.elEscanner.startScan(filtros, settings, callbackDelEscaneo);

        Log.d(ETIQUETA_LOG, "buscarEsteDispositivoBTLE(): escaneando dispositivo -> " + dispositivoBuscado);
    }

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private void detenerBusquedaDispositivosBTLE() {

        if ( this.callbackDelEscaneo == null ) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.elEscanner.stopScan( this.callbackDelEscaneo );
        this.callbackDelEscaneo = null;

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonDetenerBusquedaDispositivosBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton detener busqueda dispositivos BTLE Pulsado" );
        this.detenerBusquedaDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private void inicializarBlueTooth() {
        Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): revisando permisos");

        String[] permisos;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            // Android 12 o superior
            permisos = new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
            };
        } else {
            // Android 10 y 11
            permisos = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            };
        }

        boolean permisosConcedidos = true;
        for (String p : permisos) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                permisosConcedidos = false;
                break;
            }
        }

        if (!permisosConcedidos) {
            Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): solicitando permisos");
            ActivityCompat.requestPermissions(this, permisos, CODIGO_PETICION_PERMISOS);
            return;
        }

        // Si llegamos aquí, tenemos permisos
        Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): permisos concedidos, inicializando BT");
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
        if (bta == null) {
            Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): dispositivo sin BT");
            return;
        }

        if (!bta.isEnabled()) {
            bta.enable();
            Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): habilitando BT...");
        }

        this.elEscanner = bta.getBluetoothLeScanner();
        if (this.elEscanner == null) {
            Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): no se pudo obtener escáner BTLE");
        } else {
            Log.d(ETIQUETA_LOG, "inicializarBlueTooth(): escáner BTLE listo");
        }
    }

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void boton_enviar_pulsado(View quien) {
        datoEnviado = false; // reiniciar flag
        Log.d(ETIQUETA_LOG, "boton_enviar_pulsado: iniciando escaneo de dispositivo real");
        this.elTexto.setText("Buscando dispositivo...");
        buscarEsteDispositivoBTLE("HugoBelda");
    }

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(ETIQUETA_LOG, " onCreate(): empieza ");

        this.logicaFake = new LogicaFake();

        inicializarBlueTooth();

        this.elTexto = (TextView) findViewById(R.id.elTexto);
        this.datosEnviados = (TextView) findViewById(R.id.datosEnviados); // Inicializar nuevo TextView
        this.elBotonEnviar = (Button) findViewById(R.id.botonEnviar);

        // Para iniciar escaneo automáticamente, descomenta:
        // buscarEsteDispositivoBTLE("GTI-3A");

        Log.d(ETIQUETA_LOG, " onCreate(): termina ");
    } // onCreate()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults);

        switch (requestCode) {
            case CODIGO_PETICION_PERMISOS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): permisos concedidos  !!!!");
                    inicializarBlueTooth();
                    // Para iniciar escaneo automáticamente, descomenta:
                    // buscarEsteDispositivoBTLE("GTI-3A");
                }  else {

                    Log.d(ETIQUETA_LOG, " onRequestPermissionResult(): Socorro: permisos NO concedidos  !!!!");

                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    // --------------------------------------------------------------
    private void buscarTodosLosDispositivosBTLE() {
        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empieza ");

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): instalamos scan callback ");

        this.callbackDelEscaneo = new ScanCallback() {
            @Override
            public void onScanResult( int callbackType, ScanResult resultado ) {
                super.onScanResult(callbackType, resultado);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanResult() ");

                mostrarInformacionDispositivoBTLE( resultado );
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onBatchScanResults() ");

            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): onScanFailed() ");

            }
        };

        Log.d(ETIQUETA_LOG, " buscarTodosLosDispositivosBTL(): empezamos a escanear ");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.elEscanner.startScan( this.callbackDelEscaneo);

    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonBuscarDispositivosBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton buscar dispositivos BTLE Pulsado" );
        this.buscarTodosLosDispositivosBTLE();
    } // ()

    // --------------------------------------------------------------
    // --------------------------------------------------------------
    public void botonBuscarNuestroDispositivoBTLEPulsado( View v ) {
        Log.d(ETIQUETA_LOG, " boton nuestro dispositivo BTLE Pulsado" );
        //this.buscarEsteDispositivoBTLE( Utilidades.stringToUUID( "EPSG-GTI-PROY-3A" ) );
        //this.buscarEsteDispositivoBTLE( "EPSG-GTI-PROY-3A" );
        this.buscarEsteDispositivoBTLE( "HugoBelda" );

    } // ()

       /* public void boton_enviar_pulsado (View quien) {
        Log.d(ETIQUETA_LOG, "boton_enviar_pulsado: simulando envío REST con JSON fake");
        this.elTexto.setText("Enviando simulado...");

        // Simular datos
        String uuid = "1234-5678-9012-3456";
        String tipo = "Temperatura";
        int valor = 25;
        int rssi = -60;

        enviarDatos(uuid, tipo, valor, rssi);
    } // ()*/
} // class
// --------------------------------------------------------------
// --------------------------------------------------------------


