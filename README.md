# 🎲 The Royal Game of Ur Analysis
This repository is dedicated to the technical analysis of
[The Royal Game of Ur](https://en.wikipedia.org/wiki/Royal_Game_of_Ur).

The Royal Game of Ur is an ancient Sumerian board game discovered by Sir Leonard Woolley in a
tomb in ancient Ur in 1922. The game dates back to **over 4000 years ago**, and is considered
by many to be the oldest board game in the world. This repository contains code with the goal
of analysing this old game, and determining how much of the game is luck, and how much is skill!

Learn the rules of the game at https://royalur.net/rules, or play the game over at https://royalur.net.


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
We currently have a small set of agents designed to play The Royal Game of Ur.

| Agent | Description |
| ----- | ----------- |
| [Random](/docs/Agents.md#-the-random-agent-)          | Picks a random move, every time! |
| [First-Move](/docs/Agents.md#-the-first-move-agent-)  | Picks the least advanced piece to move. |
| [Last-Move](/docs/Agents.md#-the-last-move-agent-)    | Picks the most advanced piece to move. |
| [Greedy](/docs/Agents.md#-the-greedy-agent-)          | Prioritises taking pieces, and moving onto rosettes. |
| [Expectimax](/docs/Agents.md#-the-expectimax-agent-)  | Does a statistical analysis to determine the best move. |

If you'd like to delve deeper into each agent and how they function,
check out [docs/Agents.md](/docs/Agents.md) for more information!


# 📈 Current Rankings
There are currently only a few agents implemented to play the game,
and here are their rankings:

| Agent | Win Percentage |
| ----- | -------------- |
| [Expectimax](/docs/Agents.md#-the-expectimax-agent-) (Depth 5)  | 87.5% |
| [Expectimax](/docs/Agents.md#-the-expectimax-agent-) (Depth 3)  | 77.6% |
| [Greedy](/docs/Agents.md#-the-greedy-agent-)                    | 61.3% |
| [Last-Move](/docs/Agents.md#-the-last-move-agent-)              | 50.0% |
| [Random](/docs/Agents.md#-the-random-agent-)                    | 23.5% |
| [First-Move](/docs/Agents.md#-the-first-move-agent-)            | 0.1%  |

These results may not show us the best way to play the game yet, but they definitely
tell us the worst. If you want to lose every game you play, there is no better
strategy than always moving your least advanced piece!

### Want more detailed statistics?
If you'd like to delve deeper into each agent and more statistics about them,
check out [Agents.md](docs/Agents.md) for more information!


# 🍀 So, how much of the game is luck?
To quantify this question, here are the results of playing a very simple greedy strategy
against our current best strategy: depth-7 expectimax. This should give us a good idea of
how lucky you have to be when playing a very simple strategy to beat someone playing very well.

| Agent | Win Percentage |
| ----- | -------------- |
| [Expectimax](/docs/Agents.md#-the-expectimax-agent-) (Depth 7)  | 85% |
| [Greedy](/docs/Agents.md#-the-greedy-agent-)                    | 15% |

These results suggest that even if you are a world-class Game of Ur player, there is _always_
a chance that someone with very meagre skills could still win! This is part of what makes
The Royal Game of Ur such an approachable and exciting game, with matches often coming
down to the wire even with big skill disparities.


# 🉐 Does playing first give you an advantage?
Another question that people may wonder, is whether playing as light or dark gives you
an advantage in your games? Well, we ran the numbers by playing two depth-5 expectimax
agents against one another, and here are the results:

| Colour | Win Percentage |
| ------ | -------------- |
| Light  | 61.9%          |
| Dark   | 38.1%          |

From these results you can see that playing as light gives you a significant advantage
when playing The Royal Game of Ur, with a whopping 23.8% higher chance of winning!


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
