import react.*

enum class Page {
    MAIN_PAGE,
    GAME_PAGE
}

enum class Popup {
    ABOUT_POPUP,
    RULES_POPUP
}

external interface AppState : State {
    var page: Page
    var popup: Popup?
}

class App : RComponent<Props, AppState>() {
    init {
        state.page = Page.MAIN_PAGE
        state.popup = null
    }

    private fun showPage(newPage: Page) {
        setState {
            page = newPage
        }
    }

    private fun showPopup(newPopup: Popup?) {
        setState {
            popup = newPopup
        }
    }

    override fun RBuilder.render() {
        child(Navbar::class) {
            attrs {
                showMainPageFunc = {
                    showPage(Page.MAIN_PAGE)
                }
                showAboutPopupFunc = {
                    showPopup(Popup.ABOUT_POPUP)
                }
                showRulesPopupFunc = {
                    showPopup(Popup.RULES_POPUP)
                }
            }
        }
        when (state.page) {
            Page.MAIN_PAGE -> child(MainPage::class) {
                attrs {
                    showGamePageFunc = {
                        showPage(Page.GAME_PAGE)
                    }
                    showRulesPopupFunc = {
                        showPopup(Popup.RULES_POPUP)
                    }
                }
            }

            Page.GAME_PAGE -> child(GamePage::class) {}
        }
        when (state.popup) {
            Popup.ABOUT_POPUP -> child(AboutPopup::class) {
                attrs {
                    close = {
                        showPopup(null)
                    }
                }
            }

            Popup.RULES_POPUP -> child(RulesPopup::class) {
                attrs {
                    close = {
                        showPopup(null)
                    }
                }
            }

            null -> {}
        }
    }
}
