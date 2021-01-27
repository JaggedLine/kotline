import kotlinx.browser.*
import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import react.dom.*
import styled.*

class Result(val score: Int, val playerNames: Array<String>)

external interface ResultsTableProps : RProps {
    var field: Field
}

external interface ResultsTableState : RState {
    var results: Array<Result>
}

class ResultsTable : RComponent<ResultsTableProps, ResultsTableState>() {
    init {
        state.results = emptyArray()
    }

    private suspend fun getResults(): Array<Result> {
        val queryString = props.field.toQueryString()
        return window.fetch("/getResults?$queryString")
            .await().json().await().asDynamic()["results"]
            .unsafeCast<Array<Result>>()
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
                padding(0.px, 30.px, 30.px, 30.px)
                width = 100.pct
                textAlign = TextAlign.center
            }
            thead {
                tr {
                    styledTh {
                        css {
                            padding(10.px)
                            width = 80.pct
                        }
                        +"Players"
                    }
                    styledTh {
                        css {
                            padding(10.px)
                            width = 20.pct
                        }
                        +"Score"
                    }
                }
            }
            tbody {
                for (result in state.results) {
                    tr {
                        styledTd {
                            css {
                                padding(5.px)
                                width = 80.pct
                            }
                            +result.playerNames.toList().reversed().take(3)
                                .joinToString(separator = "; ")
                            if (result.playerNames.size > 3) {
                                +"; ..."
                            }
                        }
                        styledTd {
                            css {
                                padding(5.px)
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