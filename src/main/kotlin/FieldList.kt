import react.*
import react.dom.*

external interface FieldListProps: RProps {
    var fieldSizes: List<Int>
}

class FieldList: RComponent<FieldListProps, RState>() {
    override fun RBuilder.render() {
        for (fieldSize in props.fieldSizes) {
            p {
                key = fieldSize.toString()
                +"${fieldSize}x${fieldSize}"
            }
        }
    }
}