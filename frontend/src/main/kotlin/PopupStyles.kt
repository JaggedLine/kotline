import kotlinx.css.*
import kotlinx.css.properties.*
import styled.StyleSheet

object PopupStyles : StyleSheet("PopupStyles", isStatic = true) {
    val wrapper by css {
        position = Position.fixed
        top = 0.px
        left = 0.px
        bottom = 0.px
        right = 0.px
        display = Display.flex
        backgroundColor = rgba(0, 0, 0, 0.4)
        zIndex = 1000
    }
    val popup by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        margin(LinearDimension.auto)
        height = 100.pct - 20.px
        maxHeight = 600.px
        width = 100.pct - 20.px
        maxWidth = 900.px
        backgroundColor = Color.white
        borderRadius = 20.px
        boxShadow(Color.dimGrey, (-3).px, 3.px, 10.px)
        media("(max-height: 600px), (max-width: 900px)") {
            maxHeight = LinearDimension.initial
            maxWidth = LinearDimension.initial
        }
    }
    val header by css {
        display = Display.flex
        flexDirection = FlexDirection.row
        flexShrink = 0.0
        padding(15.px)
        borderBottom = "1px solid whitesmoke"
    }
    val label by css {
        margin(0.px, LinearDimension.auto, 0.px, 5.px)
        fontWeight = FontWeight.normal
    }
    val close by css {
        height = 45.px
        width = 45.px
        fontSize = 16.px
    }
    val content by css {
        flexGrow = 1.0
        padding(15.px)
        fontSize = 20.px
        overflow = Overflow.auto
        p {
            marginTop = 0.px
        }
    }
}