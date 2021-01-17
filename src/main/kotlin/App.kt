import react.*

enum class Pages {
    MAIN_PAGE,
    GAME_PAGE
}

external interface AppState : RState {
    var page: Pages
}

class App : RComponent<RProps, AppState>() {
    init {
        state.page = Pages.MAIN_PAGE
    }

    private fun showPage(newPage: Pages) {
        setState {
            page = newPage
        }
    }

    override fun RBuilder.render() {
        child(Navbar::class) {
            attrs {
                showMainPageFunc = {
                    showPage(Pages.MAIN_PAGE)
                }
                showRulesPopupFunc = {
                    TODO()
                }
            }
        }
        when (state.page) {
            Pages.MAIN_PAGE -> child(MainPage::class) {
                attrs {
                    showGamePageFunc = {
                        showPage(Pages.GAME_PAGE)
                    }
                    showRulesPopupFunc = {
                        TODO()
                    }
                }
            }
            Pages.GAME_PAGE -> child(GamePage::class) {}
        }
    }
}