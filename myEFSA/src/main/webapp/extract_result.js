/**
 * @author Vasia Pitikaki
 * Javascript for Extracting Found entities
 */

var filteredObj = [];

function extract_pdf() {
    const pdf = new jsPDF();
    let pdfElement = "<h1>Entity Extraction</h1>";
    pdfElement += "<h2><p>Entities Recognized : " + entities_num() + "</p></h2>";

    let verticalPosition = 40; // Set an initial vertical position

    // Add the initial content to the PDF
    pdf.fromHTML(pdfElement, 15, verticalPosition, {
        width: 180, // Adjust the width as needed
    });

    // Reset pdfElement for the content within the loop
    pdfElement = "";

    for (let i = 0; i < obj2.length; i++) {
        pdfElement += "<h3>"+Resources[i].Content +"</h3>";
        pdfElement += "<br>";
        pdfElement += "<h4><p>"+Resources[i].Resource +"</p></h4>";
        pdfElement += "<br>";
        pdfElement += "<h4><p>"+Resources[i].URI +"</p></h4>";
        pdfElement += "<br>";
        pdfElement += "<h5><p style='font-size: 10px;'>" + obj2[i].Text + "</p></h5>";
        pdfElement += "<br>";

        // Calculate the content height
        const contentHeight = pdf.getTextDimensions(pdfElement).h;

        // Check if the content fits on the current page
        if (verticalPosition + contentHeight <= pdf.internal.pageSize.height - 20) {
            // If it fits, add the content to the current page
            pdf.fromHTML(pdfElement, 15, verticalPosition, {
                width: 180, // Adjust the width as needed
            });
            verticalPosition += contentHeight + 15; // Update the top margin
        } else {
            // If it doesn't fit, add a new page and reset the top margin
            pdf.addPage();
            verticalPosition = 15;
            // Add the content to the new page
            pdf.fromHTML(pdfElement, 15, verticalPosition, {
                width: 180, // Adjust the width as needed
            });
            verticalPosition += contentHeight + 15; // Update the top margin
        }

        // Reset pdfElement for the next iteration
        pdfElement = "";
    }

    // Save or display the PDF as needed
    pdf.save("EnXtractify.pdf");
}


function extract_csv(){
	
	for(let i=0; i<obj.length; i++){
		
		if(obj[i].displayed === 'true'){
			filteredObj.push({Resource: obj[i].Resource, URI: obj[i].URI, Content: obj[i].Content, times_found:obj[i].times_found});
		}
	}
	
	if (filteredObj.length > 0) {
  		createAndDownloadCSV(filteredObj, 'EntityExtraction_data.csv');
	} else {
  		console.log('No data with the flag set to true.');
	}
	
}

function createAndDownloadCSV(data, filename) {
	
	const csvContent = convertToCSV(data);
  	const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });

  	// Create a temporary URL object to store the blob content
  	const url = URL.createObjectURL(blob);

  	// Create a hidden anchor element to trigger the download
  	const anchor = document.createElement('a');
  	anchor.href = url;
  	anchor.setAttribute('download', filename);

 	// Programmatically trigger a click event on the anchor element
  	// This will prompt the browser to download the file
  	anchor.click();

  	// Clean up the temporary URL object
  	URL.revokeObjectURL(url);
}

function convertToCSV(data) {
 	// Check if the data is an array of objects
  	if (!Array.isArray(data) || data.length === 0) {
    	return '';
  	}

  	// Get the headers (keys) from the first object
  	const headers = Object.keys(data[0]);

  	// Join the headers to form the first line of the CSV
  	const headerRow = headers.join(',') + '\n';

  	// Convert the data to CSV rows
  	const csvRows = data.map(obj => {
    	return headers.map(header => {
      	return obj[header];
    	}).join(',');
  	}).join('\n');

  	// Combine header and data rows
  	return headerRow + csvRows;
}
