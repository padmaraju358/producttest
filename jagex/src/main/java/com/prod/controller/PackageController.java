package com.prod.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.prod.model.ProductDetails;
import com.prod.model.Product;

@RestController
public class PackageController {

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	@GetMapping(path = "/welcome")
	public String testMe() {
		return "You are welcome";
	}

	/**
	 * Retrieve a package
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveAPackage(@PathVariable("id") String id) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		RestTemplate restTemplate = restTemplateBuilder.basicAuthorization("user", "pass").build();
		return restTemplate.getForObject("https://product-service.herokuapp.com/api/v1/products/{id}", String.class,
				params);
	}

	/**
	 * Retrieve a package in a specified currency
	 * 
	 * @param id
	 * @param currency
	 * @return
	 */

	@RequestMapping(value = "/products/{id}/{currency}", method = RequestMethod.GET)
	@ResponseBody
	public ProductDetails retrieveAPackageByCurrency(@PathVariable("id") String id,
													 @PathVariable("currency") String currency)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		RestTemplate restTemplate = restTemplateBuilder.basicAuthorization("user", "pass").build();
		Product product = restTemplate.getForObject("https://product-service.herokuapp.com/api/v1/products/{id}",
				Product.class, params);
		
		// retrieving exchange with base as EUR (limited access for free trail api key)
		// We can map the exchanges in a hashmap but this is quick for performance.
		
		String s = restTemplate.getForObject(
				"http://data.fixer.io/api/latest?access_key=02cfb27256e551af5d889e0efe3c17b7", String.class);
		Pattern pattern = Pattern.compile(currency + "(.*?),");
		Matcher matcher = pattern.matcher(s);
		String g = "";
		while (matcher.find()) {
			g = matcher.group(1).substring(2);
		}
		double val = Double.parseDouble(g) * product.getPrice();
		// Create response
		ProductDetails productDetails = new ProductDetails(product.getId(), product.getName(), val);
		return productDetails;
	}

	/**
	 * List all packages
	 * 
	 * @return
	 */
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	@ResponseBody
	public String retrieveAll() {
		RestTemplate restTemplate = restTemplateBuilder.basicAuthorization("user", "pass").build();
		return restTemplate.getForObject("https://product-service.herokuapp.com/api/v1/products", String.class);
	}

	/**
	 * Create/update and delete requires a uri which must be provided
	 * 
	 * @param product
	 * @return
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<String> create(@RequestBody Product product) {
		RestTemplate restTemplate = restTemplateBuilder.basicAuthorization("user", "pass").build();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity entity = new HttpEntity(product, headers);
		ResponseEntity<String> out = restTemplate.exchange("https://product-service.herokuapp.com/api/v1/product/",
				HttpMethod.POST, entity, String.class);

		return out;
	}

	// Delete a package
	// Create/update and delete requires a uri which must be provided
	//Rest of answers included in the email

}
