# =================================================
# Archivo: mediciones.py
# Descripción: Endpoints REST para acceder a las
#              mediciones a través de FastAPI.
# Autor: Hugo Belda Revert
# Fecha creación: 19/09/2025
# Última modificación: 30/09/2025
# =================================================

from fastapi import APIRouter
from pydantic import BaseModel
from fastapi.middleware.cors import CORSMiddleware

router = APIRouter(prefix="/api", tags=["mediciones"])

class Medicion(BaseModel):
    valor: float
    tipo: str

@router.get("/ultima")
def get_ultima():
    pass

@router.post("/guardar")
def post_guardar():
    pass