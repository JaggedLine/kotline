import kotlinx.css.*
import kotlinx.css.properties.*
import styled.StyleSheet

object PopupStyles : StyleSheet("PopupStyles", isStatic = true) {
    val wrapper by css {
        backgroundColor = Color.black.withAlpha(0.4)
        bottom = 0.px
        display = Display.flex
        left = 0.px
        position = Position.fixed
        right = 0.px
        top = 0.px
        zIndex = 1000
    }

    val popup by css {
        backgroundColor = Color.white
        borderRadius = 20.px
        boxShadow += BoxShadow(Color.dimGrey, (-3).px, 3.px, 10.px)
        display = Display.flex
        flexDirection = FlexDirection.column
        height = 100.pct - 20.px
        margin = Margin(LinearDimension.auto)
        maxHeight = 600.px
        maxWidth = 900.px
        width = 100.pct - 20.px
        media("(max-height: 600px), (max-width: 900px)") {
            maxHeight = LinearDimension.initial
            maxWidth = LinearDimension.initial
        }
    }

    val header by css {
        borderBottom = Border(1.px, BorderStyle.solid, Color.whiteSmoke)
        display = Display.flex
        flexDirection = FlexDirection.row
        flexShrink = 0.0
        padding = Padding(15.px)
    }

    val label by css {
        fontWeight = FontWeight.normal
        margin = Margin(0.px, LinearDimension.auto, 0.px, 5.px)
    }

    val close by css {
        fontSize = 16.px
        height = 45.px
        width = 45.px
    }

    val content by css {
        flexGrow = 1.0
        fontSize = 20.px
        overflow = Overflow.auto
        padding = Padding(15.px)
        p {
            marginTop = 0.px
        }
    }
}
