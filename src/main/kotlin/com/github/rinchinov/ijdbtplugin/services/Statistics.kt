package com.github.rinchinov.ijdbtplugin.services

import ConsentDialog
import com.amplitude.Amplitude
import com.amplitude.Event
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.components.*
import com.intellij.openapi.extensions.PluginId
import org.jetbrains.letsPlot.commons.registration.Disposable
import org.json.JSONObject
import java.util.*


@Service(Service.Level.APP)
@State(
    name = "ijdbtplugin",
    storages = [Storage("ijdbtplugin.xml")]
)
class Statistics: PersistentStateComponent<Statistics.State>, Disposable {
    class State {
        var isUserConsent: Boolean? = null
        var deviceId: String = UUID.randomUUID().toString()
    }

    private var myState = State()

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState = state
    }

    companion object {
        fun getInstance(): Statistics {
            return service()
        }
    }

    private var settings: ProjectSettings? = null
    private var configurations: ProjectConfigurations? = null
    private val amplitude = Amplitude.getInstance()
    private val appInfo = ApplicationInfo.getInstance()
    private val sessionId = System.currentTimeMillis()
    private val pluginDescriptor = PluginManagerCore.getPlugin(PluginId.getId("com.github.rinchinov.ijdbtplugin"))

    enum class GroupName {
        CORE, NAVIGATION_AND_ANNOTATIONS, QUERY_EXECUTION;
        override fun toString(): String {
            return when(this) {
                CORE -> "Core Functionality"
                NAVIGATION_AND_ANNOTATIONS -> "Navigation and annotations"
                QUERY_EXECUTION -> "Query Execution"
            }
        }
    }

    init {
        amplitude.init(getAmplitudeKey())
        sessionEvent("Session started")
    }

    private fun getAmplitudeKey(): String {
        val inputStream = javaClass.classLoader.getResourceAsStream("statistics1.properties")
        val properties = Properties()
        inputStream?.let {
            properties.load(it)
            return properties.getProperty("statisticsToken", "")
        }
        return ""
    }
    
    private fun getDeviceId(): String {
        if (myState.isUserConsent == null) {
            ConsentDialog().showAndGet()
            sendStatistics(GroupName.CORE, "Statistics", "Consent dialog was shown")
        }
        return if (myState.isUserConsent == true){
            myState.deviceId
        }
        else {
            UUID.randomUUID().toString()
        }
    }

    fun setProjectConfigurations(configurations: ProjectConfigurations) {
        this.configurations = configurations
    }

    fun setProjectSettings(settings: ProjectSettings) {
        this.settings = settings
    }

    fun getUserConsent() = myState.isUserConsent

    fun setUserConsent(isUserConsent: Boolean){
        myState.isUserConsent = isUserConsent
    }

    private fun getAdapterName() = configurations?.dbtProjectConfig?.adapterName

    private fun sessionEvent(eventType: String){
        val event = Event(eventType, null, UUID.randomUUID().toString())
        event.sessionId = sessionId
        event.appVersion = pluginDescriptor?.version
        event.osName = System.getProperty("os.name")
        event.osVersion = System.getProperty("os.version")
        event.platform = System.getProperty("os.arch")
        val eventProps: JSONObject = JSONObject()
            .put("IDE Name", appInfo.versionName ?: "UNKNOWN")
            .put("IDE Version", appInfo.majorVersion ?: "UNKNOWN")
            .put("DBT Adapter", getAdapterName())
        event.groups = JSONObject().apply {
            this.put(GroupName.CORE.toString(), "Statistics")
        }
        event.eventProperties = eventProps
        amplitude.logEvent(event)
    }

    private fun sendEvent(eventType: String, eventProperties: Map<String, Any>, groupProperties: Map<String, Any>){
        val event = Event(eventType, null, getDeviceId())
        event.sessionId = sessionId
        event.appVersion = pluginDescriptor?.version
        event.osName = System.getProperty("os.name")
        event.osVersion = System.getProperty("os.version")
        event.platform = System.getProperty("os.arch")
        val eventProps: JSONObject = JSONObject()
            .put("IDE Name", appInfo.versionName ?: "UNKNOWN")
            .put("IDE Version", appInfo.majorVersion ?: "UNKNOWN")
            .put("DBT Adapter", getAdapterName())
            .put("Consent", myState.isUserConsent)
        eventProperties.forEach {
            eventProps.put(it.key, it.value)
        }
        event.groups = JSONObject().apply {
            groupProperties.forEach {
                this.put(it.key, it.value)
            }
        }
        event.eventProperties = eventProps
        amplitude.logEvent(event)
    }

    fun sendStatistics(groupName: GroupName, groupValue: String, eventType: String, eventProperties: Map<String, Any>){
        val groupProperties = mapOf(groupName.toString() to groupValue)
        sendEvent(eventType, eventProperties, groupProperties)
    }

    fun sendStatistics(groupName: GroupName, groupValue: String, eventType: String){
        sendStatistics(groupName, groupValue, eventType, emptyMap())
    }

    override fun dispose() {
        sessionEvent("Session closed")
    }
}
