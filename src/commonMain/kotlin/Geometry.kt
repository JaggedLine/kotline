import kotlin.math.*

operator fun Coords.minus(other: Coords): Coords {
    return Coords(this.row - other.row, this.column - other.column)
}

fun Coords.norm(): Int {
    return this.row * this.row + this.column * this.column
}

infix fun Coords.cross(other: Coords): Int {
    return this.row * other.column - this.column * other.row
}

fun area(a: Coords, b: Coords, c: Coords): Int {
    return (b - a) cross (c - a)
}

fun intersectProjected(a: Int, b: Int, c: Int, d: Int): Boolean {
    val a1 = min(a, b)
    val b1 = max(a, b)
    val c1 = min(c, d)
    val d1 = max(c, d)
    return max(a1, c1) <= min(b1, d1)
}

fun intersect(a: Coords, b: Coords, c: Coords, d: Coords): Boolean {
    return intersectProjected(a.row, b.row, c.row, d.row)
            && intersectProjected(a.column, b.column, c.column, d.column)
            && area(a, b, c) * area(a, b, d) <= 0
            && area(c, d, a) * area(c, d, b) <= 0
}
