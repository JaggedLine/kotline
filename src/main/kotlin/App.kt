import react.*

enum class Pages {
    MAIN_PAGE,
    GAME_PAGE
}

external interface AppState: RState {
    var page: Pages
    var fieldSize: Int
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

    private fun showGamePage(newFieldSize: Int) {
        setState {
            page = Pages.GAME_PAGE
            fieldSize = newFieldSize
        }
    }

    override fun RBuilder.render() {
        when (state.page) {
            Pages.MAIN_PAGE -> child(MainPage::class) {
                attrs {
                    showGamePageFunc = {
                        fieldSize -> showGamePage(fieldSize)
                    }
                    fieldProps = listOf(
                        6 to CommonStyles.greenButton,
                        7 to CommonStyles.blueButton,
                        8 to CommonStyles.pinkButton,
                        9 to CommonStyles.yellowButton,
                        10 to CommonStyles.orangeButton
                    )
                }
            }
            Pages.GAME_PAGE -> child(GamePage::class) {
                attrs {
                    showPageFunc = {
                        page -> showPage(page)
                    }
                    fieldSize = state.fieldSize
                }
            }
        }
    }
}

