import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import styled.*
import styled.css

external interface MainPageProps: RProps {
    var setPage: (Pages) -> Unit
    var fieldProps: List<Pair<Int, RuleSet>>
}

class MainPage: RComponent<MainPageProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            styledDiv {
                css {
                    margin(50.px)
                    textAlign = TextAlign.center
                }
                styledH1 {
                    css {
                        +CommonStyles.myH1
                    }
                    +"Jagged line"
                }
                styledH2 {
                    css {
                        +CommonStyles.myH2
                    }
                    +"Build the longest polygonal chain"
                }
            }
            styledDiv {
                css {
                    display = Display.flex
                    justifyContent = JustifyContent.center
                    margin(50.px)
                }
                for ((size, ruleSet) in props.fieldProps) {
                    styledButton {
                        css {
                            +CommonStyles.myButton
                            +ruleSet
                            margin(30.px)
                            height = 250.px
                            width = 350.px
                            fontSize = 50.px
                        }
                        attrs {
                            onClickFunction = {
                                props.setPage(Pages.GAME_PAGE)
                            }
                        }
                        +"$size x $size"
                    }
                }
            }
        }
    }
}
