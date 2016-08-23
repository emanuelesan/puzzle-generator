# PUZZLE-GENERATOR #

This code generates a zelda-like (or lufia 2 -like) dungeon puzzle skeleton, and then creates a graphical representation
by positioning each room to achieve minimum area coverage.

### algorithm description ###

* current version: pretty much unstable 

### algorithim explanation
the graph is initially described as a graph having rooms as nodes and doors as edges. iteratively, an expansion seed is selected. each seed adds rooms, keys, containers etc. each round assures that the puzzle is always traversable and for every door a reacheable key is present.

when the previous step is finished, a graphical representation of the puzzle is built. the current algorithm for graphic representation looks like this:
![graphic rep](https://raw.githubusercontent.com/emanuelesan/puzzle-generator/master/graphdemo.gif)

as you can probably guess, this is a brute force algorithm and given the exponentially widening domain of the solution, it's quite slow. however, the algorithm usually finds a good solution pretty fast. the currently sole exposed function handles the cases in which the solution is not found so fast. anyway expect exponentially long time with complexity increase.

### Next steps ###
* try a force-directed graph positioning algorithm. 
* OR a dynamic programming approach. 
* OR a simulated annealing approach!

### Who do I talk to? ###

* feel free to open issues or contact me directly if you have inquiries!
