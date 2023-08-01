import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.html.js.*
import org.w3c.dom.*
import react.*
import react.dom.*
import styled.*

external interface GamePageState : State {
    var client: HttpClient
    var chainFieldRef: RefObject<ChainField>
    var resultsTableRef: RefObject<ResultsTable>
    var fieldsArray: Array<Field>
    var field: Field?
    var name: String
    var won: Boolean
    var score: Int
    var submitting: Boolean
    var submitFailed: Boolean
    var godMode: Boolean
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
        state.field = null
        state.name = ""
        state.submitting = false
        state.submitFailed = false
        state.godMode = false
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
            state.field!!,
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

    override fun componentDidMount() {
        MainScope().launch {
            val downloadedFields = getFields()
            setState {
                fieldsArray = downloadedFields
                field = fieldsArray.first()
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
                    if (state.field != null) {
                        child(ChainField::class) {
                            attrs {
                                field = state.field!!
                                onPolylineChange = { polyline ->
                                    setState {
                                        won = polyline.last() == field!!.end
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
                                                field = fieldsArray[index]
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
                        if (state.field != null) {
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
                                            field = state.field!!
                                        }
                                        ref = state.resultsTableRef
                                    }
                                }
                            }
                        }
                    } else {
                        child(GodMode::class) {
                            attrs {
                                chainFieldRef = state.chainFieldRef
                                onFieldChangeFunc = { newField ->
                                    setState {
                                        field = newField
                                    }
                                }
                                field = state.field!!
                                score = state.score
                            }
                        }
                    }
                }
            }
        }
    }
}
