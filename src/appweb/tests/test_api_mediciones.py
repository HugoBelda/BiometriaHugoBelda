# appweb/tests/test_api_ultima_fastapi.py
from fastapi.testclient import TestClient
from appweb.main import app

client = TestClient(app)

def test_api_ultima():
    response = client.get("/api/ultima")
    assert response.status_code == 200
    data = response.json()
    assert "id" in data or "error" in data  # puede no haber mediciones
    if "id" in data:
        assert "tipo" in data
        assert "valor" in data
