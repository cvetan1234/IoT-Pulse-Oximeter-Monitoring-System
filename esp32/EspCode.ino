#include <Wire.h>
#include "MAX30100_PulseOximeter.h"

PulseOximeter pox;
unsigned long startTime = 0;
unsigned long elapsedTime = 0;
unsigned long heartRateSum = 0;
unsigned long spo2Sum = 0;
unsigned long measurementsNumber = 0;
bool pulseDetected = false;

void setup() {
  Serial.begin(9600);
  Serial.println("Initializing pulse oximeter..");

  Wire.begin(32, 33);  // Initialize I2C with custom pins (SDA to GPIO 33, SCL to GPIO 32)

  if (!pox.begin()) {
    Serial.println("FAILED to initialize pulse oximeter");
    while (1);  // Hang indefinitely if initialization fails
  } else {
    Serial.println("Pulse oximeter initialized successfully");
  }

  pox.setOnBeatDetectedCallback(onBeatDetected);
}

void loop() {
  pox.update();  // Update pulse oximeter readings

  if (pulseDetected) {
    if (startTime == 0) {
      startTime = millis(); // Start timing for 1-second interval
    }

    elapsedTime = millis() - startTime;

    if (elapsedTime < 1000) { // If 1 second has not passed
      // Accumulate heart rate and SpO2 readings
      heartRateSum += pox.getHeartRate();
      spo2Sum += pox.getSpO2();
      measurementsNumber += 1;
    } else { // If 1 second has not passed
      // Print average heart rate and SpO2 over the last second
      Serial.print(heartRateSum / measurementsNumber);
      Serial.print(",");
      Serial.println(spo2Sum / measurementsNumber);

      // Reset variables for the next 1-second interval
      startTime = 0;
      elapsedTime = 0;
      heartRateSum = 0;
      spo2Sum = 0;
      measurementsNumber = 0;
      pulseDetected = false;
    }
  }
}