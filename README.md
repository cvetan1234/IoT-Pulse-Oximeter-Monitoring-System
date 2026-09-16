# IoT Pulse Oximeter Monitoring System

## Overview

This project implements an **IoT-based pulse oximeter monitoring system** for measuring and visualizing heart rate and blood oxygen saturation (SpO₂).

The system combines an **ESP32**, a **MAX30100 pulse oximeter sensor**, **Node-RED**, **MQTT**, and an **Android application** to create an end-to-end monitoring pipeline:

```text
MAX30100 Sensor → ESP32 → Node-RED → MQTT → Android App → SQLite
```

The sensor measurements are acquired by the ESP32, processed and communicated through Node-RED and MQTT, and displayed in the Android application. Measurements can also be stored locally using SQLite.

This project was originally developed as part of the **Mobile & Ubiquitous Computing** module at Ostbayerische Technische Hochschule Amberg-Weiden (OTH Amberg-Weiden).

## Features

- Heart-rate measurement in BPM
- Blood oxygen saturation (SpO₂) measurement
- ESP32-based sensor integration
- MAX30100 pulse oximeter sensor
- Node-RED data processing
- MQTT-based communication
- Android application for real-time monitoring
- Heart-rate visualization
- Current BPM and SpO₂ display
- Start and stop controls
- Local measurement storage using SQLite
- Table view for stored measurements
- MQTT connection management
- Sound feedback based on heart-rate measurements

## System Architecture

### MAX30100 Sensor

The **MAX30100** pulse oximeter sensor provides heart-rate and blood-oxygen-saturation measurements.

### ESP32

The ESP32 interfaces with the sensor and reads the measured values. The embedded code is located in:

```text
esp32/EspCode.ino
```

### Node-RED

Node-RED forms part of the data-processing and communication layer. The supplied flow is located in:

```text
nodeRED/NodeRedFlow.json
```

### MQTT

MQTT provides the messaging layer used to transmit measurements to the Android application.

### Android Application

The Android application provides the user interface for the monitoring system. It displays BPM and SpO₂ measurements, visualizes heart-rate data, manages the MQTT connection, and provides controls for monitoring and stored measurements.

### SQLite

Measurements can be stored locally on the Android device using an **SQLite database**.

## Technologies

- ESP32
- MAX30100 pulse oximeter sensor
- Arduino / C++
- Node-RED
- MQTT
- Android
- Java
- SQLite
- LaTeX

## Project Structure

```text
IoT-Pulse-Oximeter-Monitoring-System/
├── esp32/
│   └── EspCode.ino
├── nodeRED/
│   └── NodeRedFlow.json
├── android/
│   └── ... Android Studio project
├── MAX30100lib/
│   └── ... MAX30100 sensor library
├── tex/
│   └── ... LaTeX documentation sources
└── Gruppe_20.pdf
```

## How to Run

This project consists of several connected components. To run the complete system, the ESP32/sensor, Node-RED, MQTT, and Android application must be configured to communicate with one another.

### 1. ESP32 and MAX30100

Connect the MAX30100 sensor to the ESP32 according to the hardware configuration used by the project.

Open:

```text
esp32/EspCode.ino
```

in the Arduino IDE or another compatible ESP32 development environment.

Make sure the required MAX30100 library is available. The project contains a copy of the sensor library in:

```text
MAX30100lib/
```

Compile the sketch and upload it to the ESP32.

### 2. Node-RED

Install and start Node-RED.

Import the supplied flow:

```text
nodeRED/NodeRedFlow.json
```

into the Node-RED editor. Verify the communication and MQTT configuration for your environment and deploy the flow.

### 3. MQTT

An MQTT broker is required for communication with the Android application.

Configure Node-RED and the Android application to connect to the same MQTT broker and use the expected topics. Network-specific addresses or settings may need to be changed when running the project in a different environment.

### 4. Android Application

Open the project contained in:

```text
android/
```

using **Android Studio**.

Allow Gradle to synchronize the project and install the required dependencies. Then connect an Android device with USB debugging enabled or start a compatible Android emulator and run the application.

Make sure its MQTT configuration corresponds to the broker used by the Node-RED setup.

### 5. Start Monitoring

Once all components are running, the complete data flow is:

```text
MAX30100 → ESP32 → Node-RED → MQTT → Android App
```

The Android application can then display incoming heart-rate and SpO₂ measurements and store measurements locally.

## Documentation

The repository includes the original project documentation:

```text
Gruppe_20.pdf
```

The corresponding LaTeX sources are contained in:

```text
tex/
```

## Notes

The complete system depends on physical hardware and network configuration. An ESP32 with a compatible MAX30100 sensor is required to reproduce the complete measurement pipeline.

MQTT broker addresses and other environment-specific connection settings may need to be adjusted when the project is run on another computer or network.

## About

This project demonstrates an end-to-end **Internet of Things (IoT)** application combining embedded sensor acquisition, messaging, data processing, mobile visualization, and local data persistence.

## Authors

**Maria Lyoteva, Tsvetan Stanchev**  
Ostbayerische Technische Hochschule Amberg-Weiden (OTH Amberg-Weiden)
