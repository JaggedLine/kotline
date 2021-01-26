import org.w3c.dom.url.*
import kotlin.js.*

data class Point(val x: Int, val y: Int)

operator fun Point.minus(other: Point): Point {
    return Point(this.x - other.x, this.y - other.y)
}

fun Point.jsonify(): Json {
    return json().apply {
        this["row"] = this@jsonify.x
        this["column"] = this@jsonify.y
    }
}

data class Field(var sizeX: Int, var sizeY: Int, var startPoint: Point, var endPoint: Point)

fun Field.jsonify(): Json {
    return json().apply {
        this["size"] = json().apply {
            this["rows"] = this@jsonify.sizeX
            this["columns"] = this@jsonify.sizeY
        }
        this["start"] = this@jsonify.startPoint.jsonify()
        this["end"] = this@jsonify.endPoint.jsonify()
    }
}

fun Field.toQueryString(): String {
    return URLSearchParams().apply {
        append("rows", this@toQueryString.sizeX.toString())
        append("columns", this@toQueryString.sizeY.toString())
        append("startRow", this@toQueryString.startPoint.x.toString())
        append("startColumn", this@toQueryString.startPoint.y.toString())
        append("endRow", this@toQueryString.endPoint.x.toString())
        append("endColumn", this@toQueryString.endPoint.y.toString())
    }.toString()
}

fun isKnightMove(from: Point, to: Point): Boolean {
    return to - from in setOf(
        Point(1, 2), Point(1, -2), Point(-1, 2), Point(-1, -2),
        Point(2, 1), Point(2, -1), Point(-2, 1), Point(-2, -1)
    )
}

fun crossProduct(a: Point, b: Point): Int {
    return a.x * b.y - a.y * b.x
}

fun segmentsIntersect(a: Point, b: Point, c: Point, d: Point): Boolean {
    val prod1 = crossProduct(b - a, c - a)
    val prod2 = crossProduct(b - a, d - a)
    val prod3 = crossProduct(d - c, a - c)
    val prod4 = crossProduct(d - c, b - c)
    val prod12 = prod1 * prod2
    val prod34 = prod3 * prod4
    if (prod12 > 0 || prod34 > 0) {
        return false
    }
    if (prod12 < 0 && prod34 < 0) {
        return true
    }
    return false
}

fun isPolylineValid(polyline: List<Point>, field: Field): Boolean {
    for (point in polyline) {
        if (point.x < 0 || point.x > field.sizeX || point.y < 0 || point.y > field.sizeY) {
            return false
        }
    }
    for (i in 0 until polyline.size - 1) {
        if (!isKnightMove(polyline[i], polyline[i + 1])) {
            return false
        }
    }
    for (i in 0 until polyline.size - 1) {
        for (j in 0 until polyline.size - 1) {
            if (segmentsIntersect(polyline[i], polyline[i + 1], polyline[j], polyline[j + 1])) {
                return false
            }
        }
    }
    return true
}