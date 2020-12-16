import react.*

class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        child(FieldList::class) {
            attrs.fieldSizes = listOf(6, 7, 8)
        }
    }
}
