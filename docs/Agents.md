# 🤖 AI Agents to play The Royal Game of Ur
This file contains a list of all the current artifical intelligence (AI) strategies
that are implemented in this project to play
[The Royal Game of Ur](https://en.wikipedia.org/wiki/Royal_Game_of_Ur). This file
aims to describe the algorithms used by each of the agents. It also contains
statistics for each of the agents to demonstrate their strengths and weaknesses.


# 🎲 The Random Agent 🎲
The random agent is probably the most self-explanatory agent we have.
When it is the random agent's turn, it looks through all of its legal
moves, and picks a random one. Surprisingly, the random agent remains
quite effective against the worst strategies in the game. Unsurprisingly
however, more advanced agents wipe the floor with the random agent.

### 📊 Random vs. Expectimax (Depth 5)
Here are the results of a game between the random agent and
one of our best strategies: expectimax with a depth of 5:

| Agent                 | Win Percentage |
| --------------------- | -------------- |
| Expectimax (Depth 5)  | 99.5%          |
| Random                | 0.5%           |

Surprisingly, even against our best agent random still has a chance!
If you play randomly, you could expect that 1 in 200 games, you _might_
get lucky enough to win!

### 📄 Random Agent Source Code
The source code for the random agent is available in
[RandomAgent.java](/src/main/java/com/sothatsit/royalur/ai/RandomAgent.java).


# 👶 The First-Move Agent 👶
The first-move agent is the _worst_ possible strategy we have at playing
The Royal Game of Ur. This agent always picks the least advanced piece to
move.

### 📊 First-Move vs. Random
How bad could the first-move agent really be? The answer is _very, very_ bad. Here are some results of the
first-move agent playing against a _random_ player:

| Agent       | Win Percentage |
| ----------- | -------------- |
| Random      | 99.2%          |
| First-Move  | 0.8%           |

As you can see, **even against someone playing randomly**, this agent is _very_ unlikely
to win. This can be understood by considering that often this agent would get all of its
pieces onto the board before it ever considers taking a piece off the board. This gives
ample opportunity for its opponent to take its pieces before it decides to advance them.

### 📄 First-Move Agent Source Code
The source code for the first-move agent is available in
[FirstMoveAgent.java](/src/main/java/com/sothatsit/royalur/ai/FirstMoveAgent.java).


# 👴 The Last-Move Agent 👴
The last-move agent is a much better alternative to the first-move agent when
playing The Royal Game of Ur. This agent always picks the most advanced piece
to move. This leads this agent to perform much better than random, however it
is still not up to par when playing against more advanced agents.

### 📊 Last-Move vs. Random
**Here are the results of the last-move agent being played against the random agent:**

| Agent      | Win Percentage |
| ---------- | -------------- |
| Last-Move  | 89.7%          |
| Random     | 10.3%          |

Somewhat surprisingly, the random agent still has a chance against this simple strategy.
Against more advanced strategies however, this simple strategy performs much more poorly.

### 📊 Last-Move vs. Expectimax (Depth 5)
**Here are the results of the last-move agent being played
against our best expectimax agent with a depth of 5:**

| Agent                 | Win Percentage |
| --------------------- | -------------- |
| Expectimax (Depth 5)  | 88.7%          |
| Last-Move             | 11.3%          |

This shows us that although the last-move agent's strategy is much better than
picking random moves, there are still _much_ better strategies to choose from.

### 📄 Last-Move Agent Source Code
The source code for the last-move agent is available in
[LastMoveAgent.java](/src/main/java/com/sothatsit/royalur/ai/LastMoveAgent.java).


# 🤑 The Greedy Agent 🤑
The greedy agent is an agent that starts to develop some semblance of tactics
while playing The Royal Game of Ur. This agent is similar to the last-move
agent, except it prioritises taking pieces and moving pieces onto rosettes.

Here is the decision-making process of the greedy agent:
1. **Can the agent capture any pieces?** If it can, pick the most advanced
   piece that can make a capturing move, and move it.
2. **Can the agent move any pieces onto rosette tiles?** If it can, pick the
   most advanced piece that can move onto a rosette, and move it.
3. **Move the most advanced piece it can!**

This helps the greedy agent gain a modest advantage over the last-move agent.

### 📊 Greedy vs. Last-Move
Here are the results of the greedy agent playing against the last-move agent:

| Agent      | Win Percentage |
| ---------- | -------------- |
| Greedy     | 62.9%          |
| Last-Move  | 37.1%          |

These results demonstrate that the greedy agent does indeed perform
better than the last-move agent. The difference between them though is
modest, with the greedy agent winning only 25.8% more games than the
last-move agent.

### 📊 Greedy vs. Expectimax (Depth 5)
Here are the results of the greedy agent playing against a depth-5 expectimax:

| Agent                 | Win Percentage |
| --------------------- | -------------- |
| Expectimax (Depth 5)  | 81.8%          |
| Greedy                | 18.1%          |

As these results show, the greedy agent still gets easily defeated
by the depth-5 expectimax agent. This suggests that the strategy
involved in The Royal Game of Ur still dominates the match, even
when playing very sensible moves.

### 📄 Greedy Agent Source Code
The source code for the greedy agent is available in
[GreedyAgent.java](/src/main/java/com/sothatsit/royalur/ai/GreedyAgent.java).


# 📒 The Expectimax Agent 📒
The expectimax agent is currently our best agent. Expectimax (Expectation Maximisation)
is our first agent to look ahead at potential future moves, with an aim at maximising
the expected outcome of the moves it makes.

It does this by considering all the possible moves that could happen to some
depth into the future. It then scores all the possible end states after these
moves, and does a statistical analysis to determine which move is expected to
maximise its own score.

### Scoring End-States
The expectimax agent scores the potential end-states using the formula
`Σ own pieces advancement - Σ opponent's pieces advancement`, where
pieces advancement represents how many tiles each piece has been moved.
For example, if you moved a piece four spaces, you would gain 4 score.
The only way to lose score is if the opponent captures any of your pieces.

### Statistical Analysis of End-States
The score (otherwise termed utility) of each end-state is collapsed up the
tree of potential moves in two stages:
1. The move that is considered best by the active player is chosen, and the utility
   after that move is propagated up to step 2.
2. The best moves after each possible dice roll is determined. These utilities are
   then combined using a weighted sum based upon the probability of each dice roll.

This is repeated until we get all the way back up to the current state of the game.
At this point, we now have an accurate score for each move, and we can pick the
move with the highest score to play.

### 📊 Expectimax vs. Expectimax: How does the depth affect its play?
We have already shown above that expectimax beats all of our other strategies of
playing The Royal Game of Ur quite solidly. But how does it fair against itself
when its looking to different depths into the future?

Here are some results with expectimax searching to a depth of 3, 5, and 7 moves
into the future to decide its moves:

| Agent                 | Win Percentage |
| --------------------- | -------------- |
| Expectimax (Depth 7)  | 64.2%          |
| Expectimax (Depth 5)  | 54.0%          |
| Expectimax (Depth 3)  | 31.8%          |

These results show us that the depth to which the agent searches has a huge
impact on how well the agent performs. Unfortunately further testing at
higher and higher depths is infeasible due to limitations on available
compute. Therefore, there is clearly more work to be done with optimisations
and the exploration of new algorithms to crown our perfect strategy!

### 📄 Expectimax Agent Source Code
The source code for the expectimax agent is available in
[ExpectimaxAgent.java](/src/main/java/com/sothatsit/royalur/ai/ExpectimaxAgent.java).


# 🐼 The Panda Agent 🐼
One issue with expectimax is speed, as checking all possible moves within a
certain depth is expensive! Therefore, instead of calculating _all_ possible
moves, after a certain depth the panda agent simply ignores the possibility
of rolling 0's and 4's.

This sacrifices some accuracy, but it gains a significant amount in speed.

### 📊 Panda vs. Expectimax
For these tests, the panda is set to ignore rolls of 0 and 4 after a depth of 2.

**Depth 5**

| Agent       | Win Percentage | Milliseconds per move |
| ----------- | -------------- | --------------------- |
| Expectimax  | 50.0%          | 3.19 ms               |
| Panda       | 50.0%          | 6.43 ms               |

**Depth 6**

| Agent       | Win Percentage | Milliseconds per move |
| ----------- | -------------- | --------------------- |
| Expectimax  | 50.9%          | 97.5 ms               |
| Panda       | 49.1%          | 37.7 ms               |

**Depth 7**

| Agent       | Win Percentage | Milliseconds per move |
| ----------- | -------------- | --------------------- |
| Expectimax  | 52%            | 1504 ms               |
| Panda       | 48%            | 406 ms                |

These results show us that after a certain depth, ignoring the possibility of rolling
0's or 4's does not affect the performance of the agent too much. Therefore, the Panda
agent is able to perform almost as well as expectimax, with 2-3x the speed.

**Note:** Do keep in mind that these ms/move statistics will change from computer
to computer based on the hardware used. The numbers are really only useful for
comparison between algorithms.

### Why is this agent named Panda? 🐼
This agent is pretty lazy with its move checking, and if
there's one thing pandas are famous for, its their laziness!

### 📄 The Panda Agent Source Code
The source code for the panda agent is available in
[PandaAgent.java](/src/main/java/com/sothatsit/royalur/ai/PandaAgent.java).
