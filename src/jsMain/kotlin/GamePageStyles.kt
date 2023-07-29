import kotlinx.css.*
import styled.StyleSheet

object GamePageStyles : StyleSheet("GamePageStyles", isStatic = true) {
    val pageLayout by css {
        display = Display.flex
        flexDirection = FlexDirection.row
        flexWrap = FlexWrap.wrap
        fontSize = 25.px
    }

    val fieldContainer by css {
        backgroundColor = rgb(237, 255, 250)
        display = Display.flex
        flexBasis = FlexBasis("400px")
        flexGrow = 5.0
        flexShrink = 0.0
        minHeight = 400.px
        padding = Padding(30.px)
    }

    val rightContainer by css {
        backgroundColor = rgb(217, 232, 230)
        display = Display.flex
        flexBasis = FlexBasis("400px")
        flexDirection = FlexDirection.column
        flexGrow = 1.0
        flexShrink = 0.0
    }

    val submitSection by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        padding = Padding(30.px)
    }

    val sizeRow by css {
        display = Display.flex
        flexDirection = FlexDirection.row
    }

    val sizeTitle by css {
        fontWeight = FontWeight.normal
        margin = Margin(0.px, LinearDimension.auto, 0.px, 0.px)
    }

    val sizeValue by css {
        fontSize = 0.9.em
        margin = Margin(0.px)
    }

    val nameInput by css {
        fontSize = 1.em
        marginTop = 30.px
        textAlign = TextAlign.center
        width = 100.pct
    }

    val submitButton by css {
        fontSize = 1.em
        marginTop = 20.px
    }

    val enterGodModeButton = submitButton

    val godSettingsSection by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        padding = Padding(30.px)
    }

    val godSettingsRow by css {
        alignItems = Align.center
        display = Display.flex
        flexDirection = FlexDirection.row
        marginBottom = 10.px
    }

    val godSettingsRowTitle by css {
        flexBasis = FlexBasis("0")
        flexGrow = 2.0
        fontWeight = FontWeight.normal
        margin = Margin(0.px)
    }

    val godSettingsRowLabel by css {
        flexBasis = FlexBasis("0")
        flexGrow = 1.0
    }

    val godSettingsRowInput by css {
        fontSize = 1.em
        textAlign = TextAlign.center
        width = 100.pct
    }

    val godSettingsRowSplitter by css {
        textAlign = TextAlign.center
        width = 40.px
    }

    val godSettingsApply by css {
        fontSize = 1.em
        marginTop = 20.px
    }

    val godComputerSection by css {
        display = Display.flex
        flexDirection = FlexDirection.column
        marginTop = LinearDimension.auto
        padding = Padding(0.px, 30.px, 30.px, 30.px)
    }

    val godComputerScore by css {
        fontWeight = FontWeight.normal
        margin = Margin(0.px)
        textAlign = TextAlign.center
    }

    val godComputerStart by css {
        fontSize = 1.em
        marginTop = 10.px
    }
}
