package de.tudarmstadt.informatik.tk.assistance.api.tester;

import org.restlet.Component;
import org.restlet.data.Protocol;

import de.tudarmstadt.informatik.tk.assistance.api.tester.service.AssistanceService;
import de.tudarmstadt.informatik.tk.assistance.api.tester.service.DeviceService;
import de.tudarmstadt.informatik.tk.assistance.api.tester.service.SensorDataService;
import de.tudarmstadt.informatik.tk.assistance.api.tester.service.UserService;

/**
 * Assistance API tester
 * 
 * @author Wladimir Schmidt (wlsc.dev@gmail.com)
 *
 */
public class App {
	
    public static void main( String[] args ) throws Exception{
        
        Component component = new Component();  
  
        component.getServers().add(Protocol.HTTP, 80);  
        
        component.getDefaultHost().attach("/assistance", new AssistanceService());
        component.getDefaultHost().attach("/devices", new DeviceService());
        component.getDefaultHost().attach("/users", new UserService());
        component.getDefaultHost().attach("/sensordata", new SensorDataService());
        
        component.start();  
    }
}
