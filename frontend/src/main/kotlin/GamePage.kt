import kotlinx.html.js.*
import org.w3c.dom.*
import react.*
import react.dom.*
import styled.*

external interface GamePageState : RState {
    var chainFieldRef: RReadableRef<ChainField>
    var fieldList: List<Field>
    var currentField: Field
    var won: Boolean
    var score: Int
}

class GamePage : RComponent<RProps, GamePageState>() {
    init {
        state.chainFieldRef = createRef()
        state.fieldList = listOf(
            Field(sizeX = 6, sizeY = 6, startPoint = Point(3, 3), endPoint = Point(5, 4)),
            Field(sizeX = 7, sizeY = 7, startPoint = Point(3, 3), endPoint = Point(6, 4)),
            Field(sizeX = 8, sizeY = 8, startPoint = Point(3, 3), endPoint = Point(6, 4)),
            Field(sizeX = 10, sizeY = 10, startPoint = Point(3, 3), endPoint = Point(6, 4))
        ) // TODO: fetch
        state.currentField = state.fieldList.first()
        state.won = false
        state.score = 0
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
                            field = state.currentField
                            onPolylineChange = { polyline ->
                                setState {
                                    won = polyline.last() == field.endPoint
                                    score = polyline.size - 1
                                }
                            }
                            chainFieldStyle()
                        }
                        ref = state.chainFieldRef
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
                                    val index = (it.target as HTMLSelectElement).value.toInt()
                                    setState {
                                        currentField = fieldList[index]
                                        chainFieldRef.current?.clearPolyline()
                                    }
                                }
                            }
                            state.fieldList.forEachIndexed { index, field ->
                                option {
                                    attrs {
                                        value = "$index"
                                    }
                                    +"${field.sizeX} x ${field.sizeY}"
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
                            disabled = !state.won
                        }
                        if (state.won) {
                            +"Submit score ${state.score}!"
                        } else {
                            +"Your score is ${state.score}."
                        }
                    }
                }
            }
        }
    }
}