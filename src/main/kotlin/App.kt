import react.*

enum class Pages {
    MAIN_PAGE,
    GAME_PAGE
}

external interface AppState: RState {
    var page: Pages
}

class App : RComponent<RProps, AppState>() {
    init {
        state.page = Pages.MAIN_PAGE
    }

    override fun RBuilder.render() {
        when (state.page) {
            Pages.MAIN_PAGE -> child(MainPage::class) {
                attrs.setPage = {
                    newPage ->
                    setState {
                        page = newPage
                    }
                }
            }
            Pages.GAME_PAGE -> child(GamePage::class) {
                attrs.setPage = {
                        newPage ->
                    setState {
                        page = newPage
                    }
                }
            }
        }
    }
}

