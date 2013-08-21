package org.moskito.control.core;

import org.moskito.control.config.ApplicationConfig;
import org.moskito.control.config.ChartConfig;
import org.moskito.control.config.ChartLineConfig;
import org.moskito.control.config.ComponentConfig;
import org.moskito.control.config.MoskitoControlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(ApplicationRepository.class);
	/**
	 * Returns the singleton instance of the ApplicationRepository.
	 * @return
	 */
	public static final ApplicationRepository getInstance(){
		return ApplicationRepositoryInstanceHolder.instance;
	}

	/**
	 * Creates a new repository.
	 */
	private ApplicationRepository(){
		applications = new ConcurrentHashMap<String, Application>();

		readConfig();

		//Following line generates additional test data.
		/*
		new Thread(){
			public void run(){
				try{
					Thread.sleep(500);
				}catch(Exception e){}
				dummyV1();
			}

		}.start();
		*/
	}

	//add watcher for config reloads.
	private void readConfig(){
		ApplicationConfig[] configuredApplications = MoskitoControlConfiguration.getConfiguration().getApplications();
		for (ApplicationConfig ac : configuredApplications){
			Application app = new Application(ac.getName());
			for (ComponentConfig cc : ac.getComponents()){
				Component comp = new Component(app);
				comp.setCategory(cc.getCategory());
				comp.setName(cc.getName());
				app.addComponent(comp);
			}

			if (ac.getCharts()!=null && ac.getCharts().length>0){
				for (ChartConfig cc : ac.getCharts()){
					Chart chart = new Chart(app, cc.getName());

					ChartLineConfig[] lines = cc.getLines();
					for (ChartLineConfig line : lines){
						chart.addLine(line.getComponent(), line.getAccumulator(), line.getCaption());
					}

					app.addChart(chart);
				}
			}
			addApplication(app);
		}
	}

	//GENERATED TEST DATA.
	/*
	private String[] DUMMY_SERVICES = {
			"AccountService", "AuthenticationService", "AccountListService", "RecordService", "AccountSettingsService",
			"BillingService", "PhotoService"
	};

	private void dummyV1(){
		//add dummy data.
		Application app1 = new Application();
		app1.setName("ProductionTest");
		addApplication(app1);

		for (String service : DUMMY_SERVICES){
			for (int i=0; i<3; i++){
				Component serviceComponent = new Component(app1);
				serviceComponent.setName(service+"_"+i);
				serviceComponent.setCategory("Service");
				serviceComponent.setStatus(new Status());
				app1.addComponent(serviceComponent);
			}
		}
		for (int i=1; i<=20; i++){
			Component c = new Component(app1);
			c.setName("Web "+i);
			c.setCategory("Web");
			c.setStatus(new Status());

			if (i==15){
				c.getStatus().setHealth(HealthColor.RED);c.getStatus().addMessage("SessionCount 34000");
			}
			if (i==7){
				c.getStatus().setHealth(HealthColor.YELLOW);c.getStatus().addMessage("SessionCount 14000");
			}
			app1.addComponent(c);
		}

		for (int i=1; i<=3; i++){
			Component c = new Component(app1);
			c.setName("Photo "+i);
			c.setCategory("Photo");
			c.setStatus(new Status());
			app1.addComponent(c);
		}

		for (int i=1; i<=3; i++){
			Component c = new Component(app1);
			c.setName("Media "+i);
			c.setCategory("Media");
			c.setStatus(new Status(HealthColor.YELLOW, "I am not feeling good"));
			c.getStatus().addMessage("My neck is itching");
			app1.addComponent(c);
		}



	}
	//*/

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

	/**
	 * Called whenever a status change is detected. Propagates the change to attached listeners.
	 * @param application affected application.
	 * @param component affected component.
	 * @param oldStatus status before the change.
	 * @param status status after the change.
	 * @param lastUpdateTimestamp timestamp of the update.
	 */
	public void addStatusChange(Application application, Component component, Status oldStatus, Status status, long lastUpdateTimestamp){
		log.debug("addStatusChange("+application+", "+component+", "+oldStatus+", "+status+", "+lastUpdateTimestamp+")");
		for (StatusChangeListener listener: statusChangeListeners){
			try{
				listener.notifyStatusChange(application, component, oldStatus, status, lastUpdateTimestamp);
			}catch(Exception e){
				log.warn("Status change listener "+listener+" couldn't update status",e);
			}
		}
	}
}
