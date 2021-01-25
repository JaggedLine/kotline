import kotlinx.css.*
import styled.StyleSheet

object RulesPopupStyles : StyleSheet("RulesPopupStyles", isStatic = true) {
    val header by css {
        display = Display.flex
        flexDirection = FlexDirection.row
        padding(15.px)
        borderBottom = "1px solid whitesmoke"
    }
    val rulesLabel by css {
        margin(0.px, LinearDimension.auto, 0.px, 5.px)
        fontWeight = FontWeight.normal
    }
    val closeButton by css {
        height = 45.px
        width = 45.px
        fontSize = 16.px
    }
    val content by css {
        flexGrow = 1.0
        padding(15.px)
        fontSize = 20.px
        overflow = Overflow.auto
        p {
            marginTop = 0.px
        }
    }
}