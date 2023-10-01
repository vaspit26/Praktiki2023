
var obj = [];
var obj2 = [];
var Resources = [];
var c = 0;
var tool = -1;
var unities = "";
var pointer = 0;
var pdf = 0;
var text = 0;

function openModal(){
	
	var modal = document.getElementById("myModal");
  	modal.style.display = "block";
}

//Function to close the modal

function closeModal() {
	
  	var modal = document.getElementById("myModal");
  	modal.style.display = "none";
	unitycheck.checked = false;
	document.getElementById("info").value = "";
}

function submitUnities() {
	
	unities = document.getElementById("info").value;
	
	var modal = document.getElementById("myModal");
  	modal.style.display = "none";
}

function set_tools0(v){
	
	if(v == 0){ //My Configuration checked
		if(tool01.checked == true){
			if(tool === -1){
				tool = 0;
			}else if(tool === 1){
				tool = 3;
			}else if(tool === 2 ){
				tool = 4; 
			}else if(tool === 5){
				tool = 6;
			}
		}else{
			if(tool === 0){
				tool = -1;
			}else if(tool === 3){
				tool = 1;
			}else if(tool === 4){
				tool = 2;
			}else if(tool === 6 ){
				tool = 5; //no checkbox
			}
		}
	}
	
	if(v == 1 ){ //DBpedia tool checked
		
		if(tool11.checked == true){
			if(tool === -1 ){
				tool = 1; //no checkbox
			}else if(tool === 0){
				tool = 3;
			}else if(tool === 2){
				tool = 5;
			}else if(tool === 4){
				tool = 6;
			}	
		}else{
			if(tool === 1){
				tool = -1;
			}else if(tool === 3){
				tool = 0;
			}else if(tool === 5 ){
				tool = 2; //no checkbox
			}else if(tool === 6){
				tool = 4;
			}
		}
		
	}else if(v == 2){
		
		if(tool21.checked == true){
			if(tool === -1){
				tool = 2;
			}else if(tool === 0){
				tool = 4;
			}else if(tool === 1){
				tool = 3;
			}else if(tool === 3){
				tool = 6;
			}
		}else{
			if(tool === 2){
				tool = -1;
			}else if(tool === 4){
				tool = 0;
			}else if(tool === 5 ){
				tool = 1; //no checkbox
			}else if(tool === 6){
				tool = 3;
			}	
		}
	}
}

function set_tools(v){
	
	if(v == 0){ //My Configuration checked
		if(tool0.checked == true){
			if(tool === -1){
				tool = 0;
			}else if(tool === 1){
				tool = 3;
			}else if(tool === 2 ){
				tool = 4; 
			}else if(tool === 5){
				tool = 6;
			}
		}else{
			if(tool === 0){
				tool = -1;
			}else if(tool === 3){
				tool = 1;
			}else if(tool === 4){
				tool = 2;
			}else if(tool === 6 ){
				tool = 5; //no checkbox
			}
		}
	}
	
	if(v == 1 ){ //DBpedia tool checked
		
		if(tool1.checked == true){
			if(tool === -1 ){
				tool = 1; //no checkbox
			}else if(tool === 0){
				tool = 3;
			}else if(tool === 2){
				tool = 5;
			}else if(tool === 4){
				tool = 6;
			}	
		}else{
			if(tool === 1){
				tool = -1;
			}else if(tool === 3){
				tool = 0;
			}else if(tool === 5 ){
				tool = 2; //no checkbox
			}else if(tool === 6){
				tool = 4;
			}
		}
		
	}else if(v == 2){
		
		if(tool2.checked == true){
			if(tool === -1){
				tool = 2;
			}else if(tool === 0){
				tool = 4;
			}else if(tool === 1){
				tool = 3;
			}else if(tool === 3){
				tool = 6;
			}
		}else{
			if(tool === 2){
				tool = -1;
			}else if(tool === 4){
				tool = 0;
			}else if(tool === 5 ){
				tool = 1; //no checkbox
			}else if(tool === 6){
				tool = 3;
			}	
		}
	}
	
}

function reset_tool(){
	
	tool = -1;
	tool0.checked = false;
	tool1.checked = false;
	tool2.checked = false;
	tool01.checked = false;
	tool11.checked = false;
	tool21.checked = false;
}

