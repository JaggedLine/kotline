import kotlinx.css.*
import styled.StyleSheet

object GamePageStyles : StyleSheet("GamePageStyles", isStatic = true) {
    val pageLayout by css {
        display = Display.flex
        flexDirection = FlexDirection.row
        flexWrap = FlexWrap.wrap
        fontSize = 25.px
    }
    val fieldContainer by css {
        display = Display.flex
        flexGrow = 10.0
        flexShrink = 0.0
        flexBasis = FlexBasis("400px")
        minHeight = 400.px
        backgroundColor = rgb(237, 255, 250)
    }
    val rightContainer by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        flexGrow = 1.0
        flexShrink = 0.0
        flexBasis = FlexBasis("400px")
        padding(40.px)
        backgroundColor = rgb(217, 232, 230)
    }
    val sizeRow by css {
        display = Display.flex
        flexDirection = FlexDirection.row
    }
    val sizeTitle by css {
        margin(0.px, LinearDimension.auto, 0.px, 0.px)
        fontWeight = FontWeight.normal
    }
    val sizeValue by css {
        margin(0.px)
        fontStyle = FontStyle.italic
    }
    val nameInput by css {
        marginTop = 20.px
        width = 100.pct
        fontSize = 1.em
        textAlign = TextAlign.center
    }
    val submitButton by css {
        marginTop = 20.px
        fontSize = 1.em
    }
}