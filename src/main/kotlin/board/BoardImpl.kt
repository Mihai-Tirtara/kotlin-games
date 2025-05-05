package board

open class SquareBoardImpl(override val width: Int) : SquareBoard
{
    private val  cells : Array<Array<Cell>> = Array(width) {i -> Array(width) {j -> Cell(i+1,j+1)} }

    override fun getCellOrNull(i: Int, j: Int): Cell? {

        return if (i < 1 || i > width || j < 1 || j > width) null else cells[i-1][j-1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        if (i < 1 || i > width || j < 1 || j > width) throw IllegalArgumentException("Values outside of the range of this board")
        else return cells[i-1][j-1]
    }

    override fun getAllCells(): Collection<Cell> {
        return (1..width).flatMap { i -> (1..width).map { j -> cells[i-1][j-1] } }.toCollection(mutableListOf())
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        var range : IntProgression = jRange
        if(width in jRange ) {
            when {
                jRange.last > width -> range = jRange.first..width
                jRange.first > width -> range = width..jRange.last

            }
        }
        return range.map { j -> cells[i-1][j-1] }.toList()
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        var range : IntProgression = iRange
        if(width in iRange ) {
            when {
                iRange.last > width -> range = iRange.first..width
                iRange.first > width -> range = width..iRange.last

            }
        }
        return range.map { i -> cells[i-1][j-1] }.toList()
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        //index starts from 0 in the array
        val i = this.i -1
        val j = this.j-1
        return when(direction) {
            Direction.UP -> cells.getOrNull(i-1)?.getOrNull(j)
            Direction.DOWN -> cells.getOrNull(i+1)?.getOrNull(j)
            Direction.RIGHT -> cells.getOrNull(i)?.getOrNull(j+1)
            Direction.LEFT -> cells.getOrNull(i)?.getOrNull(j-1)
        }
    }

}

class GameBoardImpl<T>( width: Int) : SquareBoardImpl(width),GameBoard<T>
{
    private val cellValues = mutableMapOf<Cell, T?>()

    // Initialize if needed
    init {
        // We can use getAllCells() from SquareBoard
        for (cell in getAllCells()) {
            cellValues[cell] = null
        }
    }

    override fun get(cell: Cell): T? {
        return cellValues[cell];
    }

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cellValues.filter{ (_,value) -> predicate(value)}.keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return cellValues.entries.find { (_,value) -> predicate(value) }?.key
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cellValues.any { (_,value) -> predicate(value) }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        println(cellValues)
        println(cellValues.all { (_,value) -> predicate(value) })
        return cellValues.all { (_,value) -> predicate(value) }
    }

}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl<T>(width)

