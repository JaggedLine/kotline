import kotlinx.css.*
import kotlinx.html.classes
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*
import styled.*
import styled.css

external interface GamePageProps: RProps {
    var showPageFunc: (Pages) -> Unit
    var fieldSize: Int
}

class GamePage: RComponent<GamePageProps, RState>() {
    override fun RBuilder.render() {
        styledButton {
            css {
                +CommonStyles.transparentButton
                +CommonStyles.myButton
                display = Display.flex
                position = Position.absolute
                left = 20.px
                top = 20.px
                width = 60.px
                height = 60.px
                borderRadius = 50.pc
                fontSize = 20.px
            }
            styledSpan {
                css {
                    margin(LinearDimension.auto)
                }
                attrs {
                    classes = setOf("fas", "fa-arrow-left")
                }
            }
            attrs {
                onClickFunction = {
                    props.showPageFunc(Pages.MAIN_PAGE)
                }
            }
        }
        styledDiv {
            css {
                +CommonStyles.container
            }
            styledDiv {
                css {
                    display = Display.flex
                    margin(LinearDimension.auto)
                }
                styledDiv {
                    css {
                        backgroundImage = Image("url(pic.png)")
                        backgroundPosition = "0"
                        backgroundSize = "100%"
                        width = 500.px
                        height = 500.px
                        margin(20.px)
                    }
                }
                styledDiv {
                    css {
                        display = Display.flex
                        flexDirection = FlexDirection.column
                        margin(20.px)
                    }
                    styledDiv {
                        css {
                            width = 300.px
                        }

                        styledH3 {
                            +"""Construct the longest non-self-intersecting polyline
                            |whose vertices are nodes of the grid and the length
                            |of each edge is knight move.""".trimMargin()
                        }
                    }
                    styledInput {
                        css {
                            padding(10.px)
                            fontSize = 12.px
                            borderWidth = 1.px
                            focus {
                                borderWidth = 3.px
                            }
                            marginTop = 10.px
                        }
                    }
                    styledButton {
                        css {
                            +CommonStyles.myButton
                            +CommonStyles.orangeButton
                            height = 40.px
                            marginTop = 30.px
                        }

                        +"Submit"
                    }

                    styledDiv {
                        css {
                            display = Display.flex
                            justifyContent = JustifyContent.spaceBetween
                            marginTop = 20.px
                        }

                        span {
                            +"Your score: 42"
                        }

                        styledButton {
                            css {
                                +CommonStyles.linkButton
                            }
                            +"Show results"
                        }
                    }
                }
            }
        }
    }
}
