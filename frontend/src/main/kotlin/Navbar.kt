import kotlinx.html.js.*
import react.*
import styled.*

external interface NavbarProps : RProps {
    var showMainPageFunc: () -> Unit
    var showAboutPopupFunc: () -> Unit
    var showRulesPopupFunc: () -> Unit
}

class Navbar : RComponent<NavbarProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css { +NavbarStyles.navbar }
            styledButton {
                css {
                    +CommonStyles.linkButton
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
                    +CommonStyles.linkButton
                    +CommonStyles.hoverUnderline
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
                    +CommonStyles.linkButton
                    +CommonStyles.hoverUnderline
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