function input_format(){
	
	document.getElementById('initial_page').style.display ='block';
	document.getElementById('input').style.display ='none';	
	document.getElementById('choose_pdf').style.display ='none';
	document.getElementById('choose_txt').style.display ='none';
	document.getElementById('continue_pdf').style.display ='none';	
	document.getElementById('entities_recognized').style.display ='none';
	document.getElementById('result_display').style.display ='none';
	document.getElementById('extract_button').style.display ='none';
	document.getElementById('container_photo').style.display ='none';
	document.getElementById("previous").style.display = 'none';
	document.getElementById("next").style.display = 'none';
	document.getElementById('textSearch').value = "";
	document.getElementById("info").innerHTML = "";
	document.getElementById("image").innerHTML = "";
	document.getElementById("resource").innerHTML = "";
	document.getElementById("text").innerHTML = "";
	document.getElementById("entity_name").innerHTML = "";
	
	document.getElementById('given_input').innerHTML ="";
	unitycheck.checked = false;
	document.getElementById("info").value = "";
	reset_tool();
	obj = [];
	obj2 = [];
	Resources = [];
	c = 0;
	unities = "";
	pointer = 0;
	pdf = 0;
	text = 0;
}

function pdf_input(){
	
	document.getElementById('input').style.display ='block';	
	document.getElementById('choose_pdf').style.display ='block';	
	document.getElementById('initial_page').style.display ='none';
	pdf = 1;
	

}

function text_input(){
	
	document.getElementById('input').style.display ='block';
	document.getElementById('choose_txt').style.display = 'block';	
	document.getElementById('initial_page').style.display ='none';
	text = 1;
}


function do_search(myinput){
	
	if(tool === -1){
		
		window.alert("No selected tool!");
	}else{
	
		$.ajax({
					url: 'EntityExtractionServlet',
					method: "POST",
					data: {input:myinput,tool:tool},
					success: function (response) {
						
						const jsonArray = JSON.parse(JSON.stringify(response.forms));

						jsonArray.forEach((objectString) => {
						
							const parsedObject = JSON.parse(objectString);
							const found = parsedObject.found;
							const resource = parsedObject.resource;
							const uri = parsedObject.uri;
							const content = parsedObject.content;
							
							if(found == true){
								Resources.push({Resource: resource, Content: content, URI: uri});
							}
					
							obj.push({Found: found, Resource: resource, URI: uri, Content: content, displayed:'false', times_found:0});
									
						});
						
						display_result();
						
				  	},
				  	error: function (jqXHR, exception) {
			                 console.log('Error occured!!');
			              }
			  	});	
	}
}

function search_units(input2){
	
	var res = unities.split(/\s*-\s*/);
	console.log(res);
	
	const startIndex = input2.indexOf(res[0]);
    
    if (startIndex !== -1) {
        const endIndex = input2.indexOf(res[1], res[0] + res[0].length);
        
        if (endIndex !== -1) {
            const result = input2.substring(startIndex + res[0].length, endIndex);
            return result;
        } else {
            console.error(`End keyword "${endKeyword}" not found after start keyword "${startKeyword}"`);
            return input2;
        }
    } else {
        console.error(`Start keyword "${startKeyword}" not found`);
        return input2;
    }

}

function search(){
	
	
	input = document.getElementById("textSearch").value;
	input2 = document.getElementById("textSearch2").value;
		
	if(input!=="" && text === 1){
		do_search(input);
	}else if(input2 !== "" && pdf === 1 ){
		//Filter input, if unities
		if(unities!==""){
			input3 = search_units(input2);
			do_search(input3);
		}else{
			do_search(input2);	
		}
			
	}else{
			
		window.alert("No given input!");
	}
}

function display_result(){
	
	document.getElementById('result_display').style.display ='block';
	document.getElementById('entities_recognized').style.display ='block';
	document.getElementById('extract_button').style.display ='block';
	document.getElementById('container_photo').style.display ='block';
		
	document.getElementById('input').style.display ='none';
	
	display_entities();
	
	document.getElementById('entities_recognized').innerHTML ="Entities Recognized: "+ entities_num();
	document.getElementById('given_input').innerHTML = put_input();	
	
	
}

