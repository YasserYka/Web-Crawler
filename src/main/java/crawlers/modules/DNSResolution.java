package crawlers.modules;

import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

import io.netty.resolver.dns.DnsNameResolver;
import io.netty.resolver.dns.DnsNameResolverBuilder;

public class DNSResolution {
		
	private DnsNameResolver resolver;

	public void init() {
		resolver = new DnsNameResolverBuilder().build();
		
	}
	
	public InetAddress resolve(String hostname) {
		InetAddress address = null;
		try {address = resolver.resolve(hostname).get();} catch (InterruptedException | ExecutionException e) {/*TODO: LOG IT*/e.printStackTrace();}
		return address;
	}
}
