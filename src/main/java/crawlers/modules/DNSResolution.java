package crawlers.modules;

import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.resolver.ResolvedAddressTypes;
import io.netty.resolver.dns.DnsNameResolver;
import io.netty.resolver.dns.DnsNameResolverBuilder;
import jodd.util.LoopIterator;

public class DNSResolution {
		
	private static NioEventLoopGroup  loopGroup = new NioEventLoopGroup(1);
	private static DnsNameResolverBuilder builder = new DnsNameResolverBuilder(loopGroup.next()).channelType(NioDatagramChannel.class).resolvedAddressTypes(ResolvedAddressTypes.IPV4_PREFERRED).queryTimeoutMillis(5000).optResourceEnabled(false).ndots(1);
	private static DnsNameResolver resolver = builder.build();
			
	public static InetAddress resolveHostnameToIP(String hostname) {
		InetAddress address = null;
		try {address = resolver.resolve(hostname).get();} catch (InterruptedException | ExecutionException e) {/*TODO: LOG IT*/e.printStackTrace();}
		return address;
	}
}
