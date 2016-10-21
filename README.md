# PUZZLE-GENERATOR #

This code generates a zelda-like (or lufia 2 -like) dungeon puzzle skeleton, and then creates a graphical representation
by positioning each room to achieve minimum area coverage.

### puzzle algorithm description ###

The graph is initially described as a graph having rooms as nodes and doors as edges. iteratively, an expansion seed is selected. each seed adds rooms, keys, containers etc. Each round assures that the puzzle is always traversable and for every door a reacheable key is present.

### graphic disposition algorithim explanation

When the previous step is finished, a graphical representation of the puzzle is built. the current algorithm for graphic representation looks like this:
![graphic rep](https://raw.githubusercontent.com/emanuelesan/puzzle-generator/master/graphdemo.gif)

As you can probably guess, this is a brute force algorithm and given the exponentially widening domain of the solution, it can be very slow. however, the algorithm usually finds a good solution pretty fast. the currently sole exposed function handles the cases in which the solution is not found so fast. anyway expect exponentially long time with complexity increase.

### Next steps ###
* create a room puzzle placeholder, which can host some kind of door-opening puzzle or chest-showing one.
* provide a sokoban style puzzle generator for the previously described placeholder.
* try a force-directed graph positioning algorithm. 
* OR a dynamic programming approach. 
* OR a simulated annealing approach!

### Who do I talk to? ###

* feel free to open issues or contact me directly if you have inquiries!
