import kotlinx.css.*
import kotlinx.html.js.*
import react.*
import react.dom.*
import styled.*

data class Point(val x: Int, val y: Int)

data class Field(var sizeX: Int, var sizeY: Int, var startPoint: Point, var endPoint: Point)

external interface GamePageProps : RProps {
    var fieldOptions: List<Field>
}

external interface GamePageState : RState {
    var field: Field
    var submitStr: String
    var submitEnabled: Boolean
}

class GamePage(props: GamePageProps) : RComponent<GamePageProps, GamePageState>() {
    init {
        state.field = props.fieldOptions.first()
        state.submitStr = "Your score is 0."
        state.submitEnabled = false
    }

    private fun ChainFieldProps.chainFieldStyle() {
        backgroundColor = Color.transparent

        showGrid = true
        gridColor = rgb(48, 86, 88)
        gridWidth = 3
        gridStep = 80

        segmentWidth = 5
        segmentColor = Color.black

        nodeRadius = 8
        clickableNodeRadius = 15
        nodeColor = Color.transparent
        hoverNodeColor = Color.grey
        usedNodeColor = Color.white
        usedNodeBorderColor = Color.black
        usedNodeBorderWidth = 4
        startNodeColor = Color.black
        endNodeColor = Color.black

        deleteColor = rgba(255, 127, 127, 0.9)
    }

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
                    child(ChainField::class) {
                        attrs {
                            chainFieldStyle()
                            field = state.field
                            updateSubmitButton = { str, enabled ->
                                setState {
                                    submitStr = str
                                    submitEnabled = enabled
                                }
                            }
                        }
                    }
                }
                styledDiv {
                    css { +GamePageStyles.rightContainer }
                    styledDiv {
                        css { +GamePageStyles.sizeRow }
                        styledH3 {
                            css { +GamePageStyles.sizeTitle }
                            +"Field size:"
                        }
                        styledSelect {
                            css {

                            }
                            attrs {
                                onChangeFunction = {
                                    console.log(it.target)
                                }
                            }
                            for (fieldOption in props.fieldOptions) {
                                option {
                                    +"${fieldOption.sizeX} x ${fieldOption.sizeY}"
                                }
                            }
                        }
                    }
                    label {
                        styledInput {
                            css {
                                +CommonStyles.myInput
                                +GamePageStyles.nameInput
                            }
                            attrs {
                                placeholder = "Enter your name"
                            }
                        }
                    }
                    styledButton {
                        css {
                            +CommonStyles.myButton
                            +CommonStyles.darkGreenButton
                            +GamePageStyles.submitButton
                        }
                        attrs {
                            disabled = !state.submitEnabled
                        }
                        +state.submitStr
                    }
                }
            }
        }
    }
}