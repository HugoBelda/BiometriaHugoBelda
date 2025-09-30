import os
from fastapi import FastAPI
from fastapi.staticfiles import StaticFiles
from fastapi.middleware.cors import CORSMiddleware
from appweb.api import mediciones

app = FastAPI()

# Configuración de CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://127.0.0.1:8000", "http://localhost:8000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Incluir router de la API
app.include_router(mediciones.router)

# Carpeta base del proyecto
BASE_DIR = os.path.dirname(__file__)

# Servir HTML
html_path = os.path.join(BASE_DIR, "ux", "templates")
print("html_path:", html_path)
print("Files in html_path:", os.listdir(html_path) if os.path.exists(html_path) else "Directory does not exist")

# Servir JS/CSS/otros estáticos
js_path = os.path.join(BASE_DIR, "ux", "static")
print("js_path:", js_path)
print("Files in js_path:", os.listdir(js_path) if os.path.exists(js_path) else "Directory does not exist")
app.mount("/static", StaticFiles(directory=js_path, html=False), name="static")

# Servir HTML
app.mount("/", StaticFiles(directory=html_path, html=True), name="templates")