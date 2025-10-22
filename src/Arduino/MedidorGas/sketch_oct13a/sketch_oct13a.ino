// -*- mode: c++ -*-
// ======================================================
// Programa: Medidor de CO2/O3 con ULPSM-O3 (968-046)
// ======================================================
#include <bluefruit.h>
// ------------------------------------------------------
// Configuración de pines y constantes
// ------------------------------------------------------
#define PIN_GAS 28       // Pin analógico del sensor de gas (Vgas)
#define PIN_VREF 29      // Pin analógico de referencia (Vref)

const float SENSIBILIDAD_SENSOR = -44.75;  // nA/ppm (dato del fabricante)
const int RESOLUCION_ADC = 4096;           // ADC de 12 bits (0–4095)
const float GAIN_TIA = 499.0;              // Ganancia del amplificador (Ω)
const float TENSION_REF_MAX = 3.3;         // Voltaje máximo del ADC (V)


// ------------------------------------------------------
// Variables globales
// ------------------------------------------------------
float voltOzono = 0.0;       // Voltaje leído del sensor
float voltRef = 0.0;         // Voltaje de referencia
float concentracion = 0.0;   // Concentración calculada (ppm)


// ------------------------------------------------------
// Setup: se ejecuta una vez al iniciar
// ------------------------------------------------------
void setup() {
  Serial.begin(9600);        
  pinMode(PIN_GAS, INPUT);    
  pinMode(PIN_VREF, INPUT);   

  Serial.println("========================================");
  Serial.println("Iniciando medición de CO2/O3 (ULPSM-O3)");
  Serial.println("Esperando estabilización del sensor (~60 s)...");
  Serial.println("========================================");
  delay(2000); // pequeña pausa inicial
}


// ------------------------------------------------------
// Loop: se ejecuta continuamente
// ------------------------------------------------------
void loop() {
  int lecturaOzono = analogRead(PIN_GAS);   // Lectura cruda del canal de gas
  int lecturaVRef  = analogRead(PIN_VREF);  // Lectura cruda del canal de referencia

  Serial.print("Lectura ADC - Vgas: ");
  Serial.print(lecturaOzono);
  Serial.print(" | Vref: ");
  Serial.println(lecturaVRef);


  voltOzono = (lecturaOzono * TENSION_REF_MAX) / RESOLUCION_ADC;
  voltRef   = (lecturaVRef  * TENSION_REF_MAX) / RESOLUCION_ADC;

  // Mostrar los voltajes convertidos
  Serial.print("Voltajes - Vgas: ");
  Serial.print(voltOzono, 4);
  Serial.print(" V | Vref: ");
  Serial.print(voltRef, 4);
  Serial.println(" V");


  float deltaV = voltOzono - voltRef;

  Serial.print("Diferencia ΔV = ");
  Serial.print(deltaV * 1000, 3);
  Serial.println(" mV");


  concentracion = (deltaV / (GAIN_TIA * SENSIBILIDAD_SENSOR)) * 1e6;

  Serial.print("Concentración estimada: ");
  Serial.print(concentracion, 4);
  Serial.println(" ppm");

  Serial.println("----------------------------------------");

  delay(1000); // Espera 1 segundo entre mediciones
}
