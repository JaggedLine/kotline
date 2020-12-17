import kotlinx.css.*
import styled.StyleSheet

object MainPageStyles: StyleSheet("MainPageStyles", isStatic = true) {
    val tile by css {
        +CommonStyles.myButton
        flexGrow = 1.0
        minHeight = 150.px
        minWidth = 200.px
        margin(10.px)
        fontSize = 40.px
        flexBasis = FlexBasis("0")
    }
}