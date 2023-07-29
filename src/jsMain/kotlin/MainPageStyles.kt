import kotlinx.css.*
import styled.StyleSheet

object MainPageStyles : StyleSheet("MainPageStyles", isStatic = true) {
    val container by css {
        backgroundColor = rgb(237, 255, 250)
        flexDirection = FlexDirection.row
        justifyContent = JustifyContent.center
    }

    val content by css {
        flexGrow = 1.0
        fontSize = 25.px
        maxWidth = 700.px
        padding = Padding(50.px, 30.px)
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
        fontSize = 1.em
        marginRight = 20.px
    }

    val rulesButton by css {
        color = rgb(48, 86, 88)
        fontSize = 1.em
    }
}
