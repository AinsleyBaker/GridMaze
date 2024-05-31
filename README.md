# GridMaze Game
A wasteland strategy game that features a dynamic 10x10 grid board and various interactive game objects, including Gold, Medical Units, Mutants and traps.

The game can be played either in the terminal or GUI, the terminal involves directly inputting move values to move the player around the grid, with the game displaying a printed map and stats. The player starts with a time limit of 100 steps and 10 health. If either of these values reach zero before reaching the finish, the player loses. To win, the player must reach the top-right finish door. The player's score is determined by the collected treasure and remaining steps.

**Features**
- Mutant Movement: After each player move, mutants also move or stay in place, increasing the game's difficulty.
- Save and Load: Players can save and load game states through serialization, allowing for gameplay continuation at any time.
- High Score System: The game tracks and displays the top 5 scores, achieved by saving and reading from a text file.
- Real-time Feedback: The game provides real-time feedback by displaying messages after each game event, keeping players informed and engaged.
