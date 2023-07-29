import kotlinx.html.js.*
import react.*
import react.dom.*
import styled.*

external interface NavbarProps : Props {
    var showMainPageFunc: () -> Unit
    var showAboutPopupFunc: () -> Unit
    var showRulesPopupFunc: () -> Unit
}

class Navbar : RComponent<NavbarProps, State>() {
    override fun RBuilder.render() {
        styledDiv {
            css { +NavbarStyles.navbar }
            styledButton {
                css {
                    +BaseStyles.linkButton
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
                    +BaseStyles.linkButton
                    +BaseStyles.hoverUnderline
                    +NavbarStyles.navbarLink
                }
                attrs {
                    onClickFunction = {
                        props.showAboutPopupFunc()
                    }
                }
                +"About"
            }
            styledButton {
                css {
                    +BaseStyles.linkButton
                    +BaseStyles.hoverUnderline
                    +NavbarStyles.navbarLink
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
