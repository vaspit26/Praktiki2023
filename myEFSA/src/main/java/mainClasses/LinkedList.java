package mainClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class Node {
    Word w;
    Node next;
};

public class LinkedList {
	
	public Node head;
	
	public LinkedList(){
		head = null;
	}
	
	
	public int length() {
		
		int list_length = 0;
		
		Node tmp = new Node();
		tmp = head;
		
		while(tmp.next != null) {
			
			list_length++;
			tmp = tmp.next;
		}
		
		return list_length;
		
	}
	
	public void add(Word word) {
		
		Node newNode = new Node();
		newNode.w = new Word(word);
		
		if(head == null) {
			head = newNode;
		}else {
			Node tmp = new Node();
			tmp = head;
			while(tmp.next != null) {
				tmp = tmp.next;
			}
			
			tmp.next = newNode;
			
		}
	}
	
	public void add(Word word,int pos) {
		
		Node newNode = new Node(); 
	    newNode.w = new Word(word);
	    newNode.next = null;

	    if(pos < 1 || pos > length()) {
	      System.out.println("\nNot valid position");
	    } else if (pos == 1) {
	      newNode.next = head;
	      head = newNode;
	    } else {
	      
	      Node tmp = new Node();
	      tmp = head;
	      for(int i = 1; i < pos-1; i++) {
	        if(tmp != null) {
	          tmp = tmp.next;
	        }
	      }
	   
	      if(tmp != null) {
	        newNode.next = tmp.next;
	        tmp.next = newNode; 
	      } else {
	        System.out.print("\nThe previous node is null.");
	      }       
	    }
		
	}
	
	public void PrintList() {
		
		Node temp = new Node();
	    temp = this.head;
	    if(temp != null) {
	      System.out.print("The list contains: ");
	      while(temp != null) {
	        System.out.print(temp.w.getContent() + " ");
	        temp = temp.next;
	      }
	      System.out.println();
	    } else {
	      System.out.println("The list is empty.");
	    }
	  }    
	
	public JSONArray LinkedList_to_JSON() throws JSONException {
		
	    JSONArray jsonArray = new JSONArray();
	    
	    Node temp = new Node();
	    try {
			temp = head;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    while(temp != null) {
	    	
	    	JSONObject obj = new JSONObject();
		    obj.put("content", temp.w.getContent());
		    obj.put("uri", temp.w.getUri());
		    obj.put("resource", temp.w.getResource());
		    obj.put("found", temp.w.getFound());
		    jsonArray.put(obj.toString());
	    	
	    	temp = temp.next;
	    }
	    
	    return jsonArray;
		
	}

}
