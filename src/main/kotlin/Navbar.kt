import kotlinx.html.js.*
import react.*
import styled.*

external interface NavbarProps : RProps {
    var showMainPageFunc: () -> Unit
    var showRulesPopupFunc: () -> Unit
}

class Navbar : RComponent<NavbarProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css { +NavbarStyles.navbar }
            styledButton {
                css {
                    +CommonStyles.flatButton
                    +NavbarStyles.navbarBrand
                }
                attrs {
                    onClickFunction = {
                        props.showMainPageFunc()
                    }
                }
                +"Jagged line"
            }
            styledButton {
                css {
                    +CommonStyles.flatButton
                    +CommonStyles.hoverUnderlineButton
                    +NavbarStyles.rules
                }
                attrs {
                    onClickFunction = {
                        props.showRulesPopupFunc()
                    }
                }
                +"Rules"
            }
        }
    }
}