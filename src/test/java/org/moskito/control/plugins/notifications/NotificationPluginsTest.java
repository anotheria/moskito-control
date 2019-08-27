package org.moskito.control.plugins.notifications;

import net.anotheria.util.ArrayUtils;
import org.configureme.ConfigurationManager;
import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;
import org.junit.Test;
import org.moskito.control.core.Component;
import org.moskito.control.core.HealthColor;
import org.moskito.control.core.status.Status;
import org.moskito.control.core.status.StatusChangeEvent;
import org.moskito.control.plugins.notifications.config.BaseNotificationPluginConfig;
import org.moskito.control.plugins.notifications.config.BaseNotificationProfileConfig;
import org.moskito.control.plugins.notifications.config.NotificationStatusChange;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.moskito.control.core.HealthColor.GREEN;
import static org.moskito.control.core.HealthColor.PURPLE;
import static org.moskito.control.core.HealthColor.RED;
import static org.moskito.control.core.HealthColor.YELLOW;

/**
 * Test base config classes for notification plugins
 */
public class NotificationPluginsTest {

    /**
     * Main config for tests
     * Loads data from plugin-config-mock.json
     * in resources folder
     */
    private static final PluginConfigMock TEST_CONFIG_MOCK = getTestConfig("plugin-config-mock");

    /**
     * Helper method to load configuration in mock config object
     * @param configName name of config
     * @return configured by ConfigureMe mock config object
     */
    private static PluginConfigMock getTestConfig(String configName){

        PluginConfigMock config = new PluginConfigMock();
        ConfigurationManager.INSTANCE.configureAs(config, configName);

        return config;

    }

    /**
     * Helper method to create event object.
     * Component name and timestamp is always constant -
     * they are not required by this test class
     *
     * @param appName name of application
     * @param oldStatus new status of event
     * @param newStatus old status of event
     * @return event object filled by method arguments
     */
    private StatusChangeEvent createEvent(String appName, HealthColor oldStatus, HealthColor newStatus){

        return new StatusChangeEvent(
                new Component("nevermind"), // components names does not involve on anything
                new Status(oldStatus, "old"), new Status(newStatus, "new"),
                0
                );

    }

    /**
     * Helper method to get profiles from plugin config by it ids.
     * Used as expected in assertions with results of getProfileForEvent method of
     * BaseNotificationPluginConfig class.
     * Forms list with profile configs from first argument with ids occurs in other arguments
     *
     * @param pluginConfig configuration of plugin profile configuration are taken from here
     * @param profilesIds ids of profiles to return in list
     *
     * @return list of profile configs with specified ids
     */
    private List<ProfileConfigMock> getProfilesById(PluginConfigMock pluginConfig, String... profilesIds){

        return Arrays.stream(pluginConfig.getProfileConfigs())
                .filter(profileConfig -> ArrayUtils.contains(profilesIds, profileConfig.getId()))
                .collect(Collectors.toList());

    }

    /**
     * Tests filtering profile config by application name
     */
    @Test
    public void testApp(){

        StatusChangeEvent appOneEvent = createEvent("TEST_APP_1", RED, GREEN);
        StatusChangeEvent appTwoEvent = createEvent("TEST_APP_2", RED, GREEN);

        assertEquals(
                getProfilesById(TEST_CONFIG_MOCK, "for_test_app_1", "for_test_app_1_and_2"),
                TEST_CONFIG_MOCK.getProfileForEvent(appOneEvent)
        );

        assertEquals(
                getProfilesById(TEST_CONFIG_MOCK, "for_test_app_1_and_2"),
                TEST_CONFIG_MOCK.getProfileForEvent(appTwoEvent)
        );

    }

    /**
     * Test filtering profile configs by status changes in event
     */
    @Test
    public void testStatusChange(){

        StatusChangeEvent testEvent = createEvent("TEST_APP_3", RED, PURPLE);

        assertEquals(
                getProfilesById(TEST_CONFIG_MOCK, "for_test_app_3"),
                TEST_CONFIG_MOCK.getProfileForEvent(testEvent)
        );

    }

    /**
     * Test filtering profile configs by new event status
     */
    @Test
    public void testStatusChangeOnlyTo(){

        StatusChangeEvent testEvent = createEvent("TEST_APP_4", RED, GREEN);

        assertEquals(
                getProfilesById(TEST_CONFIG_MOCK, "for_test_app_4"),
                TEST_CONFIG_MOCK.getProfileForEvent(testEvent)
        );

    }

    /**
     * Test filtering profile configs by old event status
     */
    @Test
    public void testStatusChangeOnlyFrom(){

        StatusChangeEvent testEvent = createEvent("TEST_APP_5", YELLOW, GREEN);

        assertEquals(
                getProfilesById(TEST_CONFIG_MOCK, "for_test_app_5"),
                TEST_CONFIG_MOCK.getProfileForEvent(testEvent)
        );

    }

    /**
     * Mock for BaseNotificationProfileConfig.
     * base class can not be tested directly due this class is abstract
     * Beside fields required by base class (applications and status changes arrays)
     * has id filed to carry out expected profiles from plugin config
     * Configured by ConfigureMe
     */
    @ConfigureMe
    public static class ProfileConfigMock extends BaseNotificationProfileConfig{

        /**
         * Array of application names corresponds to this profile
         */
        @Configure
        private String[] applications = new String[0];
        /**
         * Status change conditions of this profile
         */
        @Configure
        private NotificationStatusChange[] statusChanges = new NotificationStatusChange[0];
        /**
         * Id of profile
         */
        @Configure
        private String id;


        public NotificationStatusChange[] getStatusChanges() {
            return statusChanges;
        }

        public void setApplications(String[] applications){
            this.applications = applications;
        }

        public void setStatusChanges(NotificationStatusChange[] statusChanges){
            this.statusChanges = statusChanges;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    /**
     * Mock for BaseNotificationPluginConfig.
     * base class can not be tested directly due this class is abstract.
     * Contains profiles filed with profiles configs
     * return it in getProfileConfigs method
     * Configured by ConfigureMe
     */
    @ConfigureMe
    public static class PluginConfigMock extends BaseNotificationPluginConfig<ProfileConfigMock>{

        @Configure
        private ProfileConfigMock[] profiles = new ProfileConfigMock[0];

        @Override
        protected ProfileConfigMock[] getProfileConfigs() {
            return profiles;
        }

        public void setProfiles(ProfileConfigMock[] profiles){
            this.profiles = profiles;
        }

    }

}
