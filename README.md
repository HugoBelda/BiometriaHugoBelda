# 🌐 Proyecto biometría: Arduino + Android + FastAPI

## 📌 Descripción
Sistema IoT donde un **Arduino** envía mediciones vía **Bluetooth (Beacon BLE)** a un **móvil Android**, que las reenvía por **HTTP** a una **API REST (FastAPI)**.  
Los datos se guardan en **SQLite** y se muestran en una **web HTML+JS**.

---

## 🚀 Flujo
Arduino (sensor) → Android (BLE → HTTP) → API REST (FastAPI) → SQLite → Web

---

## 📂 Estructura
proyecto/
├── arduino/ # Código beacon BLE
├── android/ # Cliente Android (REST)
├── api/ # Servidor FastAPI
│ ├── main.py
│ ├── routes/
│ ├── logic/
│ ├── database/
│ └── static/ # Web cliente
└── README.md

---

## 🌐 Endpoints
### Guardar medición
`POST /rest/guardar`
```json
{ "valor": 25.3, "tipo": "temperatura" }
Última medición
GET /rest/ultima
{ "id": 12, "valor": 24.5, "tipo": "temperatura" }
⚙️ Ejecución
cd api
pip install fastapi uvicorn
uvicorn main:app --reload
API docs: http://localhost:8000/docs
Web: http://localhost:8000/static/index.html
