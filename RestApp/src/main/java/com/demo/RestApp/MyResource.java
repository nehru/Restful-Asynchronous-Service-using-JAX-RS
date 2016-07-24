package com.demo.RestApp;

import java.util.concurrent.ExecutorService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ConnectionCallback;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Test data ";
    }
    
    @GET
	@Path("/asyncConnCallback")
	public void asyncGetConnectionCallback(@Suspended final AsyncResponse asyncResponse, @Context UriInfo info) {
    	final ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();
    	
    	final String imageName = info.getQueryParameters().getFirst("imageName");
    	final int time = Integer.parseInt(info.getQueryParameters().getFirst("time"));
    	
    	System.out.println("image name = "+imageName);
    	System.out.println("time = "+time);
    	asyncResponse.register(new ConnectionCallback() 
		{
			@Override
			public void onDisconnect(AsyncResponse asyncResponse) 
			{
				asyncResponse.resume(Response
						.status(Response.Status.SERVICE_UNAVAILABLE)
						.entity("Connection Callback").build());
			}
		});

    	executorService.submit(new Runnable() 
		{
			
			@Override
			public void run() 
			{
				System.out.println("server run called... (Thread id = "+Thread.currentThread().getId()+")");
				String result = veryExpensiveOperation();
				asyncResponse.resume(result);
			}

			private String veryExpensiveOperation() 
			{
				try {
					for(int i=0;i<100;i++){
						System.out.println(Thread.currentThread().getId()+" ");
					Thread.sleep(time);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return "Very Expensive Operation with Connection Callback (Thread id = "+Thread.currentThread().getId()+","+
						" Image Name = "+imageName+","+
						" Sleep time = "+time+
						")";
			}
		});
    	
		
	}

    
    
}
