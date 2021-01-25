import react.*

enum class Pages {
    MAIN_PAGE,
    GAME_PAGE
}

external interface AppState : RState {
    var page: Pages
    var rulesShown: Boolean
}

class App : RComponent<RProps, AppState>() {
    init {
        state.page = Pages.MAIN_PAGE
        state.rulesShown = false
    }

    private fun showPage(newPage: Pages) {
        setState {
            page = newPage
        }
    }

    private fun showRules() {
        setState {
            rulesShown = true
        }
    }

    private fun closeRules() {
        setState {
            rulesShown = false
        }
    }

    override fun RBuilder.render() {
        child(Navbar::class) {
            attrs {
                showMainPageFunc = {
                    showPage(Pages.MAIN_PAGE)
                }
                showRulesPopupFunc = {
                    showRules()
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
                        showRules()
                    }
                }
            }
            Pages.GAME_PAGE -> child(GamePage::class) {}
        }
        if (state.rulesShown) {
            child(RulesPopup::class) {
                attrs {
                    close = {
                        closeRules()
                    }
                }
            }
        }
    }
}