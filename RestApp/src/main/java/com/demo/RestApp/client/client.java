package com.demo.RestApp.client;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.Response;

public class client {

	public static void main(String[] args)   {
	 
		String[] images = {"one","two","three","four"};
		int[] time = {400,300,200,100};
		client c = new client();
		String ret = null;
		for(int i=0;i<4;i++){
			c.getAsyncData(images[i], time[i]);
			System.out.println("Async call to restservice = "+i);
		}
		
	}

	void getAsyncData(String imageName, int time){
		Client client = ClientBuilder.newClient();
		String ret = null;
		Future<Response> futureResponse = null;
		
		futureResponse = client.target("http://localhost:8080/RestApp/webapi/myresource/asyncConnCallback?imageName="+imageName+"&time="+time).request().async().get(new InvocationCallback<Response>() {

				@Override
				public void completed(Response response) {
					System.out.println("Response code "+ response.getStatus() );
					String ret = response.readEntity(String.class);
					System.out.println("Response from GET method : "+ ret);
					
				}

				@Override
				public void failed(Throwable throwable) {
					System.out.println("Failed");
					throwable.printStackTrace();
				}
				
			   }); 
		
	}
	
	
	
}
