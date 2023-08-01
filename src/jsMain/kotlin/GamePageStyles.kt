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
        backgroundColor = rgb(237, 255, 250)
        display = Display.flex
        flexBasis = FlexBasis("400px")
        flexGrow = 5.0
        flexShrink = 0.0
        minHeight = 400.px
        padding = Padding(30.px)
    }

    val rightContainer by css {
        backgroundColor = rgb(217, 232, 230)
        display = Display.flex
        flexBasis = FlexBasis("400px")
        flexDirection = FlexDirection.column
        flexGrow = 1.0
        flexShrink = 0.0
    }

    val submitSection by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        padding = Padding(30.px)
    }

    val sizeRow by css {
        display = Display.flex
        flexDirection = FlexDirection.row
    }

    val sizeTitle by css {
        fontWeight = FontWeight.normal
        margin = Margin(0.px, LinearDimension.auto, 0.px, 0.px)
    }

    val sizeValue by css {
        fontSize = 0.9.em
        margin = Margin(0.px)
    }

    val nameInput by css {
        fontSize = 1.em
        marginTop = 30.px
        textAlign = TextAlign.center
        width = 100.pct
    }

    val submitButton by css {
        fontSize = 1.em
        marginTop = 20.px
    }

    val enterGodModeButton = submitButton
}
