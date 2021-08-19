import kotlinx.css.*
import styled.StyleSheet

object NavbarStyles : StyleSheet("NavbarStyles", isStatic = true) {
    val navbar by css {
        display = Display.flex
        flexDirection = FlexDirection.row
        padding(15.px, 20.px)
        backgroundColor = rgb(48, 86, 88)
    }
    val navbarBrand by css {
        marginRight = LinearDimension.auto
        color = Color.white
        fontSize = 30.px
        fontWeight = FontWeight.bold
    }
    val navbarLink by css {
        marginLeft = 20.px
        color = Color.lightGrey
        fontSize = 18.px
        fontWeight = FontWeight.bold
    }
}