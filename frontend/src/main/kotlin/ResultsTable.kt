import kotlinx.browser.*
import kotlinx.coroutines.*
import kotlinx.css.*
import org.w3c.dom.url.*
import react.*
import react.dom.*
import styled.*

data class Result(val score: Int, val playerNames: List<String>)

external interface ResultsTableProps : RProps {
    var field: Field
}

external interface ResultsTableState : RState {
    var results: List<Result>
}

class ResultsTable : RComponent<ResultsTableProps, ResultsTableState>() {
    init {
        state.results = emptyList()
    }

    private suspend fun getResults(): List<Result> {
        console.log("get results ${props.field}")
        val params = URLSearchParams().apply {
            append("field", JSON.stringify(props.field))
        }
//        return window.fetch("/getResults?$params")
//            .await().json().await().unsafeCast<List<Result>>()
        return listOf(
            Result(score = 17, playerNames = listOf("God")),
            Result(score = 11, playerNames = listOf("Cheater", "Naughty boy")),
            Result(score = 9, playerNames = listOf("Vasya", "Fedya", "Semen")),
            Result(score = 7, playerNames = listOf("Besobrazie", "Kekos")),
            Result(score = 5, playerNames = listOf("Someone"))
        )
    }

    fun loadResults() {
        MainScope().launch {
            val downloadedResults = getResults()
            setState { results = downloadedResults }
        }
    }

    override fun componentDidMount() {
        loadResults()
    }

    override fun componentDidUpdate(prevProps: ResultsTableProps, prevState: ResultsTableState, snapshot: Any) {
        if (props.field != prevProps.field) {
            loadResults()
        }
    }

    override fun RBuilder.render() {
        styledTable {
            css {
                marginTop = 30.px
                textAlign = TextAlign.center
            }
            thead {
                tr {
                    styledTh {
                        css { padding(10.px) }
                        +"Players"
                    }
                    styledTh {
                        css { padding(10.px) }
                        +"Score"
                    }
                }
            }
            tbody {
                for (result in state.results) {
                    tr {
                        styledTd {
                            css { padding(5.px) }
                            +result.playerNames.joinToString(separator = ", ")
                        }
                        styledTd {
                            css { padding(5.px) }
                            +"${result.score}"
                        }
                    }
                }
            }
        }
    }
}