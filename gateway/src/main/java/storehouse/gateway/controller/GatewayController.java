package storehouse.gateway.controller;

import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import storehouse.gateway.service.GatewayService;

@RestController
@RequiredArgsConstructor
public class GatewayController {
	final GatewayService gatewayService;
	
	@RequestMapping(value = "/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
	ResponseEntity<byte[]> getProxy(ProxyExchange<byte[]> exchange, HttpServletRequest request) {
		return gatewayService.proxyRouting(exchange, request, request.getMethod());
	}
	
}

