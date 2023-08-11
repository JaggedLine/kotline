package kuyanov.kotline.server

import Coords
import Submission
import intersect
import minus
import norm

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

fun verify(submission: Submission): Pair<Boolean, String> {
    if (submission.solution.isEmpty()) {
        return Pair(false, "Empty solution")
    }
    if (submission.solution.first() != submission.field.start) {
        return Pair(false, "The polyline doesn't start where it should")
    }
    if (submission.solution.last() != submission.field.end) {
        return Pair(false, "The polyline doesn't end where it should")
    }
    if (submission.solution.toSet().size != submission.solution.size) {
        return Pair(false, "Duplicate nodes")
    }
    return checkPolyline(submission.field.size.rows, submission.field.size.columns, submission.solution)
}
