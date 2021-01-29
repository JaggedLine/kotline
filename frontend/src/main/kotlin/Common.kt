import org.w3c.dom.url.*
import kotlin.js.*

data class Point(val x: Int, val y: Int)

operator fun Point.minus(other: Point): Point {
    return Point(this.x - other.x, this.y - other.y)
}

fun Point.toJson(): Json {
    return json().apply {
        this["row"] = this@toJson.x
        this["column"] = this@toJson.y
    }
}

data class Field(var sizeX: Int, var sizeY: Int, var startPoint: Point, var endPoint: Point)

fun Field.toJson(): Json {
    return json().apply {
        this["size"] = json().apply {
            this["rows"] = this@toJson.sizeX
            this["columns"] = this@toJson.sizeY
        }
        this["start"] = this@toJson.startPoint.toJson()
        this["end"] = this@toJson.endPoint.toJson()
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

fun crossProduct(a: Point, b: Point): Int {
    return a.x * b.y - a.y * b.x
}

fun dotProduct(a: Point, b: Point): Int {
    return a.x * b.x + a.y * b.y
}

fun isKnightMove(from: Point, to: Point): Boolean {
    return dotProduct(from - to, from - to) == 5
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

fun canAdd(polyline: List<Point>, nextPoint: Point): Boolean {
    if (!isKnightMove(polyline.last(), nextPoint)) {
        return false
    }
    for (i in 0 until polyline.size - 1) {
        if (segmentsIntersect(polyline[i], polyline[i + 1], polyline.last(), nextPoint)) {
            return false
        }
    }
    return true
}