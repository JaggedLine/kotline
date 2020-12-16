import kotlinx.html.js.onClickFunction
import react.*
import react.dom.button

external interface MainPageProps: RProps {
    var setPage: (Pages) -> Unit
}

class MainPage: RComponent<MainPageProps, RState>() {
    override fun RBuilder.render() {
        button {
            attrs {
                onClickFunction = {
                    props.setPage(Pages.GAME_PAGE)
                }
            }
        }
    }
}
