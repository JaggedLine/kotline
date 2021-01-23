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
        outline = Outline.none
        disabled {
            cursor = Cursor.default
        }
    }
    val flatButton by css {
        background = "none"
        border = "none"
        padding = "0"
        fontFamily = "inherit"
        cursor = Cursor.pointer
        outline = Outline.none
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
        disabled {
            color = Color.black
            backgroundColor = rgb(190, 213, 210)
        }
    }
    val mySelect by css {
        display = Display.inlineBlock
        padding(0.4.em, 1.6.em, 0.4.em, 0.8.em)
        backgroundImage = Image("url(arrow-down.svg)")
        backgroundRepeat = BackgroundRepeat.noRepeat
        backgroundPosition = "calc(100% - 10px) calc(50% - 1px)"
        border = "none"
        borderRadius = 1.25.em
        fontFamily = "inherit"
        lineHeight = LineHeight("1.5em")
        cursor = Cursor.pointer
        outline = Outline.none
    }
    val myInput by css {
        padding(0.1.em, 0.em)
        minWidth = 0.px
        backgroundColor = Color.transparent
        border = "none"
        borderBottom = "1px solid grey"
        fontFamily = "inherit"
        lineHeight = LineHeight("1.5em")
        outline = Outline.none
        focus {
            borderColor = Color.darkGreen
        }
    }
}