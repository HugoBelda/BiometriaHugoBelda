##🌡️ Proyecto Biometría: Arduino + Android + FastAPI

Un sistema IoT para capturar, procesar y visualizar datos biométricos en tiempo real.
📝 Descripción
Sistema IoT que integra un Arduino para capturar mediciones de sensores, enviadas vía Bluetooth Low Energy (BLE Beacon) a un dispositivo Android. La app Android actúa como cliente, reenviando los datos por HTTP a una API REST desarrollada con FastAPI. Los datos se almacenan en una base de datos SQLite y se visualizan en una interfaz web dinámica creada con HTML+JavaScript.
🚀 Flujo del Sistema

Arduino: Captura datos del sensor y los envía vía Bluetooth (BLE).
Android: Recibe datos por BLE y los envía a la API mediante HTTP.
FastAPI: Procesa las solicitudes y almacena los datos en SQLite.
SQLite: Almacena las mediciones de forma persistente.
Web: Muestra los datos en una interfaz HTML+JS.

Diagrama del flujo:
Arduino (Sensor) → Android (BLE → HTTP) → API REST (FastAPI) → SQLite → Web (HTML+JS)

📂 Estructura del Proyecto
proyecto/
├── 📁 docs/            # PDFs con documentación de android, arduino y web
├── 📁 src/             # Código fuente del proyecto
│   ├── 📁 android/     # Aplicación Android (Cliente REST)
│   ├── 📁 arduino/     # Código para el beacon BLE en Arduino
│   └── 📁 web/         # Servidor FastAPI y frontend web
└── 📜 README.md        # Documentación del proyecto

🌐 Endpoints de la API
1. Guardar Medición

Método: POST
Ruta: /rest/guardar
Cuerpo (JSON):{
  "valor": 25.3,
  "tipo": "temperatura"
}

Descripción: Registra una nueva medición en la base de datos.

2. Obtener Última Medición

Método: GET
Ruta: /rest/ultima
Respuesta (JSON):{
  "id": 12,
  "valor": 24.5,
  "tipo": "temperatura"
}

Descripción: Devuelve la medición más reciente almacenada.


🛠️ Tecnologías Utilizadas

Arduino: Programación del microcontrolador y comunicación BLE.
Android: App para recepción de datos BLE y envío HTTP.
FastAPI: Framework para la creación de la API REST.
SQLite: Base de datos ligera para almacenamiento.
HTML+JS: Interfaz web para visualización de datos.
