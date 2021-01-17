import kotlinx.css.*
import styled.StyleSheet

object MainPageStyles : StyleSheet("MainPageStyles", isStatic = true) {
    val container by css {
        flexDirection = FlexDirection.row
        justifyContent = JustifyContent.center
        backgroundColor = rgb(237, 255, 250)
    }
    val content by css {
        flexGrow = 1.0
        maxWidth = 700.px
        padding(50.px, 30.px)
        fontSize = 25.px
    }
    val gameTitle by css {
        fontWeight = FontWeight.normal
        textAlign = TextAlign.center
    }
    val gameParagraph by css {
        color = Color.grey
        textAlign = TextAlign.justify
    }
    val actions by css {
        display = Display.flex
        flexDirection = FlexDirection.row
        justifyContent = JustifyContent.center
        marginTop = 50.px
    }
    val playButton by css {
        marginRight = 20.px
        fontSize = 1.em
    }
    val rulesButton by css {
        color = rgb(48, 86, 88)
        fontSize = 1.em
    }
}