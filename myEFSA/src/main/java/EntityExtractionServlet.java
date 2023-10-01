

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mainClasses.LinkedList;
import mainClasses.Word;

/**
 * Servlet implementation class EntityExtractionServlet
 */
public class EntityExtractionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	LinkedList inputlist = null;
	
	String WATapiKey = "a8a04b8b-ee85-40ed-8af4-8f8f207acd9a-843339462";
	String BabelfyapiKey = "a5827316-3f38-41d7-84be-8760b2bd21e3";
	String language = "en";
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EntityExtractionServlet() {
        super();
    }
    
public String make_inputformat(String input) {
		
		String[] strNums;
		
		strNums = input.split("[' ','\n','\r','\n\r]");
		
		String output = "";
		
		int i=0;
		
		/* Make the right input format */
		
		while( i<strNums.length) {
			
			if(i==0) {
				output=strNums[i];
			}else {
				output+="%20"+strNums[i];
			}
			
			i++;
		}
		
		return output;
	}
	
	private static String convertStreamToString(InputStream is) {

	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
	}
	
	public static LinkedList customSplit(String inputString, Word[] delimiters) {
		
        LinkedList result = new LinkedList();
        int startIndex = 0;
        
        for (int i=0; i<delimiters.length; i++) {
        	if(delimiters[i]!=null) {
	            int delimiterIndex = inputString.indexOf(delimiters[i].getContent(), startIndex);
	            if (delimiterIndex != -1) {
	
	                result.add(new Word(inputString.substring(startIndex, delimiterIndex),false));
	                result.add(delimiters[i]);
	                startIndex = delimiterIndex + delimiters[i].getContent().length();
	            }
        	}
        }

        // Add the remaining part of the inputString after the last delimiter
        if (startIndex < inputString.length()) {
            result.add(new Word(inputString.substring(startIndex),false));
        }

        return result;
    }
	
	public static String customsplit(String inputString,String delimeter,int i) {
		
		String [] result = inputString.split(delimeter);
		
		if(result.length>=i) {
			return result[i];
		}else {
		
			return "";
		}
		
	}
    
	public static String[] splitText(String text, String[] delimiters) {

	    List<String> result = new ArrayList<>();
	    int startIndex = 0;

	    for (String delimiter : delimiters) {

	        int delimiterIndex = text.indexOf(delimiter, startIndex);

	        if (delimiterIndex >= 0) {

	            // Add the text before the delimiter
	            String input = text.substring(startIndex, delimiterIndex);

	            if (input.endsWith(" ")) {
	                // Remove the trailing space using trim()
	                input = input.trim();
	            }

	            if (!input.isEmpty()) {
	                result.add(input);
	            }

	            // Add the delimiter itself
	            result.add(delimiter);

	            // Update the start index for the next iteration
	            startIndex = delimiterIndex + delimiter.length();
	        }
	    }

	    // Add the remaining text if any
	    String input = text.substring(startIndex).trim();
	    if (!input.isEmpty()) {
	        result.add(input);
	    }

	    // Convert the List to an array
	    return result.toArray(new String[0]);
	}


	public JSONObject getWordList(LinkedList inputlist) throws JSONException {

	    JSONObject responseDetailsJson = new JSONObject();
	    JSONArray jsonArray = new JSONArray();
	    
	    jsonArray = inputlist.LinkedList_to_JSON();
	    
	    responseDetailsJson.put("forms", jsonArray);//Here you can see the data in json format
	    
	    return responseDetailsJson;

	}
	
