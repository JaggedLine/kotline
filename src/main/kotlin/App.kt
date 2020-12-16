import kotlinx.browser.document
import react.*
import react.dom.p

val fieldSizes = listOf(6, 7, 8)

class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        child(FieldList::class) {
            attrs.fieldSizes = fieldSizes
        }
    }
}
