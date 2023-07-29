import kotlinx.css.*
import styled.StyleSheet

object NavbarStyles : StyleSheet("NavbarStyles", isStatic = true) {
    val navbar by css {
        backgroundColor = rgb(48, 86, 88)
        display = Display.flex
        flexDirection = FlexDirection.row
        padding = Padding(15.px, 20.px)
    }

    val navbarBrand by css {
        color = Color.white
        fontSize = 30.px
        fontWeight = FontWeight.bold
        marginRight = LinearDimension.auto
    }

    val navbarLink by css {
        color = Color.lightGrey
        fontSize = 18.px
        fontWeight = FontWeight.bold
        marginLeft = 20.px
    }
}
