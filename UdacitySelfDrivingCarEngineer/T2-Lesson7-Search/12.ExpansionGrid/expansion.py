# -----------
# User Instructions:
#
# Modify the function search so that it returns
# a table of values called expand. This table
# will keep track of which step each node was
# expanded.
#
# Make sure that the initial cell in the grid
# you return has the value 0.
# ----------


grid = [[0, 0, 1, 0, 0, 0],
        [0, 0, 1, 0, 0, 0],
        [0, 0, 0, 0, 1, 0],
        [0, 0, 1, 1, 1, 0],
        [0, 0, 0, 0, 1, 0]]
init = [0, 0]
goal = [len(grid) - 1, len(grid[0]) - 1]
cost = 1

delta = [[-1, 0],  # go up
         [0, -1],  # go left
         [1, 0],  # go down
         [0, 1]]  # go right

delta_name = ['^', '<', 'v', '>']


def search(grid, init, goal, cost):
    # ----------------------------------------
    # insert code here
    # ----------------------------------------

    # open list elements are of the type: [g, x, y]

    closed = [[0 for row in range(len(grid[0]))] for col in range(len(grid))]
    # starting point as checked
    closed[init[0]][init[1]] = 1
    expand = [[-1 for row in range(len(grid[0]))] for col in range(len(grid))]

    x = init[0]
    y = init[1]
    g = 0

    open = [[g, x, y]]

    found = False  # flag that is set when search complete
    resign = False  # flag set if we can't find expand
    count = 0

    print('initial open list:')
    for i in range(len(open)):
        print(' ', open[i])
    print('----')

    while found is False and resign is False:

        # check if we still have elements on the open list
        if len(open) == 0:
            resign = True
            print('fail')
            # print('###### Search terminated without success')

        else:
            # remove node from list
            # remove the element with the smallest g-value
            open.sort()
            open.reverse()
            next = open.pop()
            print('take list item')
            print(next)
            x = next[1]
            y = next[2]
            g = next[0]
            expand[x][y] = count
            count += 1

            # check if we are done
            if x == goal[0] and y == goal[1]:
                found = True
                print(next)
                print('###### Search successful')

            else:
                # expand winning elementand add to new open list
                for i in range(len(delta)):
                    x2 = x + delta[i][0]
                    y2 = y + delta[i][1]
                    if x2 >= 0 and x2 < len(grid) and y2 >= 0 and y2 < len(grid[0]):
                        if closed[x2][y2] == 0 and grid[x2][y2] == 0:
                            g2 = g + cost
                            open.append([g2, x2, y2])
                            print('append list item')
                            print([g2, x2, y2])
                            closed[x2][y2] = 1

    for i in range(len(expand)):
        print(expand[i])


search(grid, init, goal, cost)
