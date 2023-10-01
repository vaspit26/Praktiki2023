import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.*;

/**
 * Servlet implementation class EntityDisplayServlet
 */
public class EntityDisplayServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final String DBPEDIA_SPARQL_ENDPOINT = "https://dbpedia.org/sparql";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EntityDisplayServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    private JSONObject executeSparqlQuery(String query) throws IOException {
    	
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
        String url = DBPEDIA_SPARQL_ENDPOINT + "?query=" + encodedQuery + "&format=json";

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/sparql-results+json");

        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        String responseText = EntityUtils.toString(entity);

        httpClient.close();

        JSONObject resultObject = null;
        
        // Parse the JSON response and extract the abstract and photo URL
        JSONObject jsonObject;
            
		try {
			
			jsonObject = new JSONObject(responseText);
				
			if (jsonObject.has("results")) {
				org.json.JSONArray results = jsonObject.getJSONObject("results").getJSONArray("bindings");
					
				if(results.length() >0) {
		
					String abstractText = results.getJSONObject(0).getJSONObject("abstract").getString("value");
					String photoUrl = results.getJSONObject(0).getJSONObject("thumbnail").getString("value");
						
				    // Create a JSON object to hold the abstract and photo URL
				    resultObject = new JSONObject();
				    resultObject.put("abstract", abstractText);
				    resultObject.put("thumbnail", photoUrl);
				}
			}
		
		} catch (JSONException e) {

			e.printStackTrace();
		}
        return resultObject;
    }
    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json;charset=UTF-8");
        
        String param = request.getParameter("input");
        
        JSONObject resultObject = null;
        
        String tool = request.getParameter("tool");
        
        if(tool.equals("WAT") || tool.equals("MyFile")) {
            try {

                String encoding = "UTF-8";

                // Wikipedia API URL to get page content
                String wikipediaApiJSON = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts|pageimages&exintro=&explaintext=&titles="
                        + URLEncoder.encode(param.replaceAll("https://en.wikipedia.org/wiki/", ""), encoding);

                // Extract page content
                HttpURLConnection con = (HttpURLConnection) new URL(wikipediaApiJSON).openConnection();
                con.addRequestProperty("User-Agent", "Mozilla/5.0");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                // Read line by line
                String responseSB = in.lines().collect(Collectors.joining());
                
                in.close();
                
                String abstractText; 
                
                
                String wordToCheck = "extract";

                // Create a regular expression pattern to match the word
                Pattern pattern = Pattern.compile("\\b" + Pattern.quote(wordToCheck) + "\\b");

                // Use a Matcher to find matches
                Matcher matcher = pattern.matcher(responseSB);

                if (matcher.find()) {
                	abstractText  = responseSB.split("extract\":\"")[1].split("\"}")[0];
                	String aText [] = abstractText.split("\",\"thumbnail\":");
                	String photoURL = null;
                    try {
                    	photoURL = responseSB.split("\"source\":\"")[1].split("\"")[0];
                    } catch (ArrayIndexOutOfBoundsException exception) {
                    	photoURL = null;
                    }
                    resultObject = new JSONObject();
        	        resultObject.put("abstract", aText[0]);
        	        resultObject.put("thumbnail", photoURL);
                } 

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
    			e.printStackTrace();
    		}
            
            PrintWriter out = response.getWriter();
            
            if(resultObject == null) {
            	
            	out.println(resultObject);
            	
            }else {
            
            	out.println(resultObject.toString());
            }
        }else if(tool.equals("DBpedia")){
        	
        	String query = "prefix dbpedia: <http://dbpedia.org/resource/>\r\n"
        		    + "prefix dbpedia-owl: <http://dbpedia.org/ontology/>\r\n"
        		    + "\r\n"
        		    + "select ?abstract ?thumbnail where { \r\n"
        		    + "  dbpedia:" + param + " dbpedia-owl:abstract ?abstract ;\r\n"
        		    + "                   dbpedia-owl:thumbnail ?thumbnail .\r\n"
        		    + "  filter(langMatches(lang(?abstract),\"en\"))\r\n"
        		    + "}";

            // Send the SPARQL query to DBpedia and get the result
            resultObject = executeSparqlQuery(query);
            
            // Write the JSON response directly to the servlet response
            if(resultObject!=null) {
            	PrintWriter out = response.getWriter();
            	out.println(resultObject.toString());
            }else {
            	PrintWriter out = response.getWriter();
            	out.println("null");
            }
        }
	}

}
