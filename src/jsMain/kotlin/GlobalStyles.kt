import kotlinx.css.*

val globalStyles = CssBuilder().apply {
    "*, *::after, *::before" {
        boxSizing = BoxSizing.borderBox
    }

    html {
        display = Display.flex
        minHeight = 100.pct
    }

    body {
        display = Display.flex
        flex = Flex(1.0)
        flexDirection = FlexDirection.column
        fontFamily = "'Noto Sans JP', sans-serif"
        margin = Margin(0.px)
    }
}
