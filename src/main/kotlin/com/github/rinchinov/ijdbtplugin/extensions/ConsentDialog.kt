import com.github.rinchinov.ijdbtplugin.services.Statistics
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class ConsentDialog : DialogWrapper(true) {
    init {
        init()
        title = "DBT Plugin Collect Usage Statistic Consent"
    }

    override fun createCenterPanel(): JComponent? {
        val dialogPanel = JPanel()
        dialogPanel.add(JLabel("<html><p>Please consent to data collection to help us improve the plugin.<br>" +
                "By clicking 'Agree', you consent to the collection and use of your data as described in our " +
                "<a href='https://github.com/rinchinov/ij-dbt-plugin/blob/main/PRIVACY_POLICY'>privacy policy</a>.</p></html>"))
        return dialogPanel
    }

    override fun doOKAction() {
        super.doOKAction()
        saveConsent(true)
    }

    override fun doCancelAction() {
        super.doCancelAction()
        saveConsent(false)
    }

    private fun saveConsent(consented: Boolean) {
        Statistics.getInstance().setUserConsent(consented)
    }
}
