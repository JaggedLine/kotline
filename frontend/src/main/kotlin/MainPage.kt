import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.*
import react.*
import styled.*

external interface MainPageProps : RProps {
    var showGamePageFunc: () -> Unit
    var showRulesPopupFunc: () -> Unit
}

class MainPage : RComponent<MainPageProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                +CommonStyles.container
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
                            +CommonStyles.myButton
                            +CommonStyles.darkGreenButton
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
                            +CommonStyles.linkButton
                            +CommonStyles.hoverUnderline
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