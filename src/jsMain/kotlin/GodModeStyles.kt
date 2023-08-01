import kotlinx.css.*
import styled.StyleSheet

object GodModeStyles : StyleSheet("GodModeStyles", isStatic = true) {
    val settingsSection by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        padding = Padding(30.px)
    }

    val settingsRow by css {
        alignItems = Align.center
        display = Display.flex
        flexDirection = FlexDirection.row
        marginBottom = 10.px
    }

    val settingsRowTitle by css {
        flexBasis = FlexBasis("0")
        flexGrow = 2.0
        fontWeight = FontWeight.normal
        margin = Margin(0.px)
    }

    val settingsRowLabel by css {
        flexBasis = FlexBasis("0")
        flexGrow = 1.0
    }

    val settingsRowInput by css {
        fontSize = 1.em
        textAlign = TextAlign.center
        width = 100.pct
    }

    val settingsRowSplitter by css {
        textAlign = TextAlign.center
        width = 40.px
    }

    val settingsApply by css {
        fontSize = 1.em
        marginTop = 20.px
    }

    val computerSection by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        marginTop = LinearDimension.auto
        padding = Padding(0.px, 30.px, 30.px, 30.px)
    }

    val computerScore by css {
        fontWeight = FontWeight.normal
        margin = Margin(0.px)
        textAlign = TextAlign.center
    }

    val computerStart by css {
        fontSize = 1.em
        marginTop = 10.px
    }
}
