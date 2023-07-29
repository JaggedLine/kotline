import react.dom.render
import styled.injectGlobal
import web.dom.document

fun main() {
    injectGlobal(globalStyles)
    render(document.body) {
        child(App::class) {}
    }
}
