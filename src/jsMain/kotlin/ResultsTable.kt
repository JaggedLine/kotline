import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import react.dom.*
import styled.*

external interface ResultsTableProps : Props {
    var field: Field
}

external interface ResultsTableState : State {
    var results: Results
}

class ResultsTable : RComponent<ResultsTableProps, ResultsTableState>() {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    init {
        state.results = Results(emptyList())
    }

    private suspend fun getResults(): Results {
        val url = "/getResults" +
                "?rows=${props.field.size.rows}" +
                "&columns=${props.field.size.columns}" +
                "&startRow=${props.field.start.row}" +
                "&startColumn=${props.field.start.column}" +
                "&endRow=${props.field.end.row}" +
                "&endColumn=${props.field.end.column}"
        return client.get(url).body()
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
                padding = Padding(0.px, 30.px, 30.px, 30.px)
                width = 100.pct
                textAlign = TextAlign.center
            }
            thead {
                tr {
                    styledTh {
                        css {
                            padding = Padding(10.px)
                            width = 80.pct
                        }
                        +"Players"
                    }
                    styledTh {
                        css {
                            padding = Padding(10.px)
                            width = 20.pct
                        }
                        +"Score"
                    }
                }
            }
            tbody {
                for (result in state.results.entries) {
                    tr {
                        styledTd {
                            css {
                                padding = Padding(5.px)
                                width = 80.pct
                            }
                            +result.names.toList().reversed().take(3)
                                .joinToString(separator = "; ")
                            if (result.names.size > 3) {
                                +"; ..."
                            }
                        }
                        styledTd {
                            css {
                                padding = Padding(5.px)
                                width = 20.pct
                            }
                            +"${result.score}"
                        }
                    }
                }
            }
        }
    }
}
