import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.*
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import org.w3c.dom.*
import react.*
import react.dom.*
import styled.*

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

external interface GamePageState : State {
    var client: HttpClient
    var chainFieldRef: RefObject<ChainField>
    var resultsTableRef: RefObject<ResultsTable>
    var fieldsArray: Array<Field>
    var currentField: Field?
    var name: String
    var won: Boolean
    var score: Int
    var submitting: Boolean
    var submitFailed: Boolean
    var godMode: Boolean
    var finding: Boolean
}

class GamePage : RComponent<Props, GamePageState>() {
    init {
        state.client = HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }
        state.chainFieldRef = createRef()
        state.resultsTableRef = createRef()
        state.fieldsArray = emptyArray()
        state.currentField = null
        state.name = ""
        state.submitting = false
        state.submitFailed = false
        state.godMode = false
        state.finding = false
    }

    private suspend fun getFields(): Array<Field> {
        return state.client.get("/getFields").body()
    }

    private suspend fun submitSolution() {
        setState {
            submitting = true
            submitFailed = false
        }
        val submission = Submission(
            state.name,
            state.currentField!!,
            state.chainFieldRef.current!!.getPolyline()
        )
        try {
            state.client.post("/submit") {
                contentType(ContentType.Application.Json)
                setBody(submission)
            }
        } catch (e: Throwable) {
            setState {
                submitting = false
                submitFailed = true
            }
            return
        }
        setState {
            submitting = false
            submitFailed = false
        }
        state.resultsTableRef.current!!.loadResults()
        state.chainFieldRef.current!!.clearPolyline()
    }

    private fun launchWorker() {
        setState {
            finding = true
        }
        val worker = Worker("/static/worker.js")
        worker.postMessage(Json.encodeToString(state.currentField!!))
        worker.onmessage = { e ->
            val polyline = Json.decodeFromString<WorkerMessage>(e.data.toString()).polyline
            if (polyline == null) {
                setState {
                    finding = false
                }
            } else {
                state.chainFieldRef.current!!.setPolyline(polyline)
            }
        }
    }

    override fun componentDidMount() {
        MainScope().launch {
            val downloadedFields = getFields()
            setState {
                fieldsArray = downloadedFields
                currentField = fieldsArray.first()
            }
        }
    }

    override fun RBuilder.render() {
        styledDiv {
            css { +BaseStyles.container }
            styledDiv {
                css {
                    +BaseStyles.container
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
                                        won = polyline.last() == field.end
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
                                        +BaseStyles.mySelect
                                        +BaseStyles.darkGreenButton
                                        +GamePageStyles.sizeValue
                                    }
                                    attrs {
                                        onChangeFunction = {
                                            val index = (it.target as HTMLSelectElement).value.toInt()
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
                                            +"${field.size.rows} x ${field.size.columns}"
                                        }
                                    }
                                }
                            }
                            label {
                                styledInput {
                                    css {
                                        +BaseStyles.myInput
                                        +GamePageStyles.nameInput
                                    }
                                    attrs {
                                        placeholder = "Enter your name"
                                        maxLength = "20"
                                        id = "playerName"
                                        onChangeFunction = {
                                            setState {
                                                name = (it.target as HTMLInputElement).value
                                            }
                                        }
                                    }
                                }
                            }
                            if (state.name != "GOD") {
                                styledButton {
                                    css {
                                        +BaseStyles.myButton
                                        +BaseStyles.darkGreenButton
                                        +GamePageStyles.submitButton
                                    }
                                    attrs {
                                        disabled = !state.won || state.submitting
                                        onClickFunction = {
                                            MainScope().launch {
                                                submitSolution()
                                            }
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
                                        +BaseStyles.myButton
                                        +BaseStyles.darkGreenButton
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
                                css { +BaseStyles.scrollableWrapper }
                                styledDiv {
                                    css {
                                        +BaseStyles.scrollable
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
                                            +BaseStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.size.rows.toString()
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
                                            +BaseStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.size.columns.toString()
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
                                            +BaseStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.start.row.toString()
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
                                            +BaseStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.start.column.toString()
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
                                            +BaseStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.end.row.toString()
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
                                            +BaseStyles.myInput
                                            +GamePageStyles.godSettingsRowInput
                                        }
                                        attrs {
                                            defaultValue = state.currentField!!.end.column.toString()
                                            id = "endY"
                                        }
                                    }
                                }
                            }
                            styledButton {
                                css {
                                    +BaseStyles.myButton
                                    +BaseStyles.darkGreenButton
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
                                            size = FieldSize(newSizeX, newSizeY),
                                            start = Coords(newStartX, newStartY),
                                            end = Coords(newEndX, newEndY)
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
                                    +BaseStyles.myButton
                                    +BaseStyles.darkGreenButton
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
