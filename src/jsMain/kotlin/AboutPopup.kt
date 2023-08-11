import kotlinx.html.js.*
import react.*
import react.dom.*
import styled.*

external interface AboutPopupProps : Props {
    var close: () -> Unit
}

class AboutPopup : RComponent<AboutPopupProps, State>() {
    override fun RBuilder.render() {
        styledDiv {
            css { +PopupStyles.wrapper }
            styledDiv {
                css { +PopupStyles.popup }
                styledDiv {
                    css { +PopupStyles.header }
                    styledH1 {
                        css { +PopupStyles.label }
                        +"About this site"
                    }
                    styledButton {
                        css {
                            +BaseStyles.iconButton
                            +BaseStyles.transparentButton
                            +PopupStyles.close
                        }
                        span("fas fa-times") {}
                        attrs {
                            onClickFunction = {
                                props.close()
                            }
                        }
                    }
                }
                styledDiv {
                    css { +PopupStyles.content }
                    p {
                        +"""The site was created in January 2021 by two first-year students of the 
                            Higher School of Economics:
                        """.trimIndent()
                    }
                    ul {
                        li { +"Fedor Kuyanov" }
                        li { +"Semyon Savkin" }
                    }
                    p {
                        +"""Originally, JaggedLine was a school project at School #57.
                            Inspired by this game, we rewrote it from scratch in the Kotlin language,
                            giving it a fresh new look. Check out our 
                        """.trimIndent()
                        a {
                            +"repository"
                            attrs { href = "https://github.com/JaggedLine/kotline" }
                        }
                        +" on GitHub. If you notice any bugs, please leave an issue."
                    }
                    p {
                        b { +"P.S. " }
                        +"There is also a God Mode in this game!"
                    }
                }
            }
        }
    }
}
