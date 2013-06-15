package org.moskito.control.core;

import org.apache.log4j.Logger;
import org.moskito.control.config.ApplicationConfig;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages applications.
 *
 * @author lrosenberg
 * @since 01.04.13 23:08
 */
public final class ApplicationRepository {

	/**
	 * Map with currently configured applications.
	 */
	private ConcurrentMap<String, Application> applications;

	/**
	 * Listeners for status updates.
	 */
	private List<StatusChangeListener> statusChangeListeners = new CopyOnWriteArrayList<StatusChangeListener>();


	private static Logger log = Logger.getLogger(ApplicationRepository.class);
	/**
	 * Returns the singleton instance of the ApplicationRepository.
	 * @return
	 */
	public static final ApplicationRepository getInstance(){
		return ApplicationRepositoryInstanceHolder.instance;
	}

	private ApplicationRepository(){
		applications = new ConcurrentHashMap<String, Application>();

		readConfig();

		//dummyV1();
	}

	//add watcher for config reloads.
	private void readConfig(){
		ApplicationConfig[] applications = MoskitoControlConfiguration.getConfiguration().getApplications();
		for (ApplicationConfig ac : applications){
			Application app = new Application(ac.getName());
			for (ComponentConfig cc : ac.getComponents()){
				Component comp = new Component(app);
				comp.setCategory(cc.getCategory());
				comp.setName(cc.getName());
				app.addComponent(comp);
			}
			addApplication(app);
		}
	}

	private String[] DUMMY_SERVICES = {
			"AccountService", "AuthenticationService", "AccountListService", "RecordService", "AccountSettingsService",
			"BillingService", "PhotoService"
	};

	private void dummyV1(){
		//add dummy data.
		Application app1 = new Application();
		app1.setName("Production");
		addApplication(app1);

		Application app2 = new Application();
		app2.setName("PreProduction");
		addApplication(app2);


		Application app3 = new Application();
		app3.setName("Post Production");
		addApplication(app3);

		Application app4 = new Application();
		app4.setName("QA");
		addApplication(app4);

		for (String service : DUMMY_SERVICES){
			for (int i=0; i<3; i++){
				Component serviceComponent = new Component(app1);
				serviceComponent.setName(service+"_"+i);
				serviceComponent.setCategory("Service");
				serviceComponent.setStatus(new Status());
				app1.addComponent(serviceComponent);
				app2.addComponent(serviceComponent.clone());
				app3.addComponent(serviceComponent.clone());
				Component qaService = serviceComponent.clone();
				Status qaServiceStatus = new Status(); qaServiceStatus.setHealth(HealthColor.PURPLE); qaServiceStatus.addMessage("DOWN FOR GOOD");
				app4.addComponent(qaService);
			}
		}
		for (int i=1; i<=20; i++){
			Component c = new Component(app1);
			c.setName("Web "+i);
			c.setCategory("Web");
			c.setStatus(new Status());

			if (i==15)
				c.getStatus().setHealth(HealthColor.RED);
			if (i==7)
				c.getStatus().setHealth(HealthColor.YELLOW);
			app1.addComponent(c);
			if (i==1){
				app2.addComponent(c); app3.addComponent(c); app4.addComponent(c);
			}
		}

		for (int i=1; i<=3; i++){
			Component c = new Component(app1);
			c.setName("Photo "+i);
			c.setCategory("Photo");
			c.setStatus(new Status());
			app1.addComponent(c);
			if (i==1){
				app2.addComponent(c); app3.addComponent(c); app4.addComponent(c);
			}
		}

		for (int i=1; i<=3; i++){
			Component c = new Component(app1);
			c.setName("Media "+i);
			c.setCategory("Media");
			c.setStatus(new Status());
			app1.addComponent(c);
			if (i==1){
				app2.addComponent(c); app3.addComponent(c); app4.addComponent(c);
			}
		}



	}

	private void addApplication(Application app){
		applications.put(app.getName(), app);
	}

	public List<Application> getApplications(){
		ArrayList<Application> ret = new ArrayList<Application>();
		ret.addAll(applications.values());
		return ret;
	}

	public Application getApplication(String applicationName) {
		return applications.get(applicationName);
	}

	public void addStatusChangeListener(StatusChangeListener statusChangeListener) {
		statusChangeListeners.add(statusChangeListener);
	}

	public void removeStatusChangeListener(StatusChangeListener statusChangeListener) {
		statusChangeListeners.remove(statusChangeListener);
	}

	/**
	 * Singleton instance holder class.
	 */
	private static class ApplicationRepositoryInstanceHolder{
		/**
		 * Singleton instance.
		 */
		private static final ApplicationRepository instance = new ApplicationRepository();
	}

	public void addStatusChange(Application app, Component component, Status oldStatus, Status status, long lastUpdateTimestamp){
		log.debug("addStatusChange("+app+", "+component+", "+oldStatus+", "+status+", "+lastUpdateTimestamp+")");
		for (StatusChangeListener listener: statusChangeListeners){
			try{
				listener.notifyStatusChange(app, component, oldStatus, status, lastUpdateTimestamp);
			}catch(Exception e){
				log.warn("Status change listener "+listener+" couldn't update status",e);
			}
		}
	}
}
