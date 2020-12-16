import kotlinx.css.*
import kotlinx.css.properties.*
import styled.StyleSheet

object CommonStyles : StyleSheet("CommonStyles", isStatic = true) {
    val greenButton by css {
        backgroundColor = Color.lightGreen
        hover {
            backgroundColor = rgb(124, 228, 124)
        }
        active {
            backgroundColor = rgb(104, 214, 104)
        }
    }
    val blueButton by css {
        backgroundColor = Color.lightBlue
        hover {
            backgroundColor = rgb(149, 208, 228)
        }
        active {
            backgroundColor = rgb(125, 188, 209)
        }
    }
    val pinkButton by css {
        backgroundColor = Color.lightPink
        hover {
            backgroundColor = rgb(255, 159, 174)
        }
        active {
            backgroundColor = rgb(255, 138, 155)
        }
    }
    val myButton by css {
        fontFamily = "inherit"
        border(2.px, BorderStyle.solid, rgba(0, 0, 0, 0.1))
        borderRadius = 20.px
        color = Color.black
        hover {
            boxShadow(Color.lightGrey, 2.px, 2.px, 10.px)
        }
    }
    val myH1 by css {
        fontSize = 50.px
    }
    val myH2 by css {
        fontSize = 30.px
    }
}