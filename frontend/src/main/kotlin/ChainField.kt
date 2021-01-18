import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.js.*
import react.*
import styled.*
import kotlin.math.*

data class Point(val x: Int, val y: Int)

external interface ChainFieldProps : RProps {
    var sizeX: Int
    var sizeY: Int
    var startPoint: Point
    var endPoint: Point

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
    var points: MutableList<Point>
    var busy: Boolean
    var coveredNode: Point?
}

class ChainField(props: ChainFieldProps) : RComponent<ChainFieldProps, ChainFieldState>() {
    init {
        state.points = mutableListOf(props.startPoint)
        state.busy = false
        state.coveredNode = null
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                position = Position.relative
                margin(LinearDimension.auto)
                height = (props.gridStep * (props.sizeX - 1) + props.nodeRadius * 2).px
                width = (props.gridStep * (props.sizeY - 1) + props.nodeRadius * 2).px
                backgroundColor = props.backgroundColor
            }
            if (props.showGrid) {
                for (i in 0 until props.sizeX) {
                    styledDiv {
                        css {
                            position = Position.absolute
                            top = (i * props.gridStep + props.nodeRadius - props.gridWidth / 2).px
                            left = (props.nodeRadius - props.gridWidth / 2).px
                            height = props.gridWidth.px
                            width = ((props.sizeY - 1) * props.gridStep + props.gridWidth).px
                            backgroundColor = props.gridColor
                        }
                    }
                }
                for (j in 0 until props.sizeY) {
                    styledDiv {
                        css {
                            position = Position.absolute
                            top = (props.nodeRadius - props.gridWidth / 2).px
                            left = (j * props.gridStep + props.nodeRadius - props.gridWidth / 2).px
                            height = ((props.sizeX - 1) * props.gridStep + props.gridWidth).px
                            width = props.gridWidth.px
                            backgroundColor = props.gridColor
                        }
                    }
                }
            }
            for (i in 0 until state.points.size - 1) {
                val x1 = state.points[i].x * props.gridStep
                val y1 = state.points[i].y * props.gridStep
                val x2 = state.points[i + 1].x * props.gridStep
                val y2 = state.points[i + 1].y * props.gridStep
                val xc = (x1 + x2) / 2
                val yc = (y1 + y2) / 2
                val angle = -atan2((y2 - y1) * 1.0, (x2 - x1) * 1.0) * 180 / PI
                val len = sqrt((x2 - x1) * (x2 - x1) * 1.0 + (y2 - y1) * (y2 - y1) * 1.0) +
                        props.segmentWidth
                val toDelete = state.coveredNode in state.points &&
                        state.points.indexOf(state.coveredNode) <= i
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
            for (i in 0 until props.sizeX) {
                for (j in 0 until props.sizeY) {
                    val curPoint = Point(i, j)
                    val isCovered = curPoint == state.coveredNode
                    val isUsed = curPoint in state.points
                    val toDelete = state.coveredNode in state.points && isUsed &&
                            state.points.indexOf(state.coveredNode) <=
                            state.points.indexOf(curPoint)
                    styledDiv {
                        css {
                            position = Position.absolute
                            top = (i * props.gridStep).px
                            left = (j * props.gridStep).px
                            height = (props.nodeRadius * 2).px
                            width = (props.nodeRadius * 2).px
                            backgroundColor = when {
                                curPoint == props.startPoint -> props.startNodeColor
                                curPoint == props.endPoint -> props.endNodeColor
                                toDelete -> props.deleteColor
                                isCovered && !isUsed -> props.hoverNodeColor
                                !isCovered && isUsed -> props.usedNodeColor
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
            for (i in 0 until props.sizeX) {
                for (j in 0 until props.sizeY) {
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
                                if (!state.busy) {
                                    setState {
                                        coveredNode = Point(i, j)
                                    }
                                }
                            }
                            onMouseOutFunction = {
                                setState {
                                    coveredNode = null
                                }
                            }
                            onClickFunction = {
                                setState {
                                    state.points.add(Point(i, j))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}