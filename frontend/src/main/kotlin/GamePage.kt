import kotlinx.browser.*
import kotlinx.coroutines.*
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
    var fieldsArray: Array<Field>
    var currentField: Field?
    var won: Boolean
    var score: Int
    var submitting: Boolean
    var submitFailed: Boolean
    var isNameGod: Boolean
    var godMode: Boolean
    var finding: Boolean
}

class GamePage : RComponent<RProps, GamePageState>() {
    init {
        state.chainFieldRef = createRef()
        state.resultsTableRef = createRef()
        state.fieldsArray = emptyArray()
        state.currentField = null
        state.submitting = false
        state.submitFailed = false
        state.isNameGod = false
        state.godMode = false
        state.finding = false
    }

    private suspend fun getFields(): Array<Field> {
        return window.fetch("/getFields")
            .await().json().await().unsafeCast<Array<Json>>()
            .map { it.toField() }.toTypedArray()
    }

    private fun loadFields() {
        MainScope().launch {
            val downloadedFields = getFields()
            setState {
                fieldsArray = downloadedFields
                currentField = fieldsArray.first()
            }
        }
    }

    private fun submitSolution() {
        setState {
            submitting = true
            submitFailed = false
        }
        val submitBody = json().apply {
            this["name"] = (document.getElementById("playerName")
                    as HTMLInputElement).value
            this["field"] = state.currentField!!.toJson()
            this["solution"] = state.chainFieldRef.current.getPolyline()
                .map { it.toJson() }
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
            state.resultsTableRef.current.loadResults()
            state.chainFieldRef.current.clearPolyline()
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

    private fun launchWorker() {
        setState {
            finding = true
        }
        val worker = Worker("worker.js")
        worker.postMessage(state.currentField!!.toJson())
        worker.onmessage = { e ->
            val data = e.data.unsafeCast<Json>()
            if (data["finished"] as Boolean) {
                setState {
                    finding = false
                }
            } else {
                val polylineToDisplay = data["polyline"].unsafeCast<Array<Json>>()
                    .map { it.toPoint() }
                state.chainFieldRef.current.setPolyline(polylineToDisplay)
            }
        }
    }

    override fun componentDidMount() {
        loadFields()
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
                    if (state.currentField != null) {
                        child(ChainField::class) {
                            attrs {
                                field = state.currentField!!
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
                }
                styledDiv {
                    css { +GamePageStyles.rightContainer }
                    if (!state.godMode) {
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
                                                currentField = fieldsArray[index]
                                            }
                                        }
                                    }
                                    state.fieldsArray.forEachIndexed { index, field ->
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
                        if (state.currentField != null) {
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
                                            field = state.currentField!!
                                        }
                                        ref = state.resultsTableRef
                                    }
                                }
                            }
                        }
                    } else {
                        styledDiv {
                            css { +GamePageStyles.godSettingsSection }
                            styledDiv {
                                css { +GamePageStyles.godSettingsRow }
                                styledH3 {
                                    css { +GamePageStyles.godSettingsRowTitle }
                                    +"Field size:"
                                }
                                styledLabel {
                                    css { +GamePageStyles.godSettingsRowLabel }
                                    styledInput {
                                        css {
                                            +CommonStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.sizeX.toString()
                                            id = "sizeX"
                                        }
                                    }
                                }
                                styledDiv {
                                    css { +GamePageStyles.godSettingsRowSplitter }
                                    +"x"
                                }
                                styledLabel {
                                    css { +GamePageStyles.godSettingsRowLabel }
                                    styledInput {
                                        css {
                                            +CommonStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.sizeY.toString()
                                            id = "sizeY"
                                        }
                                    }
                                }
                            }
                            styledDiv {
                                css { +GamePageStyles.godSettingsRow }
                                styledH3 {
                                    css { +GamePageStyles.godSettingsRowTitle }
                                    +"Start point:"
                                }
                                styledLabel {
                                    css { +GamePageStyles.godSettingsRowLabel }
                                    styledInput {
                                        css {
                                            +CommonStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.startPoint.x.toString()
                                            id = "startX"
                                        }
                                    }
                                }
                                styledDiv {
                                    css { +GamePageStyles.godSettingsRowSplitter }
                                    +","
                                }
                                styledLabel {
                                    css { +GamePageStyles.godSettingsRowLabel }
                                    styledInput {
                                        css {
                                            +CommonStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.startPoint.y.toString()
                                            id = "startY"
                                        }
                                    }
                                }
                            }
                            styledDiv {
                                css { +GamePageStyles.godSettingsRow }
                                styledH3 {
                                    css { +GamePageStyles.godSettingsRowTitle }
                                    +"End point:"
                                }
                                styledLabel {
                                    css { +GamePageStyles.godSettingsRowLabel }
                                    styledInput {
                                        css {
                                            +CommonStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.endPoint.x.toString()
                                            id = "endX"
                                        }
                                    }
                                }
                                styledDiv {
                                    css { +GamePageStyles.godSettingsRowSplitter }
                                    +","
                                }
                                styledLabel {
                                    css { +GamePageStyles.godSettingsRowLabel }
                                    styledInput {
                                        css {
                                            +CommonStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.endPoint.y.toString()
                                            id = "endY"
                                        }
                                    }
                                }
                            }
                            styledButton {
                                css {
                                    +CommonStyles.myButton
                                    +CommonStyles.darkGreenButton
                                    +GamePageStyles.godSettingsApply
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
                                            sizeX = newSizeX,
                                            sizeY = newSizeY,
                                            startPoint = Point(
                                                x = newStartX,
                                                y = newStartY
                                            ),
                                            endPoint = Point(
                                                x = newEndX,
                                                y = newEndY
                                            )
                                        )
                                        if (isFieldValid(newField)) {
                                            setState {
                                                currentField = newField
                                            }
                                        }
                                    }
                                }
                                +"Apply"
                            }
                        }
                        styledDiv {
                            css { +GamePageStyles.godComputerSection }
                            styledH3 {
                                css { +GamePageStyles.godComputerScore }
                                +"Current length: ${state.score}"
                            }
                            styledButton {
                                css {
                                    +CommonStyles.myButton
                                    +CommonStyles.darkGreenButton
                                    +GamePageStyles.godComputerStart
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
            }
        }
    }
}