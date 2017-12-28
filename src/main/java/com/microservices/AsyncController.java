package com.microservices;

import java.util.List;

import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import rx.Observable;
import rx.schedulers.Schedulers;

@RestController
@RequestMapping
@Api(value = "Rx", produces = "application/json")
public class AsyncController {
	
	private static final String host = "http://localhost:";
	private static final String port = "8080";
	private static final String contextPath = "/microservices-template/"; 

	@RequestMapping(value = "/getRx/{ids}", method = RequestMethod.GET,produces = "application/json")
	@ApiOperation(value = "Fetch a Load Balancer Appliance using Id", response = List.class, httpMethod = "GET")
	public DeferredResult<List<String>> get(@PathVariable String[] ids) {
		DeferredResult<List<String>> deffered = new DeferredResult<List<String>>();
		Observable<String> vals = Observable.from(ids);
		
		vals
			.flatMap(AsyncController::getObs)
			.toSortedList(ids.length)
			.subscribe(m -> deffered.setResult(m));
		
		return deffered;
	}
	
	public static Observable<String> getObs(String product){
		System.out.println("Inside GETOBS : "+ Thread.currentThread().getName() );
		return Observable
				.just(product)
				.flatMap(AsyncController::service)
				.subscribeOn(Schedulers.io());
	}
	
	/*public DeferredResult<String> getAMessageAsync() {
	    Observable<String> o = this.service1.getAMessageObs();
	    DeferredResult<Message> deffered = new DeferredResult<>(90000);
	    o.subscribe(m -> deffered.setResult(m), e -> deffered.setErrorResult(e));
	    return deffered;
	}*/

	
	public static Observable<String> service(String product) {
		System.out.println("Received " + product + " on " + Thread.currentThread().getName());
		RestTemplate template = new RestTemplate();
		template.getInterceptors().add(new BasicAuthorizationInterceptor("admin", "admin"));
		LBModel entity = template.getForObject(
				 host + port + contextPath + "get/" + product,
				LBModel.class);
		
		ObjectMapper mapper = new ObjectMapper();
		String result = null;
		try {
			result = entity == null ? null : mapper.writeValueAsString(entity); 
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Observable.just(result);
	}

}
