# appweb/tests/test_crear_y_obtener_fastapi.py
from fastapi.testclient import TestClient
from appweb.main import app

client = TestClient(app)

def test_crear_y_obtener_medicion():
    payload = {"tipo": "temperatura", "valor": 22.7}

    # POST
    response = client.post("/api/guardar", json=payload)
    assert response.status_code == 200
    data = response.json()
    id_creado = data["id"]
    assert data["mensaje"] == "Medicion guardada correctamente"

    # GET
    response = client.get("/api/ultima")
    assert response.status_code == 200
    ultima = response.json()

    assert ultima["id"] == id_creado
    assert ultima["tipo"] == payload["tipo"]
    assert ultima["valor"] == payload["valor"]
