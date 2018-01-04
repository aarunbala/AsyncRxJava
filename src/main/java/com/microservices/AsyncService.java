package com.microservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.microservices.model.Allergen;
import com.microservices.model.AllergenRepository;
import com.microservices.model.Product;
import com.microservices.model.ProductRepository;
import com.microservices.model.ProductRequest;
import com.microservices.model.Products;
import com.microservices.vo.ProductVO;
import com.microservices.vo.AllergenVO;

import rx.Observable;
import rx.schedulers.Schedulers;

@Service
public class AsyncService {

    private Logger LOG = LoggerFactory.getLogger(AsyncService.class);

	@Autowired
	ProductRepository repo;
	
	@Autowired
	AllergenRepository allergenRepo;
	
	@Autowired
	private Tracer tracer;
	

	public DeferredResult<List<ProductVO>> getProducts(ProductRequest request) {
		DeferredResult<List<ProductVO>> deffered = new DeferredResult<List<ProductVO>>();
		Observable<String> profiles = Observable.from(request.getProfiles());
		Products products = new Products();
		
		profiles
			.flatMap(profile -> getProductsForProfile(products, request.getProductIds(), profile))
			.toSortedList(request.getProfiles().size())
                .doOnError(er -> System.out.println("MAIN"+er))
			.subscribe(m -> {
				collateProductDetails(products, request.getProductIds());
				deffered.setResult(products.getProducts());	
			});
		
		return deffered;
	}

	public Observable<Products> getProductsForProfile(Products products, List<String> productIds, String profile) {
//		System.out.println("Inside getProductsForProfile : " + Thread.currentThread().getName());
		LOG.info("Inside getProductsForProfile : {}", Thread.currentThread().getName());
		return Observable
				.just(profile)
				.flatMap(pdt -> getProductFromDB(products, productIds, profile))
                .onErrorReturn(er -> {
                    er.printStackTrace();
                    return new Products();
                })
				.subscribeOn(Schedulers.io());
	}

	public Observable<Products> getProductFromDB(Products products, List<String> productIds, String profile) {
		System.out.println("Inside getProductFromDB : " + Thread.currentThread().getName());
		LOG.info("Inside getProductFromDB : {}" , Thread.currentThread().getName());
		switch (profile) {
			case "BASIC":
				Span spanB = this.tracer.createSpan("Basic Database call");
				products.setBasic(getBasicProductDetails(productIds));
				this.tracer.close(spanB);
				break;
			case "ALLERGEN":
				Span spanA = this.tracer.createSpan("Allergen Database call");
//			    throw new RuntimeException("Database unavailable");
				products.setAllergen(getAllergenDetails(productIds));
				this.tracer.close(spanA);
                break;
			case "LOCATION":
				break;
		}
		return Observable.just(products);
	}
	
	public Map<String, Product> getBasicProductDetails(List<String> productIds){
		List<Product> productList = repo.findAll(productIds);
		Map<String, Product> productMap = new HashMap<>();
		System.out.println(productList);
		if(productIds != null) {
			productList.forEach(pdt -> {
				Product pt = (Product) pdt;
				productMap.put(pt.getProductId(), pt);
			});
		}
		return productMap;
	}
	
	public Map<String, List<Allergen>> getAllergenDetails(List<String> productIds){
		List<Allergen> allergenList = allergenRepo.findByProductIdIn(productIds);
		System.out.println(allergenList);
		Map<String, List<Allergen>> allergenMap = new HashMap<>();
		allergenList.forEach(allergen -> {
			if(allergenMap.containsKey(allergen.getProductId())) {
				allergenMap.get(allergen.getProductId()).add(allergen);
			} else {
				List<Allergen> allergens = new ArrayList<>();
				allergens.add(allergen);
				allergenMap.put(allergen.getProductId(), allergens);
			}
		});
		return allergenMap;
	}

	public void collateProductDetails(Products products, List<String> productIds) {
		System.out.println(products);
		List<ProductVO> productVOList = new ArrayList<>();
		productIds.forEach(productId -> {
			Product pdt = products.getBasic().get(productId);
			ProductVO pdtVO = new ProductVO();
			BeanUtils.copyProperties(pdt, pdtVO);
			
			List<Allergen> allergens = products.getAllergen().get(productId);
			List<AllergenVO> allergenVOs = new ArrayList<>();
			if(allergens != null) {
				allergens.forEach(allergen -> {
					AllergenVO vo = new AllergenVO();
					BeanUtils.copyProperties(allergen, vo);
					allergenVOs.add(vo);
				});
			}
			
			pdtVO.setAllergens(allergenVOs);
			productVOList.add(pdtVO);
		});
		products.setProducts(productVOList);
	}
	
	public void addProducts(List<Product> products) {
		LOG.info("Adding Products");
		repo.save(products);
	}

	public void addAllergens(List<Allergen> allergens) {
		LOG.info("Adding Allergens");
		allergenRepo.save(allergens);
	}
}
