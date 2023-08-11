import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.html.js.*
import react.*
import react.dom.*
import styled.*
import web.dom.*
import web.html.*

external interface GamePageState : State {
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
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }
    private val chainFieldRef = createRef<ChainField>()
    private val resultsTableRef = createRef<ResultsTable>()

    init {
        state.fieldsArray = emptyArray()
        state.field = null
        state.name = ""
        state.submitting = false
        state.submitFailed = false
        state.godMode = false
    }

    private suspend fun getFields(): Array<Field> {
        return client.get("/getFields").body()
    }

    private suspend fun submitSolution() {
        setState {
            submitting = true
            submitFailed = false
        }
        val submission = Submission(
            state.name,
            state.field!!,
            chainFieldRef.current!!.getPolyline()
        )
        try {
            client.post("/submit") {
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
        resultsTableRef.current!!.loadResults()
        chainFieldRef.current!!.clearPolyline()
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
                            ref = chainFieldRef
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
                                        id = "field-size"
                                        onChangeFunction = {
                                            val index = (document.getElementById("field-size")
                                                    as HTMLSelectElement).value.toInt()
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
                                        id = "your-name"
                                        onChangeFunction = {
                                            setState {
                                                name = (document.getElementById("your-name")
                                                        as HTMLInputElement).value
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
                                        ref = resultsTableRef
                                    }
                                }
                            }
                        }
                    } else {
                        child(GodMode::class) {
                            attrs {
                                chainFieldRef = this@GamePage.chainFieldRef
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
