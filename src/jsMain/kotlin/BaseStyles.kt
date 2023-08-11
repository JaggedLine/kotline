import kotlinx.css.*
import kotlinx.css.properties.*
import styled.StyleSheet

val globalStyles = CssBuilder().apply {
    "*, *::after, *::before" {
        boxSizing = BoxSizing.borderBox
    }

    html {
        display = Display.flex
        minHeight = 100.pct
    }

    body {
        display = Display.flex
        flex = Flex(1.0)
        flexDirection = FlexDirection.column
        fontFamily = "'Noto Sans JP', sans-serif"
        margin = Margin(0.px)
    }
}

object BaseStyles : StyleSheet("BaseStyles", isStatic = true) {
    val container by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        flexGrow = 1.0
        position = Position.relative
    }

    val scrollableWrapper by css {
        flexGrow = 1.0
        position = Position.relative
    }

    val scrollable by css {
        height = 100.pct
        overflow = Overflow.auto
        position = Position.absolute
        width = 100.pct
    }

    val myButton by css {
        border = Border.none
        borderRadius = 1.25.em
        cursor = Cursor.pointer
        display = Display.inlineBlock
        fontFamily = "inherit"
        lineHeight = LineHeight("1.5em")
        outline = Outline.none
        padding = Padding(0.5.em, 1.em)
        textDecoration = TextDecoration.none
        disabled {
            cursor = Cursor.default
        }
    }

    val linkButton by css {
        backgroundColor = Color.transparent
        borderWidth = 0.px
        cursor = Cursor.pointer
        display = Display.inlineBlock
        fontFamily = "inherit"
        outline = Outline.none
        padding = Padding(0.px)
        textDecoration = TextDecoration.none
    }

    val hoverUnderline by css {
        hover {
            textDecoration = TextDecoration(setOf(TextDecorationLine.underline))
        }
    }

    val iconButton by css {
        backgroundColor = Color.transparent
        borderRadius = 50.pct
        borderWidth = 0.px
        cursor = Cursor.pointer
        display = Display.flex
        flexShrink = 0.0
        fontFamily = "inherit"
        outline = Outline.none
        textDecoration = TextDecoration.none
        span {
            margin = Margin(LinearDimension.auto)
        }
    }

    val darkGreenButton by css {
        backgroundColor = rgb(48, 86, 88)
        color = Color.white
        hover {
            backgroundColor = rgb(65, 116, 119)
        }
        active {
            backgroundColor = rgb(48, 86, 88)
        }
        disabled {
            backgroundColor = rgb(190, 213, 210)
            color = Color.black
        }
    }

    val transparentButton by css {
        backgroundColor = Color.transparent
        color = Color.black.withAlpha(0.8)
        hover {
            backgroundColor = Color.black.withAlpha(0.05)
        }
        active {
            backgroundColor = Color.black.withAlpha(0.1)
        }
    }

    val mySelect by css {
        appearance = Appearance.none
        backgroundImage = Image("url(/static/arrow-down.svg)")
        backgroundPosition = RelativePosition("calc(100% - 10px) calc(50% - 1px)")
        backgroundRepeat = BackgroundRepeat.noRepeat
        border = Border.none
        borderRadius = 1.25.em
        cursor = Cursor.pointer
        display = Display.inlineBlock
        fontFamily = "inherit"
        lineHeight = LineHeight("1.5em")
        outline = Outline.none
        padding = Padding(0.4.em, 1.6.em, 0.4.em, 0.8.em)
    }

    val myInput by css {
        backgroundColor = Color.transparent
        border = Border.none
        borderBottom = Border(1.px, BorderStyle.solid, Color.grey)
        fontFamily = "inherit"
        lineHeight = LineHeight("1.5em")
        minWidth = 0.px
        outline = Outline.none
        padding = Padding(0.1.em, 0.em)
        focus {
            borderColor = Color.darkGreen
        }
    }
}
