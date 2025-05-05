package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.GameBoardImpl
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */

class GameOfFifteen(val initializer: GameOfFifteenInitializer) : Game {
    val board: GameBoard<Int> = GameBoardImpl<Int>(4)
    override fun initialize() {
       val rows = 4
        val cols = 4

        for(i in 0 until rows)
        {
            for (j in 0 until cols)
            {
                val index = i * cols + j
                if(index < initializer.initialPermutation.size)
                {
                    board[Cell(i+1,j+1)] = initializer.initialPermutation[index]
                }
            }
        }
    }

    override fun canMove(): Boolean {
        val emptyCell = board.find { it == null }  ?: return false

        return Direction.values().any {
            direction ->
            with(board) {
                emptyCell.getNeighbour(direction) != null
            }
        }

    }

    override fun hasWon(): Boolean {
        val width = board.width
        var expectedValue = 1

        // Check all cells for expected values
        for (i in 1..width) {
            for (j in 1..width) {
                val cell = board.getCell(i, j)
                val value = board[cell]

                // Last cell should be empty
                if (i == width && j == width) {
                    if (value != null) return false
                }
                // All other cells should have the expected sequential value
                else {
                    if (value != expectedValue) return false
                    expectedValue++
                }
            }
        }

        return true
    }

    override fun processMove(direction: Direction) {
        // Find the empty cell
        val emptyCell = board.find { it == null } ?: return

        val oppositeDirection = when(direction) {
            Direction.UP -> Direction.DOWN
            Direction.DOWN -> Direction.UP
            Direction.LEFT -> Direction.RIGHT
            Direction.RIGHT -> Direction.LEFT
        }

        val cellToMove = with(board) {
            emptyCell.getNeighbour(oppositeDirection)
        } ?: return

        // Get the value of the cell that would move
        val valueToMove = board[cellToMove] ?: return

        // Swap the values
        board[emptyCell] = valueToMove
        board[cellToMove] = null
    }

    override fun get(i: Int, j: Int): Int? {
        val cell = board.getCell(i, j)
        return board[cell]
    }

}


fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game = GameOfFifteen(initializer)


