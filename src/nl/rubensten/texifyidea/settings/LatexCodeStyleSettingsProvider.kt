package nl.rubensten.texifyidea.settings

import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.openapi.options.Configurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import nl.rubensten.texifyidea.LatexLanguage

/**
 *
 * @author Sten Wessel
 */
class LatexCodeStyleSettingsProvider : CodeStyleSettingsProvider() {

    override fun createCustomSettings(settings: CodeStyleSettings?) = settings?.let { LatexCodeStyleSettings(it) }

    override fun getConfigurableDisplayName() = LatexLanguage.INSTANCE.displayName

    override fun createSettingsPage(settings: CodeStyleSettings, originalSettings: CodeStyleSettings?): Configurable {
        return object : CodeStyleAbstractConfigurable(settings, originalSettings, configurableDisplayName) {

            override fun createPanel(settings: CodeStyleSettings?): CodeStyleAbstractPanel {
                val language = LatexLanguage.INSTANCE

                return object : TabbedLanguageCodeStylePanel(language, currentSettings, settings) {

                }
            }

        }
    }
}