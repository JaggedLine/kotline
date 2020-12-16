import kotlinx.browser.document
import react.*

external interface GamePageProps: RProps {
    var setPage: (Pages) -> Unit
}

class GamePage: RComponent<GamePageProps, RState>() {
    override fun RBuilder.render() {
        document.bgColor = "green"
    }
}
