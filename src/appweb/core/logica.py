# =================================================
# Archivo: mediciones.py
# Descripción: Funciones de lógica para manejar
#              la tabla 'mediciones' en SQLite.
# Autor: Hugo Belda Revert
# Fecha creación: 30/09/2025
# Última modificación: 30/09/2025
# =================================================

import sqlite3
import os

DB_PATH = os.path.join(os.path.dirname(__file__),
                       "..", "database", "sensores.db")

# -------------------------------------------------
#
# ultima_medicion() -> devuelve la última medición
#
# Entrada: ninguna
# Salida: diccionario con:
#     id: Entero -> ID de la medición
#     valor: Float -> valor de la medición
#     tipo: Texto -> tipo de medición
#     error: Texto -> en caso de que no haya mediciones
#
# -------------------------------------------------
def ultima_medicion():
    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()
    cursor.execute(
        "SELECT id, valor, tipo FROM mediciones ORDER BY id DESC LIMIT 1")
    row = cursor.fetchone()
    conn.close()

    if row:
        return {"id": row[0], "valor": row[1], "tipo": row[2]}
    return {"error": "no hay mediciones"}


# -------------------------------------------------
#
# guardar_medicion(valor: float, tipo: str) -> int
#
# Entrada:
#     valor: Float -> valor de la medición
#     tipo: Texto -> tipo de la medición
#
# Salida:
#     Entero -> ID de la nueva medición insertada
#
# -------------------------------------------------
def guardar_medicion(valor: float, tipo: str):
    conn = sqlite3.connect(DB_PATH)
    cursor = conn.cursor()
    cursor.execute(
        "INSERT INTO mediciones (valor, tipo) VALUES (?, ?)", (valor, tipo)
    )
    nueva_id = cursor.lastrowid
    conn.commit()
    conn.close()
    return nueva_id
