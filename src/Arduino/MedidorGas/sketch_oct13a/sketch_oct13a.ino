// -*-c++-*-

// --------------------------------------------------------------
//
// Hugo Belda Revert
// 13-10-2025
//
// --------------------------------------------------------------
#include <DGS.h>
#include <SoftwareSerial.h>

// Definicion de los pines UART
#define RX_PIN 15
#define TX_PIN 17

//////////////////////////////////////////////////////////////////////////
// Recibe una cadena del sensor digital en forma de secuencia ASCII.
//////////////////////////////////////////////////////////////////////////
int sensorData [11];
void Serial1InParser(void)
{
    int i = 0;
    for (int i =0; i<11; i++) {
       while(!Serial1.available()) { }
       sensorData[i] = Serial1.parseInt();
    }
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);   // Serial is the USB serial port
  Serial1.begin(9600);  // Puerto serie hardware para comunicación con el sensor, 8 bit, no parity, 1 stop bit, 3.3V
}

void loop() {
  // put your main code here, to run repeatedly:
}
