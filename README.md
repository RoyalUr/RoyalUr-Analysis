# 🎲 The Royal Game of Ur Analysis
This repository is dedicated to the technical analysis of
[The Royal Game of Ur](https://en.wikipedia.org/wiki/Royal_Game_of_Ur).

The Royal Game of Ur is an ancient Sumerian board game discovered by Sir Leonard Woolley in a
tomb in ancient Ur in 1922. The game dates back to **over 4000 years ago**, and is considered
by many to be the oldest board game in the world. This repository contains code with the goal
of analysing this old game, and determining how much of the game is luck, and how much is skill.

**Learn the rules** at https://royalur.net/rules, or **play the game** at https://royalur.net.


# 🏆 Goals
**Ultimate Goal:** Determine the best possible strategy to use to win at The Royal Game of Ur.

**Side Goal:** Gain new insights into the strategy of the game.

# 📈 Current Rankings

There are currently only a few agents implemented to play the game, but regardless
here are there current rankings:

| Agent | Win Percentage |
| ----- | -------------- |
| [Expectimax](src/main/java/com/sothatsit/royalur/ai/ExpectimaxAgent.java) (Depth 5)  | 87% |
| [Expectimax](src/main/java/com/sothatsit/royalur/ai/ExpectimaxAgent.java) (Depth 3)  | 76% |
| [Greedy](src/main/java/com/sothatsit/royalur/ai/GreedyAgent.java)                    | 60% |
| [Last-Move](src/main/java/com/sothatsit/royalur/ai/LastMoveAgent.java)               | 51% |
| [Random](src/main/java/com/sothatsit/royalur/ai/RandomAgent.java)                    | 23% |
| [First-Move](src/main/java/com/sothatsit/royalur/ai/FirstMoveAgent.java)             | 0%  |

These results may not show us the best way to play the game yet, but they definitely
tell us the worst. If you want to lose every game you play, there is no better
strategy than always playing the first possible move you can!


# 📝 License
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
