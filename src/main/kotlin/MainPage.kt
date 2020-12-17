import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import styled.*
import styled.css

external interface MainPageProps: RProps {
    var showGamePageFunc: (Int) -> Unit
    var fieldProps: List<Pair<Int, RuleSet>>
}

class MainPage: RComponent<MainPageProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css {
                +CommonStyles.container
            }
            styledDiv {
                css {
                    margin(50.px)
                    textAlign = TextAlign.center
                }
                styledH1 {
                    css {
                        color = Color.black
                        fontSize = 50.px
                    }
                    +"Jagged line"
                }
                styledH2 {
                    css {
                        color = Color.grey
                        fontSize = 30.px
                    }
                    +"Geometric puzzle of different levels"
                }
            }
            styledDiv {
                css {
                    display = Display.flex
                    flexWrap = FlexWrap.wrap
                    margin(0.px, LinearDimension.auto)
                    maxWidth = 700.px
                }
                for ((size, ruleSet) in props.fieldProps) {
                    styledButton {
                        css {
                            +MainPageStyles.tile
                            +ruleSet
                        }
                        attrs {
                            onClickFunction = {
                                props.showGamePageFunc(size)
                            }
                        }
                        +"$size x $size"
                    }
                }
                styledButton {
                    css {
                        +MainPageStyles.tile
                        +CommonStyles.greyButton
                    }
                    attrs {
                        onClickFunction = {

                        }
                    }
                    +"Other"
                }
            }
        }
    }
}
