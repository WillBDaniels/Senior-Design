package webapptest;

import com.btr.proxy.search.ProxySearch;
import com.btr.proxy.util.PlatformUtil;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles everything to do with establishing the existence of, and
 * traversal through, various proxies which exist upon a clients network which
 * they are communicating out of This class leverages the Proxy Vole library in
 * order to help facilitate the proxy handling.
 *
 * @author William
 */
public class ProxyHandler {

    private final String serverLocation = "http://54.175.166.105";
    //private final String serverLocation = "http://54.175.166.105";
    private final int serverPort = 80;

    /**
     * This method attempts to get the proxy settings the user has
     * automatically. If it can't find the settings, simply nothing happens, and
     * it's assumed that there is no proxy.
     *
     * @return a map that contains all of the mappings.
     */
    public Map<String, Integer> getProxySettings() {
        Map<String, Integer> proxyAddress = new HashMap<>();
        try {
            ProxySearch proxySearch = new ProxySearch();
            if (PlatformUtil.getCurrentPlattform() == com.btr.proxy.util.PlatformUtil.Platform.WIN) {
                proxySearch.addStrategy(ProxySearch.Strategy.IE);
                proxySearch.addStrategy(ProxySearch.Strategy.FIREFOX);
                proxySearch.addStrategy(ProxySearch.Strategy.JAVA);
            } else if (PlatformUtil.getCurrentPlattform() == com.btr.proxy.util.PlatformUtil.Platform.LINUX) {
                proxySearch.addStrategy(ProxySearch.Strategy.GNOME);
                proxySearch.addStrategy(ProxySearch.Strategy.KDE);
                proxySearch.addStrategy(ProxySearch.Strategy.FIREFOX);
            } else {
                proxySearch.addStrategy(ProxySearch.Strategy.OS_DEFAULT);
            }

            ProxySelector myProxySelector = proxySearch.getProxySelector();

            ProxySelector.setDefault(myProxySelector);
            if (ProxySelector.getDefault() == null) {
                return proxyAddress;
            }
            Proxy proxy = (Proxy) ProxySelector.getDefault().
                    select(new URI(serverLocation + ":" + serverPort)).iterator().
                    next();
            InetSocketAddress addr = (InetSocketAddress) proxy.address();
            if (addr != null) {
                proxyAddress.put(addr.getHostName(), addr.getPort());
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace(System.err);
        }
        return proxyAddress;
    }

}
