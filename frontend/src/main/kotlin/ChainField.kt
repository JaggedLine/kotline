import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.*
import react.*
import styled.*
import kotlin.math.*

external interface ChainFieldProps : RProps {
    var field: Field
    var onPolylineChange: (List<Point>) -> Unit

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

external interface ChainFieldState : RState {
    var polyline: MutableList<Point>
    var coveredNode: Point?
}

class ChainField : RComponent<ChainFieldProps, ChainFieldState>() {
    init {
        state.polyline = mutableListOf()
        state.coveredNode = null
    }

    fun getPolyline(): List<Point> {
        return state.polyline
    }

    fun clearPolyline() {
        setState {
            polyline = mutableListOf(props.field.startPoint)
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
                margin(LinearDimension.auto)
                height = (props.gridStep * (props.field.sizeX - 1) + props.nodeRadius * 2).px
                width = (props.gridStep * (props.field.sizeY - 1) + props.nodeRadius * 2).px
                backgroundColor = props.backgroundColor
            }
            if (props.showGrid) {
                for (i in 0 until props.field.sizeX) {
                    styledDiv {
                        css {
                            position = Position.absolute
                            top = (i * props.gridStep + props.nodeRadius - props.gridWidth / 2).px
                            left = (props.nodeRadius - props.gridWidth / 2).px
                            height = props.gridWidth.px
                            width = ((props.field.sizeY - 1) * props.gridStep + props.gridWidth).px
                            backgroundColor = props.gridColor
                        }
                    }
                }
                for (j in 0 until props.field.sizeY) {
                    styledDiv {
                        css {
                            position = Position.absolute
                            top = (props.nodeRadius - props.gridWidth / 2).px
                            left = (j * props.gridStep + props.nodeRadius - props.gridWidth / 2).px
                            height = ((props.field.sizeX - 1) * props.gridStep + props.gridWidth).px
                            width = props.gridWidth.px
                            backgroundColor = props.gridColor
                        }
                    }
                }
            }
            for (i in 0 until state.polyline.size - 1) {
                val x1 = state.polyline[i].x * props.gridStep
                val y1 = state.polyline[i].y * props.gridStep
                val x2 = state.polyline[i + 1].x * props.gridStep
                val y2 = state.polyline[i + 1].y * props.gridStep
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
            for (i in 0 until props.field.sizeX) {
                for (j in 0 until props.field.sizeY) {
                    val curPoint = Point(i, j)
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
                                curPoint == props.field.startPoint -> props.startNodeColor
                                curPoint == props.field.endPoint -> props.endNodeColor
                                isCovered && !isUsed -> props.hoverNodeColor
                                !toDelete && isUsed -> props.usedNodeColor
                                toDelete -> props.deleteColor
                                else -> props.nodeColor
                            }
                            border = when {
                                isUsed -> "${props.usedNodeBorderWidth}px solid ${props.usedNodeBorderColor}"
                                else -> "none"
                            }
                            borderRadius = 50.pct
                        }
                    }
                }
            }
            for (i in 0 until props.field.sizeX) {
                for (j in 0 until props.field.sizeY) {
                    val curPoint = Point(i, j)
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