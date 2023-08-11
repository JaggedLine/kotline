import kotlinx.html.*
import kotlinx.html.js.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import react.*
import react.dom.*
import styled.*
import web.dom.*
import web.html.*
import web.workers.*

fun isFieldValid(field: Field): Boolean {
    if (field.size.rows > 15 || field.size.columns > 15) {
        return false
    }
    if (field.start.row < 0 || field.start.row >= field.size.rows) {
        return false
    }
    if (field.start.column < 0 || field.start.column >= field.size.columns) {
        return false
    }
    if (field.end.row < 0 || field.end.row >= field.size.rows) {
        return false
    }
    if (field.end.column < 0 || field.end.column >= field.size.columns) {
        return false
    }
    if (field.start == field.end) {
        return false
    }
    return true
}

external interface GodModeProps : Props {
    var chainFieldRef: RefObject<ChainField>
    var onFieldChangeFunc: (Field) -> Unit
    var field: Field
    var score: Int
}

external interface GodModeState : State {
    var finding: Boolean
}

class GodMode : RComponent<GodModeProps, GodModeState>() {
    init {
        state.finding = false
    }

    private fun launchWorker() {
        setState {
            finding = true
        }
        val worker = Worker("/static/worker.js")
        worker.postMessage(Json.encodeToString(props.field))
        worker.onmessage = { e ->
            val polyline = Json.decodeFromString<WorkerMessage>(e.data.toString()).polyline
            if (polyline == null) {
                setState {
                    finding = false
                }
            } else {
                props.chainFieldRef.current!!.setPolyline(polyline)
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css { +GodModeStyles.settingsSection }
            styledDiv {
                css { +GodModeStyles.settingsRow }
                styledH3 {
                    css { +GodModeStyles.settingsRowTitle }
                    +"Field size:"
                }
                styledLabel {
                    css { +GodModeStyles.settingsRowLabel }
                    styledInput {
                        css {
                            +BaseStyles.myInput
                            +GodModeStyles.settingsRowInput
                        }
                        attrs {
                            defaultValue = props.field.size.rows.toString()
                            id = "sizeX"
                        }
                    }
                }
                styledDiv {
                    css { +GodModeStyles.settingsRowSplitter }
                    +"x"
                }
                styledLabel {
                    css { +GodModeStyles.settingsRowLabel }
                    styledInput {
                        css {
                            +BaseStyles.myInput
                            +GodModeStyles.settingsRowInput
                        }
                        attrs {
                            defaultValue = props.field.size.columns.toString()
                            id = "sizeY"
                        }
                    }
                }
            }
            styledDiv {
                css { +GodModeStyles.settingsRow }
                styledH3 {
                    css { +GodModeStyles.settingsRowTitle }
                    +"Start point:"
                }
                styledLabel {
                    css { +GodModeStyles.settingsRowLabel }
                    styledInput {
                        css {
                            +BaseStyles.myInput
                            +GodModeStyles.settingsRowInput
                        }
                        attrs {
                            defaultValue = props.field.start.row.toString()
                            id = "startX"
                        }
                    }
                }
                styledDiv {
                    css { +GodModeStyles.settingsRowSplitter }
                    +","
                }
                styledLabel {
                    css { +GodModeStyles.settingsRowLabel }
                    styledInput {
                        css {
                            +BaseStyles.myInput
                            +GodModeStyles.settingsRowInput
                        }
                        attrs {
                            defaultValue = props.field.start.column.toString()
                            id = "startY"
                        }
                    }
                }
            }
            styledDiv {
                css { +GodModeStyles.settingsRow }
                styledH3 {
                    css { +GodModeStyles.settingsRowTitle }
                    +"End point:"
                }
                styledLabel {
                    css { +GodModeStyles.settingsRowLabel }
                    styledInput {
                        css {
                            +BaseStyles.myInput
                            +GodModeStyles.settingsRowInput
                        }
                        attrs {
                            defaultValue = props.field.end.row.toString()
                            id = "endX"
                        }
                    }
                }
                styledDiv {
                    css { +GodModeStyles.settingsRowSplitter }
                    +","
                }
                styledLabel {
                    css { +GodModeStyles.settingsRowLabel }
                    styledInput {
                        css {
                            +BaseStyles.myInput
                            +GodModeStyles.settingsRowInput
                        }
                        attrs {
                            defaultValue = props.field.end.column.toString()
                            id = "endY"
                        }
                    }
                }
            }
            styledButton {
                css {
                    +BaseStyles.myButton
                    +BaseStyles.darkGreenButton
                    +GodModeStyles.settingsApply
                }
                attrs {
                    disabled = state.finding
                    onClickFunction = {
                        val newSizeX = (document.getElementById("sizeX")
                                as HTMLInputElement).value.toInt()
                        val newSizeY = (document.getElementById("sizeY")
                                as HTMLInputElement).value.toInt()
                        val newStartX = (document.getElementById("startX")
                                as HTMLInputElement).value.toInt()
                        val newStartY = (document.getElementById("startY")
                                as HTMLInputElement).value.toInt()
                        val newEndX = (document.getElementById("endX")
                                as HTMLInputElement).value.toInt()
                        val newEndY = (document.getElementById("endY")
                                as HTMLInputElement).value.toInt()
                        val newField = Field(
                            size = FieldSize(newSizeX, newSizeY),
                            start = Coords(newStartX, newStartY),
                            end = Coords(newEndX, newEndY)
                        )
                        if (isFieldValid(newField)) {
                            props.onFieldChangeFunc(newField)
                        }
                    }
                }
                +"Apply"
            }
        }
        styledDiv {
            css { +GodModeStyles.computerSection }
            styledH3 {
                css { +GodModeStyles.computerScore }
                +"Current length: ${props.score}"
            }
            styledButton {
                css {
                    +BaseStyles.myButton
                    +BaseStyles.darkGreenButton
                    +GodModeStyles.computerStart
                }
                attrs {
                    disabled = state.finding
                    onClickFunction = {
                        launchWorker()
                    }
                }
                when {
                    state.finding -> +"Finding..."
                    else -> +"Find the best solution!"
                }
            }
        }
    }
}
