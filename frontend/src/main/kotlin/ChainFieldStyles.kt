import kotlinx.css.*

fun ChainFieldProps.chainFieldStyle() {
    backgroundColor = Color.transparent

    showGrid = true
    gridColor = rgb(48, 86, 88)
    gridWidth = 3
    gridStep = 80

    segmentWidth = 5
    segmentColor = Color.black

    nodeRadius = 8
    clickableNodeRadius = 15
    nodeColor = Color.transparent
    hoverNodeColor = Color.grey
    usedNodeColor = Color.white
    usedNodeBorderColor = Color.black
    usedNodeBorderWidth = 4
    startNodeColor = Color.black
    endNodeColor = Color.black

    deleteColor = rgba(255, 127, 127, 0.9)
}