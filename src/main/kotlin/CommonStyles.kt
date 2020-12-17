import kotlinx.css.*
import kotlinx.css.properties.*
import styled.StyleSheet

object CommonStyles: StyleSheet("CommonStyles", isStatic = true) {
    val container by css {
        display = Display.flex
        flexGrow = 1.0
        flexDirection = FlexDirection.column
    }
    val greenButton by css {
        color = Color.black
        backgroundColor = Color.lightGreen
        hover {
            backgroundColor = rgb(124, 228, 124)
        }
        active {
            backgroundColor = rgb(104, 214, 104)
        }
    }
    val blueButton by css {
        color = Color.black
        backgroundColor = Color.lightBlue
        hover {
            backgroundColor = rgb(149, 208, 228)
        }
        active {
            backgroundColor = rgb(125, 188, 209)
        }
    }
    val pinkButton by css {
        color = Color.black
        backgroundColor = Color.lightPink
        hover {
            backgroundColor = rgb(255, 159, 174)
        }
        active {
            backgroundColor = rgb(255, 138, 155)
        }
    }
    val yellowButton by css {
        color = Color.black
        backgroundColor = Color.gold
        hover {
            backgroundColor = rgb(245, 208, 11)
        }
        active {
            backgroundColor = rgb(230, 199, 36)
        }
    }
    val orangeButton by css {
        color = Color.black
        backgroundColor = Color.orange
        hover {
            backgroundColor = rgb(242, 160, 10)
        }
        active {
            backgroundColor = rgb(230, 155, 17)
        }
    }
    val greyButton by css {
        color = Color.black
        backgroundColor = Color.lightGrey
        hover {
            backgroundColor = rgb(199, 199, 199)
        }
        active {
            backgroundColor = Color.darkGrey
        }
    }
    val transparentButton by css {
        color = Color.black
        backgroundColor = Color.transparent
        hover {
            backgroundColor = Color.lightGrey
        }
        active {
            backgroundColor = Color.darkGrey
        }
    }
    val myButton by css {
        fontFamily = "inherit"
        border(1.px, BorderStyle.solid, rgba(0, 0, 0, 0.1))
        borderRadius = 10.px
        hover {
            boxShadow(Color.lightGrey, 2.px, 2.px, 10.px)
        }
    }
    val linkButton by css {
        background = "none"
        border = "none"
        padding = "0"
        color = rgb(0,102,153)
        textDecoration = TextDecoration(setOf(TextDecorationLine.underline))
        cursor = Cursor.pointer
    }
}