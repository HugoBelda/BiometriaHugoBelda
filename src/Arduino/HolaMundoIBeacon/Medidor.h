// -*- mode: c++ -*-

#ifndef MEDIDOR_H_INCLUIDO
#define MEDIDOR_H_INCLUIDO

// ------------------------------------------------------
// Clase Medidor
// ------------------------------------------------------
// Esta clase representa un dispositivo o módulo encargado
// de realizar mediciones de distintos tipos de sensores.
// ------------------------------------------------------
class Medidor {

  // .....................................................
  // .....................................................
private:

public:

  // .....................................................
  // constructor
  // .....................................................
  Medidor(  ) {
  } // ()

  // .....................................................
  // Método medirCO2()
  // .....................................................
  // Simula una medición de concentración de CO₂.
  // En un sistema real, leería los datos de un sensor.
  // Devuelve un valor entero.
  // .....................................................
  int medirCO2() {
    return 132; 
  } // ()

  // .....................................................
  // Método medirTemperatura()
  // .....................................................
  // Simula la lectura de la temperatura ambiente.
  // En un caso real, leería desde un sensor como el DHT22.
  // Devuelve la temperatura como un número entero (°C).
  // .....................................................
  int medirTemperatura() {
    return -11; 
  } // ()
	
}; // class Medidor


// ------------------------------------------------------
// ------------------------------------------------------
// ------------------------------------------------------
// ------------------------------------------------------
#endif
