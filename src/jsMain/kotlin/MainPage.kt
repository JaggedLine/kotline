import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.*
import react.*
import react.dom.*
import styled.*

external interface MainPageProps : Props {
    var showGamePageFunc: () -> Unit
    var showRulesPopupFunc: () -> Unit
}

class MainPage : RComponent<MainPageProps, State>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                +BaseStyles.container
                +MainPageStyles.container
            }
            styledDiv {
                css { +MainPageStyles.content }
                styledH2 {
                    css { +MainPageStyles.gameTitle }
                    +"Geometric puzzle of different levels"
                }
                styledP {
                    css { +MainPageStyles.gameParagraph }
                    +"""Construct the longest non-self-intersecting polyline
                        |whose vertices are nodes of the grid, and each edge
                        |is a knight move. Try to get the highest score!""".trimMargin()
                }
                styledDiv {
                    css { +MainPageStyles.actions }
                    styledButton {
                        css {
                            +BaseStyles.myButton
                            +BaseStyles.darkGreenButton
                            +MainPageStyles.playButton
                        }
                        +"Play now"
                        styledSpan {
                            css {
                                marginLeft = 15.px
                            }
                            attrs {
                                classes = setOf("fas", "fa-chevron-right")
                            }
                        }
                        attrs {
                            onClickFunction = {
                                props.showGamePageFunc()
                            }
                        }
                    }
                    styledButton {
                        css {
                            +BaseStyles.linkButton
                            +BaseStyles.hoverUnderline
                            +MainPageStyles.rulesButton
                        }
                        attrs {
                            onClickFunction = {
                                props.showRulesPopupFunc()
                            }
                        }
                        +"View rules"
                    }
                }
            }
        }
    }
}
