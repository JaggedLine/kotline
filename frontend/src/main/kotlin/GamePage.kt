import react.*
import styled.*

class GamePage : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        styledDiv {
            css { +CommonStyles.container }
            styledDiv {
                css {
                    +CommonStyles.container
                    +GamePageStyles.pageLayout
                }
                styledDiv {
                    css { +GamePageStyles.fieldContainer }
                }
                styledDiv {
                    css { +GamePageStyles.rightContainer }
                    styledDiv {
                        css { +GamePageStyles.sizeRow }
                        styledH3 {
                            css { +GamePageStyles.sizeTitle }
                            +"Field size:"
                        }
                        styledH3 {
                            css { +GamePageStyles.sizeValue }
                            +"6 x 6"
                        }
                    }
                    styledDiv {
                        css { +GamePageStyles.scoreRow }
                        styledH3 {
                            css { +GamePageStyles.scoreTitle }
                            +"Your score:"
                        }
                        styledH3 {
                            css { +GamePageStyles.scoreValue }
                            +"10"
                        }
                    }
                    styledInput {
                        css {
                            +GamePageStyles.nameInput
                        }
                        attrs {
                            placeholder = "Enter your name"
                        }
                    }
                    styledButton {
                        css {
                            +CommonStyles.myButton
                            +CommonStyles.darkGreenButton
                            +GamePageStyles.submitButton
                        }
                        +"Submit"
                    }
                }
            }
        }
    }
}