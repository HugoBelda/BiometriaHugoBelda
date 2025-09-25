# 🌡️ Proyecto Biometría: Arduino + Android + FastAPI

**Un sistema IoT para capturar, procesar y visualizar datos biométricos en tiempo real.**

<p align="center">
  <img src="https://img.shields.io/badge/Arduino-00979D?style=flat&logo=arduino&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/FastAPI-009688?style=flat&logo=fastapi&logoColor=white" />
  <img src="https://img.shields.io/badge/SQLite-003B57?style=flat&logo=sqlite&logoColor=white" />
</p>

---

## 📝 Descripción

Sistema IoT que integra un **Arduino** para capturar mediciones de sensores, enviadas vía **Bluetooth Low Energy (BLE Beacon)** a un dispositivo **Android**. La app Android actúa como cliente, reenviando los datos por **HTTP** a una **API REST** desarrollada con **FastAPI**. Los datos se almacenan en una base de datos **SQLite** y se visualizan en una interfaz web dinámica creada con **HTML+JavaScript**.

---

## 🚀 Flujo del Sistema

1. **Arduino**: Captura datos del sensor y los envía vía Bluetooth (BLE).
2. **Android**: Recibe datos por BLE y los envía a la API mediante HTTP.
3. **FastAPI**: Procesa las solicitudes y almacena los datos en SQLite.
4. **SQLite**: Almacena las mediciones de forma persistente.
5. **Web**: Muestra los datos en una interfaz HTML+JS.

**Diagrama del flujo**:
Arduino (Sensor) → Android (BLE → HTTP) → API REST (FastAPI) → SQLite → Web (HTML+JS)

## 📂 Estructura del Proyecto
proyecto/
├── 📁 arduino/         # Código para el beacon BLE en Arduino
├── 📁 android/         # Aplicación Android (Cliente REST)
├── 📁 api/             # Servidor FastAPI
│   ├── main.py         # Punto de entrada de la API
│   ├── 📁 routes/      # Rutas de la API
│   ├── 📁 logic/       # Lógica de negocio
│   ├── 📁 database/    # Configuración de SQLite
│   └── 📁 static/      # Archivos web (HTML, JS, CSS)
└── 📜 README.md        # Documentación del proyecto

## 🌐 Endpoints de la API

### 1. Guardar Medición
- **Método**: `POST`
- **Ruta**: `/rest/guardar`
- **Cuerpo** (JSON):
  ```json
  {
    "valor": 25.3,
    "tipo": "temperatura"
  }

Descripción: Registra una nueva medición en la base de datos.

2. Obtener Última Medición

Método: GET
Ruta: /rest/ultima
Respuesta (JSON):
json{
  "id": 12,
  "valor": 24.5,
  "tipo": "temperatura"
}

Descripción: Devuelve la medición más reciente almacenada.


⚙️ Ejecución

Navega al directorio de la API:
bashcd api

Instala las dependencias:
bashpip install fastapi uvicorn

Inicia el servidor:
bashuvicorn main:app --reload

Accede a los recursos:

📚 Documentación de la API: http://localhost:8000/docs
🌐 Interfaz web: http://localhost:8000/static/index.html




🛠️ Tecnologías Utilizadas

Arduino: Programación del microcontrolador y comunicación BLE.
Android: App para recepción de datos BLE y envío HTTP.
FastAPI: Framework para la creación de la API REST.
SQLite: Base de datos ligera para almacenamiento.
HTML+JS: Interfaz web para visualización de datos.
