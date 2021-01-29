# Kotlin line

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Kotlin line is a web-application for the "Jagged line" game. 
The player's task in this game is to construct a polyline from start to end.
Each segment should be a knight's move, and the segments can not intersect.
The goal is to create as long polyline as possible.

<img width="200" alt="Screenshot 2021-01-28 at 22 43 28" src="https://user-images.githubusercontent.com/42250320/106190231-69192900-61ba-11eb-9b19-5f4458b22a4f.png">

# Installation

Dependencies:

- JDK 15

You need to compile the "frontend" module by running:

`./gradlew browserDistribution`

You have to do it before the first run.

To run the server, execute this command:

`./gradlew run`

You can specify the port by modifying `backend/main/resources/application.conf`.
In addition, you can adjust the list of available fields by modifying `backend/main/resources/fields.json`

The database will be stored in `backend/database`