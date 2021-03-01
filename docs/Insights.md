# 🤖 AI insights about The Royal Game of Ur
We've used our [AI agents](/docs/Agents.md) to develop some insights
about The Royal Game of Ur, which are listed below. If you have
any other metrics you'd like to see measured about the game,
open an issue, and we can discuss the best way to measure them!


# 🍀 How much of the game is luck? 🍀
To quantify this question, we chose three different AI agents to
represent an okay player, a good player, and a great player.

| Skill Level  | AI Agent |
| ------------ | -------- |
| Okay Player  | [Greedy Agent](/docs/Agents.md#-the-greedy-agent-) |
| Good Player  | [Expectimax Agent](/docs/Agents.md#-the-expectimax-agent-) with a search depth of 3 |
| Great Player |[Panda Agent](/docs/Agents.md#-the-panda-agent-) with a search depth of 7 and full search for 2 moves |

We then played all of these agents against one another to get an
idea of how lucky you would have to be to win against players
of different skill levels.

## 📊 Results

### Okay Player vs. Good Player

| Agent              | Win Percentage |
| ------------------ | -------------- |
| Expectimax Depth 3 | 83.2%          |
| Greedy             | 16.8%          |


### Good Player vs. Great Player

| Agent              | Win Percentage |
| ------------------ | -------------- |
| Panda Depth 7      | 77.0%          |
| Expectimax Depth 3 | 23.0%          |


### Okay Player vs. Great Player

| Agent         | Win Percentage |
| ------------- | -------------- |
| Panda Depth 7 | 92.0%          |
| Greedy        | 8.0%           |

## Conclusions
These results suggest that even if you are a world-class Game of Ur player, there is _always_
a chance that someone with very meagre skills could still win! This is part of what makes
The Royal Game of Ur such an approachable and exciting game, with matches often coming
down to the wire even with big skill disparities. However, this chance is still very
small when the skill gap between players is large.

## Run the Luck target yourself
If you'd like to run the above tests yourself, you can run them through the
RoyalUrAnalysis CLI using:
```
java -jar target/CLI.jar Luck
```


# 🐇 Does playing first give you an advantage? 🐇
Another question that people may wonder is: **Does playing as light or dark give you
an advantage?**

Well, we ran the numbers by playing two depth-5 panda agents against one another
for 20,000 games, and here are the results:

| Colour | Win Percentage |
| ------ | -------------- |
| Light  | 58.4%          |
| Dark   | 41.6%          |

From these results you can see that playing as light gives you a significant advantage
when playing The Royal Game of Ur, with a 16.8% higher chance of winning! That said,
ending up playing second with dark is not the end of the world, with a still significant
41.6% chance of winning.

## Run the First-Move-Advantage target yourself
If you'd like to run the above tests yourself, you can run them through the
RoyalUrAnalysis CLI using:
```
java -jar target/CLI.jar First-Move-Advantage
```
