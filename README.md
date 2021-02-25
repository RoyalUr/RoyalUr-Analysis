# 🎲 The Royal Game of Ur Analysis
This repository is dedicated to the technical analysis of
[The Royal Game of Ur](https://en.wikipedia.org/wiki/Royal_Game_of_Ur).

The Royal Game of Ur is an ancient Sumerian board game discovered by Sir Leonard Woolley in a
tomb in ancient Ur in 1922. The game dates back to **over 4000 years ago**, and is considered
by many to be the oldest board game in the world. This repository contains code with the goal
of analysing this old game, and determining how much of the game is luck, and how much is skill.

**Learn the rules** at https://royalur.net/rules, or **play the game** at https://royalur.net.


### Join the community!
If you're interested in The Royal Game of Ur, we have a [Discord](https://discord.gg/Ea49VVru5N)
and a [Reddit](https://www.reddit.com/r/GameofUr/) that you might want to check out! These are
both good places to talk about the game, its strategies, and see cool board recreations that
people have made! The Discord is also a great place to find strong opponents to challenge!

<p float="left">
  <a href="https://discord.gg/Ea49VVru5N">
    <img src="https://royalur.net/res/discord.svg" height="64" valign="middle" />
  </a>
  <a href="https://www.reddit.com/r/GameofUr/">
    <img src="https://royalur.net/res/reddit.svg" height="64" valign="middle" />
  </a>
  <a href="https://royalur.net">
    <img src="https://royalur.net/favicon.png" height="64" valign="middle" />
  </a>
</p>


# 🏆 Goals
**Ultimate Goal:** Determine the best possible strategy to use to win at The Royal Game of Ur.

**Side Goal:** Gain new insights into the strategy of the game.


# 🤖 The Agents
We currently have a small set of agents that are designed to play The Royal Game of Ur.

**Here they are!**

| Agent       | Description |
| ----------- | -------------- |
| Random      | Picks a random move, every time! |
| First-Move  | Picks the least advanced piece to move. |
| Last-Move   | Picks the most advanced piece to move. |
| Greedy      | Prioritises taking pieces, and moving onto rosettes. |
| Expectimax  | Does a statistical analysis to determine the best move. |

### Want more detail about the strategies?
If you'd like to delve deeper into each agent and how they function,
check out [Agents.md](docs/Agents.md) for more information!


# 📈 Current Rankings
There are currently only a few agents implemented to play the game, but regardless
here are there current rankings:

| Agent | Win Percentage |
| ----- | -------------- |
| [Expectimax](src/main/java/com/sothatsit/royalur/ai/ExpectimaxAgent.java) (Depth 5)  | 87.5% |
| [Expectimax](src/main/java/com/sothatsit/royalur/ai/ExpectimaxAgent.java) (Depth 3)  | 77.6% |
| [Greedy](src/main/java/com/sothatsit/royalur/ai/GreedyAgent.java)                    | 61.3% |
| [Last-Move](src/main/java/com/sothatsit/royalur/ai/LastMoveAgent.java)               | 50.0% |
| [Random](src/main/java/com/sothatsit/royalur/ai/RandomAgent.java)                    | 23.5% |
| [First-Move](src/main/java/com/sothatsit/royalur/ai/FirstMoveAgent.java)             | 0.1%  |

These results may not show us the best way to play the game yet, but they definitely
tell us the worst. If you want to lose every game you play, there is no better
strategy than always playing the first possible move you can!

### Want more detailed statistics?
If you'd like to delve deeper into each agent and more statistics about them,
check out [Agents.md](docs/Agents.md) for more information!


# 🍀 So, how much of the game is luck?
To quantify this question, here are the results of playing a very simple greedy strategy
against our current best strategy: expectimax with a search depth of 7. This should give
us a good idea of how lucky you have to be when playing a very simple strategy to beat
someone playing very well.

**So, what are the results?**

| Agent | Win Percentage |
| ----- | -------------- |
| [Expectimax](src/main/java/com/sothatsit/royalur/ai/ExpectimaxAgent.java) (Depth 7)  | 85% |
| [Greedy](src/main/java/com/sothatsit/royalur/ai/GreedyAgent.java)                    | 15% |

This suggests that even if you are a world-class Game of Ur player, there is always a
chance that someone with very meagre skills could still win! This is part of what makes
The Royal Game of Ur such an approachable and exciting game, with matches often coming
down to the wire.


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
