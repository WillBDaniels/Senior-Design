package edu.csci.standalone_server;

//import com.magvar.mvencryption.MVCipher;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 12-2014
 *
 * @author William
 */
public class ParameterFilter extends Filter {

    /**
     * This is simply a string descriptor of the filter.
     *
     * @return a String description that tells what this filter does.
     */
    @Override
    public String description() {
        return "Parses the requested URI for http post parameters";
    }

    /**
     * This overriden method is a consequence of extending the 'Filter' abstract
     * class. This allows us to create our own specific filter.
     *
     * @param exchange the HttpExchange obtained from the client.
     * @param chain ???
     * @throws IOException
     */
    @Override
    public void doFilter(HttpExchange exchange, Chain chain)
            throws IOException {

        parseGetParameters(exchange);
        parsePostParameters(exchange);
        chain.doFilter(exchange);
    }

    private void parseGetParameters(HttpExchange exchange)
            throws UnsupportedEncodingException {
        Map<String, Object> parameters = new HashMap<>();
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
        exchange.setAttribute("parameters", parameters);
    }

    @SuppressWarnings("unchecked")
    private void parsePostParameters(HttpExchange exchange)
            throws IOException {

        if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, Object> parameters
                    = (Map<String, Object>) exchange.getAttribute("parameters");
            InputStreamReader isr
                    = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String item = br.readLine();
            String query = item;
            parseQuery(query, parameters);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseQuery(String query, Map<String, Object> parameters)
            throws UnsupportedEncodingException {
        //MVCipher dc = new MVCipher();

        if (query != null) {
            String pairs[] = query.split("[&]");

            for (String pair : pairs) {
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                    //key = dc.decrypt(key);
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                    //value = dc.decrypt(value);
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}
