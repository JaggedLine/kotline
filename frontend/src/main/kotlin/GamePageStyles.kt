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
        flexGrow = 10.0
        flexShrink = 0.0
        flexBasis = FlexBasis("400px")
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
        marginBottom = 10.px
    }
    val sizeTitle by css {
        margin(0.px, LinearDimension.auto, 0.px, 0.px)
        fontWeight = FontWeight.normal
    }
    val sizeValue by css {
        margin(0.px)
        fontStyle = FontStyle.italic
    }
    val scoreRow = sizeRow
    val scoreTitle = sizeTitle
    val scoreValue = sizeValue
    val nameInput by css {
        marginTop = 20.px
        fontSize = 1.em
    }
    val submitButton by css {
        marginTop = 20.px
        fontSize = 1.em
    }
}