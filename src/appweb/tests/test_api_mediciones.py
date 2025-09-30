import pytest
import asyncio
from httpx import AsyncClient
from appweb.main import app

@pytest.mark.asyncio
async def test_api_ultima():
    async with AsyncClient(base_url="http://127.0.0.1:8000") as client:
        response = await client.get("http://127.0.0.1:8000/api/ultima")
    assert response.status_code == 200
    data = response.json()
    assert "id" in data
    assert "tipo" in data
    assert "valor" in data
