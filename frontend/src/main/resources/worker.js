const dir = [[1, 2], [-1, 2], [1, -2], [-1, -2], [2, 1], [-2, 1], [2, -1], [-2, -1]]

function crossProduct(a, b) {
    return a.row * b.column - a.column * b.row
}

function minus(a, b) {
    return {
        row: a.row - b.row,
        column: a.column - b.column
    }
}

function segmentsIntersect(a, b, c, d) {
    let prod1 = crossProduct(minus(b, a), minus(c, a))
    let prod2 = crossProduct(minus(b, a), minus(d, a))
    let prod3 = crossProduct(minus(d, c), minus(a, c))
    let prod4 = crossProduct(minus(d, c), minus(b, c))
    let prod12 = prod1 * prod2
    let prod34 = prod3 * prod4
    if (prod12 > 0 || prod34 > 0) {
        return false
    }
    return prod12 < 0 && prod34 < 0;
}

function inside(point, field) {
    return point.row >= 0 && point.row < field.size.rows &&
        point.column >= 0 && point.column < field.size.columns
}

let chain = []
let bestChain = []

function recursiveGen(field) {
    let lastPoint = chain[chain.length - 1]
    if (lastPoint.row === field.end.row && lastPoint.column === field.end.column) {
        if (chain.length >= bestChain.length) {
            bestChain = chain.slice(0, chain.length)
            postMessage({
                finished: false,
                polyline: bestChain
            })
        }
        return
    }
    for (let id = 0; id < dir.length; id++) {
        let nextPoint = {
            row: lastPoint.row + dir[id][0],
            column: lastPoint.column + dir[id][1]
        }
        if (!inside(nextPoint, field)) {
            continue
        }
        let success = true
        for (let i = 0; i < chain.length; i++) {
            if (nextPoint.row === chain[i].row && nextPoint.column === chain[i].column) {
                success = false
                break
            }
        }
        if (!success) continue
        for (let i = 0; i < chain.length - 1; i++) {
            if (segmentsIntersect(chain[i], chain[i + 1], lastPoint, nextPoint)) {
                success = false
                break
            }
        }
        if (success) {
            chain.push(nextPoint)
            recursiveGen(field)
            chain.pop()
        }
    }
}

onmessage = function (e) {
    let field = e.data
    chain.push(field.start)
    recursiveGen(field)
    postMessage({
        finished: true
    })
}