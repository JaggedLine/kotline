import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.*
import react.*
import react.dom.*
import styled.*
import kotlin.math.*

fun canAdd(polyline: List<Coords>, nextPoint: Coords): Boolean {
    if ((nextPoint - polyline.last()).norm() != 5) {
        return false
    }
    for (i in 0..<polyline.size - 2) {
        if (intersect(polyline[i], polyline[i + 1], polyline.last(), nextPoint)) {
            return false
        }
    }
    return true
}

external interface ChainFieldProps : Props {
    var field: Field
    var onPolylineChange: (List<Coords>) -> Unit

    var backgroundColor: Color

    var showGrid: Boolean
    var gridColor: Color
    var gridWidth: Int
    var gridStep: Int

    var segmentWidth: Int
    var segmentColor: Color

    var nodeRadius: Int
    var clickableNodeRadius: Int
    var nodeColor: Color
    var hoverNodeColor: Color
    var usedNodeColor: Color
    var usedNodeBorderColor: Color
    var usedNodeBorderWidth: Int
    var startNodeColor: Color
    var endNodeColor: Color

    var deleteColor: Color
}

external interface ChainFieldState : State {
    var polyline: MutableList<Coords>
    var coveredNode: Coords?
}

class ChainField : RComponent<ChainFieldProps, ChainFieldState>() {
    init {
        state.polyline = mutableListOf()
        state.coveredNode = null
    }

    fun getPolyline(): List<Coords> {
        return state.polyline
    }

    fun setPolyline(newPolyline: List<Coords>) {
        setState {
            polyline = newPolyline.toMutableList()
            props.onPolylineChange(polyline)
        }
    }

    fun clearPolyline() {
        setState {
            polyline = mutableListOf(props.field.start)
            props.onPolylineChange(polyline)
        }
    }

    override fun componentDidMount() {
        clearPolyline()
    }

    override fun componentDidUpdate(prevProps: ChainFieldProps, prevState: ChainFieldState, snapshot: Any) {
        if (props.field != prevProps.field) {
            clearPolyline()
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                position = Position.relative
                margin = Margin(LinearDimension.auto)
                height = (props.gridStep * (props.field.size.rows - 1) + props.nodeRadius * 2).px
                width = (props.gridStep * (props.field.size.columns - 1) + props.nodeRadius * 2).px
                backgroundColor = props.backgroundColor
            }
            if (props.showGrid) {
                for (i in 0..<props.field.size.rows) {
                    styledDiv {
                        css {
                            position = Position.absolute
                            top = (i * props.gridStep + props.nodeRadius - props.gridWidth / 2).px
                            left = (props.nodeRadius - props.gridWidth / 2).px
                            height = props.gridWidth.px
                            width = ((props.field.size.columns - 1) * props.gridStep + props.gridWidth).px
                            backgroundColor = props.gridColor
                        }
                    }
                }
                for (j in 0..<props.field.size.columns) {
                    styledDiv {
                        css {
                            position = Position.absolute
                            top = (props.nodeRadius - props.gridWidth / 2).px
                            left = (j * props.gridStep + props.nodeRadius - props.gridWidth / 2).px
                            height = ((props.field.size.rows - 1) * props.gridStep + props.gridWidth).px
                            width = props.gridWidth.px
                            backgroundColor = props.gridColor
                        }
                    }
                }
            }
            for (i in 0..<state.polyline.size - 1) {
                val x1 = state.polyline[i].row * props.gridStep
                val y1 = state.polyline[i].column * props.gridStep
                val x2 = state.polyline[i + 1].row * props.gridStep
                val y2 = state.polyline[i + 1].column * props.gridStep
                val xc = (x1 + x2) / 2
                val yc = (y1 + y2) / 2
                val angle = -atan2((y2 - y1) * 1.0, (x2 - x1) * 1.0) * 180 / PI
                val len = sqrt((x2 - x1) * (x2 - x1) * 1.0 + (y2 - y1) * (y2 - y1) * 1.0) +
                        props.segmentWidth
                val toDelete = state.coveredNode in state.polyline &&
                        state.polyline.indexOf(state.coveredNode) <= i
                styledDiv {
                    css {
                        position = Position.absolute
                        top = (xc + props.nodeRadius - len / 2).px
                        left = (yc + props.nodeRadius - props.segmentWidth / 2).px
                        height = len.px
                        width = props.segmentWidth.px
                        transform.rotate(angle.deg)
                        borderRadius = (props.segmentWidth / 2).px
                        backgroundColor = when {
                            toDelete -> props.deleteColor
                            else -> props.segmentColor
                        }
                    }
                }
            }
            for (i in 0..<props.field.size.rows) {
                for (j in 0..<props.field.size.columns) {
                    val curPoint = Coords(i, j)
                    val isCovered = curPoint == state.coveredNode
                    val isUsed = curPoint in state.polyline
                    val toDelete = state.coveredNode in state.polyline && isUsed &&
                            state.polyline.indexOf(state.coveredNode) <
                            state.polyline.indexOf(curPoint)
                    styledDiv {
                        css {
                            position = Position.absolute
                            top = (i * props.gridStep).px
                            left = (j * props.gridStep).px
                            height = (props.nodeRadius * 2).px
                            width = (props.nodeRadius * 2).px
                            backgroundColor = when {
                                curPoint == props.field.start -> props.startNodeColor
                                curPoint == props.field.end -> props.endNodeColor
                                isCovered && !isUsed -> props.hoverNodeColor
                                !toDelete && isUsed -> props.usedNodeColor
                                toDelete -> props.deleteColor
                                else -> props.nodeColor
                            }
                            border = when {
                                isUsed -> Border(
                                    props.usedNodeBorderWidth.px, BorderStyle.solid, props.usedNodeBorderColor
                                )

                                else -> Border.none
                            }
                            borderRadius = 50.pct
                        }
                    }
                }
            }
            for (i in 0..<props.field.size.rows) {
                for (j in 0..<props.field.size.columns) {
                    val curPoint = Coords(i, j)
                    styledDiv {
                        css {
                            position = Position.absolute
                            top = (i * props.gridStep - props.clickableNodeRadius + props.nodeRadius).px
                            left = (j * props.gridStep - props.clickableNodeRadius + props.nodeRadius).px
                            height = (props.clickableNodeRadius * 2).px
                            width = (props.clickableNodeRadius * 2).px
                            backgroundColor = Color.transparent
                        }
                        attrs {
                            onMouseOverFunction = {
                                setState {
                                    coveredNode = curPoint
                                }
                            }
                            onMouseOutFunction = {
                                setState {
                                    coveredNode = null
                                }
                            }
                            onClickFunction = {
                                setState {
                                    if (curPoint in polyline) {
                                        while (polyline.last() != curPoint) {
                                            polyline.removeLast()
                                        }
                                    } else if (canAdd(polyline, curPoint)) {
                                        polyline.add(curPoint)
                                    }
                                    props.onPolylineChange(polyline)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
