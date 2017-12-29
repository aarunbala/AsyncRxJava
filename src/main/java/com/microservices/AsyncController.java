package com.microservices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.model.Allergen;
import com.microservices.model.Product;
import com.microservices.model.ProductRepository;
import com.microservices.model.ProductRequest;
import com.microservices.model.Products;
import com.microservices.vo.ProductVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@RestController
public class AsyncController {
	
	private static final String host = "http://localhost:";
	private static final String port = "8080";
	private static final String contextPath = "/microservices-template/"; 
	
	@Autowired
	AsyncService service;
	
	@PostMapping(value = "/getProducts",produces = "application/json")
	public DeferredResult<List<ProductVO>> getProducts(@RequestBody ProductRequest request) {
		return service.getProducts(request);
	}
	
	@PostMapping("/addProducts")
	public void addProducts(@RequestBody List<Product> products) {
		service.addProducts(products);
	}
	
	@PostMapping("/addAllergens")
	public void addAllergens(@RequestBody List<Allergen> allergens) {
		service.addAllergens(allergens);
	}

	@RequestMapping(value = "/getRx/{ids}", method = RequestMethod.GET,produces = "application/json")
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
