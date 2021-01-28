import kotlinx.browser.*
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.*
import org.w3c.dom.*
import org.w3c.fetch.*
import react.*
import react.dom.*
import styled.*
import kotlin.js.*

external interface GamePageState : RState {
    var chainFieldRef: RReadableRef<ChainField>
    var resultsTableRef: RReadableRef<ResultsTable>
    var fieldList: List<Field>
    var currentField: Field
    var won: Boolean
    var score: Int
    var submitting: Boolean
    var submitFailed: Boolean
    var isNameGod: Boolean
    var godMode: Boolean
}

class GamePage : RComponent<RProps, GamePageState>() {
    init {
        state.chainFieldRef = createRef()
        state.resultsTableRef = createRef()
        state.fieldList = listOf(
            Field(sizeX = 6, sizeY = 6, startPoint = Point(3, 3), endPoint = Point(5, 4)),
            Field(sizeX = 7, sizeY = 7, startPoint = Point(3, 3), endPoint = Point(6, 4)),
            Field(sizeX = 8, sizeY = 8, startPoint = Point(1, 1), endPoint = Point(6, 6))
        ) // TODO: fetch
        state.currentField = state.fieldList.first()
        state.submitting = false
        state.submitFailed = false
        state.isNameGod = false
        state.godMode = false
    }

    private fun submitSolution() {
        setState {
            submitting = true
            submitFailed = false
        }
        val submitBody = json().apply {
            this["name"] = (document.getElementById("playerName")
                    as HTMLInputElement).value
            this["field"] = state.currentField.toJson()
            this["solution"] = state.chainFieldRef.current?.getPolyline()
                ?.map { it.toJson() }
        }
        window.fetch(
            "/submit", RequestInit(
                method = "POST",
                headers = json().apply {
                    this["Content-Type"] = "application/json"
                },
                body = JSON.stringify(submitBody)
            )
        ).then {
            state.resultsTableRef.current?.loadResults()
            state.chainFieldRef.current?.clearPolyline()
            setState {
                submitting = false
                submitFailed = false
            }
        }.catch {
            setState {
                submitting = false
                submitFailed = true
            }
        }
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
                        css { +GamePageStyles.submitSection }
                        styledDiv {
                            css { +GamePageStyles.sizeRow }
                            styledH3 {
                                css { +GamePageStyles.sizeTitle }
                                +"Field size:"
                            }
                            styledSelect {
                                css {
                                    +CommonStyles.mySelect
                                    +CommonStyles.darkGreenButton
                                    +GamePageStyles.sizeValue
                                }
                                attrs {
                                    onChangeFunction = {
                                        val index = (it.target as HTMLSelectElement)
                                            .value.toInt()
                                        setState {
                                            currentField = fieldList[index]
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
                        if (!state.godMode) {
                            label {
                                styledInput {
                                    css {
                                        +CommonStyles.myInput
                                        +GamePageStyles.nameInput
                                    }
                                    attrs {
                                        placeholder = "Enter your name"
                                        maxLength = "20"
                                        id = "playerName"
                                        onChangeFunction = {
                                            if ((it.target as HTMLInputElement).value == "GOD") {
                                                setState {
                                                    isNameGod = true
                                                }
                                            } else {
                                                setState {
                                                    isNameGod = false
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (!state.isNameGod) {
                                styledButton {
                                    css {
                                        +CommonStyles.myButton
                                        +CommonStyles.darkGreenButton
                                        +GamePageStyles.submitButton
                                    }
                                    attrs {
                                        disabled = !state.won || state.submitting
                                        onClickFunction = {
                                            submitSolution()
                                        }
                                    }
                                    when {
                                        state.submitting -> +"Submitting..."
                                        state.submitFailed -> +"Try again"
                                        state.won -> +"Submit score ${state.score}!"
                                        else -> +"Your score is ${state.score}."
                                    }
                                }
                            } else {
                                styledButton {
                                    css {
                                        +CommonStyles.myButton
                                        +CommonStyles.darkGreenButton
                                        +GamePageStyles.enterGodModeButton
                                    }
                                    attrs {
                                        onClickFunction = {
                                            setState {
                                                godMode = true
                                            }
                                        }
                                    }
                                    +"Enter GOD mode"
                                }
                            }
                        }
                    }
                    if (!state.godMode) {
                        styledDiv {
                            css { +CommonStyles.scrollableWrapper }
                            styledDiv {
                                css {
                                    +CommonStyles.scrollable
                                    media("(max-width: 850px)") {
                                        position = Position.relative
                                    }
                                }
                                child(ResultsTable::class) {
                                    attrs {
                                        field = state.currentField
                                    }
                                    ref = state.resultsTableRef
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}