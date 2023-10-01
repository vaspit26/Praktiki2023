
var file;
var pdf_name;

document.addEventListener("DOMContentLoaded", () => {
	
 	const dropBox = document.getElementById("dropBox");

  	dropBox.addEventListener("dragover", (e) => {
    	e.preventDefault();
    	dropBox.classList.add("highlight");
  	});

  	dropBox.addEventListener("dragleave", () => {
    	dropBox.classList.remove("highlight");
  	});

  	dropBox.addEventListener("drop", (e) => {
    	e.preventDefault();
    	dropBox.classList.remove("highlight");
    	file = e.dataTransfer.files;
    	handleFiles();
  	});

  	document.getElementById("fileInput").addEventListener("change", (e) => {
    	file = e.target.files;
    	handleFiles();
  	});
	
	
	function handleFiles() {
	
	    document.getElementById("fileContainer").innerHTML = ""; // Clear any previous content
	
		if (file[0].type === "application/pdf") {
		
			pdf_name = file[0].name;
	
	    	// Create a container PDF file
	    	const fileDiv = document.createElement("div");
			fileDiv.classList.add("file-item");
	
	        // Display the file name
	        const fileName = document.createElement("p");
	        fileName.textContent = file[0].name;
	
	        fileDiv.appendChild(fileName);
	
	        // Create a delete button
	        const deleteBtn = document.createElement("button");
	        deleteBtn.textContent = "Delete";
	        deleteBtn.classList.add("delete-btn");
	        deleteBtn.addEventListener("click", () => {
		        // Remove the file container when the delete button is clicked
		        fileDiv.remove();
		
		        // Clear the file input so that the same file can be selected again
		        const fileInput = document.getElementById("fileInput");
		        fileInput.value = null;
			});
	    	fileDiv.appendChild(deleteBtn);
	    	fileContainer.appendChild(fileDiv);
			
	    }else{
			window.alert("Please upload a PDF file to proceed");
		}
	}
});


function show_pdf(data){
	
	document.getElementById('choose_pdf').style.display ='none';	
	document.getElementById('continue_pdf').style.display ='block';
	document.getElementById('textSearch2').value = data;
	
}


function PDF_Handler(unities){
	
	// Send the PDF file to the server 
	const formData = new FormData();
	formData.append("pdfFile", file[0]);
	formData.append("unities",unities);
	
	
	fetch("PDF_Handler", {
		method: "POST",
	    body: formData,
	})
	.then(response => {
	    if (!response.ok) {
	        throw new Error(`HTTP error! Status: ${response.status}`);
	    }

   		return response.text();
	})
	.then(data => {
	    show_pdf(data);
	
	})
	.catch(error => {
	    console.error("Error while sending the PDF to the server:", error);
	});
}