# =================================================
# Archivo: mediciones.py
# Descripción: Endpoints REST para acceder a las
#              mediciones a través de FastAPI.
# Autor: Hugo Belda Revert
# Fecha creación: 30/09/2025
# Última modificación: 30/09/2025
# =================================================

from fastapi import APIRouter
from pydantic import BaseModel
from appweb.core.logica import ultima_medicion, guardar_medicion
from fastapi.middleware.cors import CORSMiddleware

router = APIRouter(prefix="/api", tags=["mediciones"])

class Medicion(BaseModel):
    valor: float
    tipo: str

# -------------------------------------------------
#
# get_ultima() -> endpoint API
#
# Entrada: ninguna (GET /api/ultima)
# Salida: JSON con:
#     id: Entero -> ID de la medición
#     valor: Float -> valor de la medición
#     tipo: Texto -> tipo de medición
#     error: Texto -> en caso de que no haya mediciones
#
# -------------------------------------------------
@router.get("/ultima")
def get_ultima():
    return ultima_medicion()

# -------------------------------------------------
#
# post_guardar(medicion: Medicion) -> endpoint API
#
# Entrada:
#     medicion: JSON con:
#         valor: Float -> valor de la medición
#         tipo: Texto -> tipo de la medición
#
# Salida:
#     JSON con:
#         mensaje: Texto -> confirmación de guardado
#         id: Entero -> ID de la nueva medición
#
# -------------------------------------------------
@router.post("/guardar")
def post_guardar(medicion: Medicion):
    nueva_id = guardar_medicion(medicion.valor, medicion.tipo)
    return {"mensaje": "Medicion guardada correctamente", "id": nueva_id}
