package org.anotheria.moskito.control.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 01.04.13 23:08
 */
public class ApplicationRepository {

	private ConcurrentMap<String, Application> applications;

	public static final ApplicationRepository getInstance(){
		return ApplicationRepositoryInstanceHolder.instance;
	}

	private ApplicationRepository(){
		applications = new ConcurrentHashMap<String, Application>();


		dummyV1();
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
		for (String service : DUMMY_SERVICES){
			for (int i=0; i<3; i++){
				Component serviceComponent = new Component();
				serviceComponent.setName(service);
				serviceComponent.setCategory("Service");
				serviceComponent.setStatus(new Status());

			}
		}


		Application app2 = new Application();
		app2.setName("PreProduction");
		addApplication(app2);


		Application app3 = new Application();
		app3.setName("Post Production");
		addApplication(app3);

		Application app4 = new Application();
		app4.setName("QA");
		addApplication(app4);

	}

	private void addApplication(Application app){
		applications.put(app.getName(), app);
	}

	public List<Application> getApplications(){
		ArrayList<Application> ret = new ArrayList<Application>();
		ret.addAll(applications.values());
		return ret;
	}


	private static class ApplicationRepositoryInstanceHolder{
		private static final ApplicationRepository instance = new ApplicationRepository();
	}
}
