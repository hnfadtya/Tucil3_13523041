# Tucil3_13523041
Tugas Kecil IF2211 Strategi Algoritma

<h1 align="center"> Tugas Kecil 3 IF2211 Strategi Algoritma </h1>
<h1 align="center">  Penyelesaian Permainan Rush Hour Menggunakan Algoritma UCS, Greedy Best First Search, dan A* </h1>


## General Information
Rush Hour is a grid-based logic puzzle game that challenges players to slide vehicles around a grid (usually 6x6) so that the main car (usually red) can escape a traffic jam through an exit on the side of the board. Each vehicle can only move in a straight line forward or backward according to its orientation (horizontal or vertical), and cannot rotate. The main objective of the game is to move the red car to the exit in the least number of moves possible. The essential components of the Rush Hour game are:
1. Board
The board is where the game is played. The board consists of cells, which are singular points on the board. A piece will occupy cells on the board. When the game starts, all pieces have been placed on the board with a certain configuration in the form of piece location and orientation, either horizontal or vertical. Only primary pieces can be moved out of the board through the exit. Pieces that are not primary pieces cannot be moved out of the board. The board has one exit that must be on the board wall and aligned with the orientation of the primary piece.
2. Piece
A piece is a vehicle on the board. Each piece has a position, size, and orientation. The orientation of a piece can only be horizontal or vertical - it cannot be diagonal. Pieces can have various sizes, namely the number of cells occupied by the piece. By default, the size variations of a piece are 2-piece (occupying 2 cells) or 3-piece (occupying 3 cells). A piece cannot be moved past/through another piece.
3. Primary Piece
The primary piece is the main vehicle that must be removed from the board (usually red). There can only be one primary piece.
4. Exit
The exit is where the primary piece can be moved out to complete the game
5. Movement
The movement in question is the shifting of pieces in the game. Pieces can only move/shift straight according to their orientation (up-down if vertical and left-right if horizontal). A piece cannot be moved past/through another piece

## Contributors
|   NIM    |               Nama               |
| :------: | :------------------------------: |
| 13522041 |       Hanif Kalyana Aditya       |


## Features
Features that used in this program are:
| NO  | Algorithm   | Description                                                                       |
|:---:|-------------|-----------------------------------------------------------------------------------|
| 1   | UCS         | RushHour searching the most optimum path using Uniform Cost Search algorithm    |
| 2   | GreedyBFS   | RushHour searching the path using Greedy Best First Search algorithm              |
| 3   | A*          | RushHour searching the path using A* algorithm                                    |


## Requirements Program
|   NO   |  Required Program                  |                           Reference Link                            |
| :----: | ---------------------------------- |---------------------------------------------------------------------|
|   1    | Java                               | [Java](https://www.java.com/en/download/)                           |                            
|   2    | Java Swing                         | -                                                                   |
|   3    | Java Kit                           | [Java Kit](https://www.oracle.com/java/technologies/downloads/)     |


## How to Run The Program
### CLI Based
1. Clone this repository by typing `git clone https://github.com/hnfadtya/Tucil3_13522041.git` in terminal.
2. Open the repository folder.
3. If you're using Windows, type `./run.bat` in terminal.
5. Input file name in test/ folder.
6. Choose the algorithm by typing `UCS` / `GBFS` / `A*`.
6. Choose the heuristic by typing `Manhattan Distance` / `Blocking Piece`.
5. Input file name for output.
7. Wait and the result will appear.


## Project Status
This project has been completed and can be executed.


## Project Structure
```bash

│
├── README.md
│
│  
├── bin                         # Executable files
│  
├── doc/                        # Document files
│    └── Tucil3_13522041.pdf
│
├── src/                        # Source code files
│    ├── model/
│    ├── parser/
│    ├── solver/
│    ├── heuristic/
│    └── Main.java 
│
├── test/                        # Testing files
│                     
└── run.bat                                  