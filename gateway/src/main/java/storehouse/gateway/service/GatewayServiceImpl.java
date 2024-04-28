package storehouse.gateway.service;

import java.security.KeyStore.Entry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GatewayServiceImpl implements GatewayService {
	@Value("#{${app.map.hosts.ports}}")
	Map<String, String> hostsPortsMap;

	@Override
	public ResponseEntity<byte[]> proxyRouting(ProxyExchange<byte[]> proxyExchange, HttpServletRequest request,
			String httpMethod) {
		String url = getUrl(request);
		ResponseEntity<byte[]> res;
		if(url == null) {
			res = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} else {
			res = switch(httpMethod) {
			case "GET" -> proxyExchange.uri(url).get();
			case "PUT" -> proxyExchange.uri(url).put();
			case "POST" -> proxyExchange.uri(url).post();
			case "DELETE" -> proxyExchange.uri(url).delete();
			default -> throw new IllegalArgumentException("Wrong HTTP method");
			};
		}

		return res;
	}

	private String getUrl(HttpServletRequest request) {
		String uri = request.getRequestURI(); //  /accounts/admin@tel-ran.co.il
		String[] uriParts = uri.split("/+");
		log.trace("received URI parts: {}", Arrays.deepToString(uriParts));

		String res = null;
		if(uriParts.length != 0 && uriParts != null) {		
			String key = null;
			String acc = "";
			int i = 1;
			while (key == null && i < uriParts.length) {
				acc = acc + "/" + uriParts[i];
				log.trace("intermediate key for hostsPortsMap: {}", acc);
				i ++;
				if(hostsPortsMap.containsKey(acc)) {
					log.trace("hostsPortsMapresult by key:{}", hostsPortsMap.get(acc));
					key = acc;
				}
			}

			if(key != null) {
				log.trace("key for hostsPortsMap: {}", key);
				String hostPort = hostsPortsMap.get(key);
				res = String.format("%s%s", hostPort, uri);
			}
		}
		log.debug("result URL is {}", res);
		return res;
	}

	@PostConstruct
	void logHostsPortsMap() {
		log.debug("hosts-ports map is: {}",hostsPortsMap);
	}
}
