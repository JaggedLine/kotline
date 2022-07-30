import java.lang.Integer.max
import java.lang.Integer.min

operator fun Coords.minus(other: Coords): Coords {
    return Coords(this.row - other.row, this.column - other.column)
}

fun Coords.norm(): Int {
    return this.row * this.row + this.column * this.column
}

infix fun Coords.cross(other: Coords): Int {
    return this.row * other.column - this.column * other.row
}

fun area(A: Coords, B: Coords, C: Coords): Int {
    return (B - A) cross (C - A)
}

fun intersect1(_a: Int, _b: Int, _c: Int, _d: Int): Boolean {
    var a = _a
    var b = _b
    var c = _c
    var d = _d
    if (a > b) a = b.also { b = a }
    if (c > d) c = d.also { d = c }
    return max(a, c) <= min(b, d)
}

fun intersect(A: Coords, B: Coords, C: Coords, D: Coords): Boolean {
    return intersect1(A.row, B.row, C.row, D.row)
            && intersect1(A.column, B.column, C.column, D.column)
            && area(A, B, C) * area(A, B, D) <= 0
            && area(C, D, A) * area(C, D, B) <= 0
}

fun checkPolyline(n: Int, m: Int, polyline: List<Coords>): Pair<Boolean, String> {
    for (pt in polyline) {
        if (!(pt.row >= 0 && pt.column >= 0 && pt.row < n && pt.column < m)) {
            return Pair(false, "A point out of bounds")
        }
    }
    for (i in 1..polyline.lastIndex) {
        if ((polyline[i] - polyline[i - 1]).norm() != 5) {
            return Pair(false, "A segment is not a knight move")
        }
    }
    for (i in 2..polyline.lastIndex) {
        for (j in 1..i - 2) {
            if (intersect(polyline[i], polyline[i - 1], polyline[j], polyline[j - 1])) {
                return Pair(false, "Segments intersect")
            }
        }
    }
    return Pair(true, "")
}

fun verify(claim: Claim): Pair<Boolean, String> {
    if (claim.solution.isEmpty()) {
        return Pair(false, "Empty solution")
    }
    if (claim.solution[0] != claim.field.start) {
        return Pair(false, "The polyline doesn't start where it should")
    }
    if (claim.solution.last() != claim.field.end) {
        return Pair(false, "The polyline doesn't end where it should")
    }
    if (claim.solution.toSet().size != claim.solution.size) {
        return Pair(false, "Repeating nodes")
    }
    return checkPolyline(claim.field.size.rows, claim.field.size.columns, claim.solution)
}