public void search_elsewhere2(String text) throws IOException {
		
		BufferedReader br ; 
	    String result = null;
	    int array_length = 0;
		String delimeters [];
		String input = make_inputformat(text);
		
		URL url;
		Word w = null;
		HttpURLConnection con = null;
		
		url = new URL("https://wat.d4science.org/wat/tag/tag?lang=en&gcube-token="+WATapiKey+"&text="+input);
		con = (HttpURLConnection) url.openConnection();

		// Set the request method to GET
		con.setRequestMethod("GET");
	    
	    if (con.getResponseCode() == 200) {      
            try {
            	
            	result = convertStreamToString(con.getInputStream());
            		
				JSONObject myObject = new JSONObject(result);
				JSONArray array = null;
					
				array = myObject.optJSONArray("annotations");
					
				if(array!=null) {
					
					array_length = array.length();
					
					delimeters = new String[array_length];
					
					for(int i = 0; i < array_length; i++){
						
						delimeters[i] = array.getJSONObject(i).get("spot").toString();
					}
					
					String[] res = splitText(text, delimeters);
							
					int counter = 0;
					
					for (int i=0; i<res.length; i++) {
						
						if(counter<delimeters.length) {
							
							if(res[i].equals(delimeters[counter])) { //Found with tool
								
								w = new Word(res[i],"https://en.wikipedia.org/wiki/"+array.getJSONObject(counter).get("title").toString(),"WAT",true);
								inputlist.add(w);
								counter++;
							}else { //Not found
								
								w = new Word(res[i],false);
								inputlist.add(w);
							}
							
						}else { //Not found
							
							w = new Word(res[i],false);
							inputlist.add(w);
						}
					}
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
	            
	    } else {
	    	
	        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	        String strCurrentLine;
	        while ((strCurrentLine = br.readLine()) != null) {
	        	System.out.print(strCurrentLine);
	        }
	    }
	}
	
	public void search_elsewhere(String text,int tool) throws IOException {
		
		BufferedReader br ; 
	    String result = null;
	    int array_length = 0;
		String delimeters [];
		String input = make_inputformat(text);
		URL url;
		Word w = null;
		HttpURLConnection con = null;
		
		/*Connect with REST API of DBpedia*/
		
		url = new URL("https://api.dbpedia-spotlight.org/en/annotate?text="+input);
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept", "application/json");
		    
	    
	    if (con.getResponseCode() == 200) {      
            
            try {
            	
            	result = convertStreamToString(con.getInputStream());
            		
				JSONObject myObject = new JSONObject(result);
				JSONArray array = null;

				array = myObject.optJSONArray("Resources");
					
					
				if(array!=null) {
					
					array_length = array.length();
					
					delimeters = new String[array_length];
					
					for(int i = 0; i < array_length; i++){
						
						delimeters[i] = array.getJSONObject(i).get("@surfaceForm").toString();

					}
					
					String[] res = splitText(text, delimeters);
							
					int counter = 0;
					
					for (int i=0; i<res.length; i++) {
						
						if(counter<delimeters.length) {
							
							if(res[i].equals(delimeters[counter])) { //Found with tool
								
								w = new Word(res[i],(array.getJSONObject(counter).get("@URI")).toString(),"DBpedia",true);

								inputlist.add(w);
								counter++;
							}else { //Not found
								if(tool == 5 ) { //If it choosen search with other tool 
									search_elsewhere2(res[i]);
								}else {
									w = new Word(res[i],false);
									inputlist.add(w);
								}
							}
							
						}else { //Not found
							if( tool == 5 ) { //If it choosen search with other tool 
								search_elsewhere2(res[i]);
							}else {
								w = new Word(res[i],false);
								inputlist.add(w);
							}
						}
					}
				}else if(tool == 1){
					w = new Word(text,false);
					inputlist.add(w);
				}else {
					search_elsewhere2(text);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
	            
	    } else {
	        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	        String strCurrentLine;
	        while ((strCurrentLine = br.readLine()) != null) {
	        	System.out.print(strCurrentLine);
	        }
	    }
	}
	
	public static String [] customsplit(String inputString) {
		
		String [] result = inputString.split(" ");
			
			
		return result;
			
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String text = request.getParameter("input");

		inputlist = new LinkedList();
		Word w [];
		
		String t = request.getParameter("tool");

		int tool = Integer.parseInt(t);
		int n = 0;
		 
		String [] input_words ;
		String keep = "";
		String keep_notfound = "";
		String searchingfor;
		String URL = "";
			
		input_words = customsplit(text);
		searchingfor = input_words[0];
		
		w = new Word[input_words.length];
		
		int search_tool = 0;
		
		if(tool == 3) {
			search_tool = 1;
		}else if(tool == 4){
			search_tool = 2;
		}else if(tool == 6) {
			search_tool = 5;
		}else {
			search_tool = tool;
		}
			
			
		if(tool == 0 || tool == 3 || tool == 4 || tool == 6 ) {
		
			try( FileInputStream fis = new FileInputStream(new File("mytool.xlsx"));
	
			Workbook workbook = new XSSFWorkbook(fis)) {
	
				Sheet sheet = workbook.getSheetAt(0);
				String cellValue;
	
				int start = 0; /*pointer to word to search for*/
				boolean found; /*was an entity found flag*/
				int j; /*second loop pointer*/
				int loopcounterj = 0;
				boolean loop_out = false; /*If found get out of the loop */
				     
				while(start<input_words.length) {
				    	 
					j=start;
				    loopcounterj = 0;
				    found = false;
				    	 
				    while(j<input_words.length) { /*combine the words to get longest prefix*/
				    		 
				    	loop_out = false;
					    	 
					    if( loopcounterj == 0 ) {
					    	searchingfor = input_words[j];
					    }
					    	 
					    loopcounterj++;
	
					    	 
					    for (org.apache.poi.ss.usermodel.Row row : sheet) {
	
					    	org.apache.poi.ss.usermodel.Cell firstCell = row.getCell(0); // Get the first cell in the row
						    	 
						    if (firstCell != null && !loop_out) {    		 
						    		 
						    	cellValue = firstCell.toString();
						    		 
						    	if(cellValue.equals(searchingfor)) {
						    		org.apache.poi.ss.usermodel.Cell secondCell = row.getCell(1); // Get the first cell in the row
						    	    URL = secondCell.toString();
						    	    found = true ;
						    	    keep = searchingfor;
						    	    start++; //To brhka so deixne sthn epomenh
						    	    loop_out = true;
						    	}	     
						    } //else break    	 
						}
					    	 	 
				    	j++;
	
				    	     
				    	if(j<input_words.length) {  //Longest Prefux Match
				    		searchingfor +=" ";
				    	    searchingfor += input_words[j];
				    	    	 
				    	}else { //Longest done
				    		if(!found) { //if no entity is recognized
				    			if(keep_notfound == "") {
				    				
				    				keep_notfound = input_words[start];
						    	}else {
						    		
						    		keep_notfound += " ";
						    		keep_notfound += input_words[start];
						    	}
						    		 
						    	start++;
						    		 
						    }else { //Some entity is recognized
						    	if(keep_notfound !="") { //Handle the entities before the one recognized
						    		if(tool != 0) {
						    			
						    			search_elsewhere(keep_notfound,search_tool);
						    		}else {
						    			w[n] = new Word(keep_notfound,false);
									    inputlist.add(w[n]);
										n++;
						    		}
						    		
						    		keep_notfound ="";
						    	}
	
						    	w[n] = new Word(keep,URL,"MyFile",true);
						    	inputlist.add(w[n]);
								n++; 
						    		 
						    }    		  
				    	}	     
				    }
				}
				     
				if(keep_notfound !="" && tool!=0) {
						
					search_elsewhere(keep_notfound,search_tool);
				}else {
					w[n] = new Word(keep_notfound,false);
				    inputlist.add(w[n]);
				}
				
				response.setContentType("application/json");
				response.getWriter().write(getWordList(inputlist).toString());
				     
			} catch (IOException e) {
				e.printStackTrace();
				
			} catch (JSONException e) {
				e.printStackTrace();
				
			}
		}else {
			
			if(tool == 2) {

				search_elsewhere2(text);
			}else {
			
				search_elsewhere(text,search_tool);
			}
			
			response.setContentType("application/json");
			try {
				response.getWriter().write(getWordList(inputlist).toString());
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
