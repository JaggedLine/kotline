import kotlinx.css.*
import kotlinx.css.properties.*
import styled.StyleSheet

object CommonStyles : StyleSheet("CommonStyles", isStatic = true) {
    val container by css {
        position = Position.relative
        display = Display.flex
        flexGrow = 1.0
        flexDirection = FlexDirection.column
    }
    val myButton by css {
        display = Display.inlineBlock
        padding(0.5.em, 1.em)
        border = "none"
        borderRadius = 1.25.em
        fontFamily = "inherit"
        lineHeight = LineHeight("1.5em")
        textDecoration = TextDecoration.none
        cursor = Cursor.pointer
    }
    val flatButton by css {
        background = "none"
        border = "none"
        padding = "0"
        fontFamily = "inherit"
        cursor = Cursor.pointer
    }
    val hoverUnderlineButton by css {
        hover {
            textDecoration(TextDecorationLine.underline)
        }
    }
    val darkGreenButton by css {
        color = Color.white
        backgroundColor = rgb(48, 86, 88)
        hover {
            backgroundColor = rgb(65, 116, 119)
        }
        active {
            backgroundColor = rgb(48, 86, 88)
        }
    }
}