import kotlinx.browser.document
import react.dom.render

//override fun AppState.init() {
//
//}

fun main() {
    render(document.getElementById("root")) {
        child(App::class) {

        }
    }
}