function makeAjaxRequest(obj){
		
	var param;
	
	var resource = obj.Resource;
	
	if(resource === 'MyFile' || resource === 'WAT'){
		param = obj.URI;
	}else if(resource === 'DBpedia'){
		var pathname = new URL(obj.URI).pathname;
		const myArray = pathname.split("/");		
		param = myArray[2];
	}
	
	return new Promise(function(resolve, reject) {
	
		$.ajax({
			url: 'EntityDisplayServlet',
			method: "GET",
			data: {input:param,tool:resource},
			success: function (response) {	
				resolve(response);
							
			},
			error: function (jqXHR, exception) {
				reject(error);
			}
		});	
	});
	
}

// Custom function to manage AJAX requests based on Found of each object

function processObject(obj) {
	
	return new Promise(function(resolve, reject) {
	
		makeAjaxRequest(obj)
        .then(function(response) {   
          // Resolve the Promise once the AJAX request is completed
			resolve(response);
        })
        .catch(function(error) {
	
			console.error(error); 

          	// Resolve the Promise even if there's an error to continue with the loop
          	resolve(null);

        });
  });
}

function createCarousel(){

	document.getElementById("container_photo").style.display = 'block';	
	showSlide(0);
	if(obj2.length>1){
		document.getElementById("previous").style.display = 'block';
		document.getElementById("next").style.display = 'block';	
	}	
} 

//Modify basic_container 
function showSlide(i){
	
	im = obj2[i].Photo;	
	if(im === null){
		
		document.getElementById("image").innerHTML = "";
	}else{
		
		document.getElementById("image").innerHTML = "<img src="+im+">";
	}
	
	
	document.getElementById("resource").innerHTML =obj2[i].Resource;
	document.getElementById("entity_name").innerHTML = obj2[i].Content;

	if(obj2[i].Text === null){
		
		document.getElementById("text").innerHTML = "";
	}else{
	
		document.getElementById("text").innerHTML = obj2[i].Text;
	}

}

function display_entities() {	
	
	let promises = [];
	
	entities_num();
	
	for (const myobj of Resources) {
    	promises.push(processObject(myobj));
  	}

  	// Wait for all Promises to resolve
  	Promise.all(promises)
    	.then(function(responses) {
	
	    for (const response of responses) {
        	if (response !== null) {
				
				var jsonObject = JSON.parse(JSON.stringify(response));
				var photo = jsonObject.thumbnail;
				var text = jsonObject.abstract;
				
				if(pointer<Resources.length){	
					
					obj2.push({Photo: photo, Text: text, Resource: Resources[pointer].Resource, Content: Resources[pointer].Content});
					pointer++;
				}

        	}else{

				obj2.push({Photo: null, Text: null, Resource: Resources[pointer].Resource, Content: Resources[pointer].Content});
				pointer++;
			}
      	}
		
		if(obj2.length>0){
			createCarousel(0);
		}
	
    })
    .catch(function(error) {
	      
	      console.error(error);
    });


}

function nextSlide() {
	
	c++;
	
	if(c>=obj2.length){
		c = 0;
	}
	
	showSlide(c);
	
}

function prevSlide() {

	c--;
	
	if(c<0){
		c = obj2.length - 1;
	}
  	
	showSlide(c);

}

function entities_num(){

	var flag;
	var number = 0;
	
	for(var i = 0; i<obj.length; i++){ 
		
		if(obj[i].Found){
			
			flag = true;
			
			for(j=0; j<i; j++){
				
				if(obj[i].Content.toLowerCase() === obj[j].Content.toLowerCase()){
					
					obj[j].times_found++;
					flag = false;
					break;
				}
			}
			
			if( flag ){
				obj[i].displayed = 'true';
				obj[j].times_found = 1;
				number++;
			}
		}
	}
	
	return number;
}

function put_input(){
	
	input = "";
	
	for(var i = 0; i<obj.length; i++){
		
		if(obj[i].Found){
			input+='<a href='+obj[i].URI+' target=\"_blank\">'+obj[i].Content+'</a>';
			
		}else{
			input+=obj[i].Content;
			
		}
		
		input += " ";
	}
	
	return input;
	
}