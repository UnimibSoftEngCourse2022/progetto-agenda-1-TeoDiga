function formTipiContatto() {
document.getElementById("Titolo").innerHTML = "Agenda - Tipi Contatto";
document.getElementById("Riga1").innerHTML = "<br>Tipi contatto definiti";
document.getElementById("Form1").innerHTML = "";
document.getElementById("Form2").innerHTML = '<form id="formTipiContatto">'+
'<label for="tipo">Tipo contatto:</label><br><input type="text" id = "tipo" name="tipo"><br>'+
'<label for="descrizione">Descrizione :</label><br><input type="text" id = "descrizione" name="descrizione"><br>'+
'<label for="colore">Colore</label><br><input type="color" id = "colore" name="colore"><br><br>'+
'<input type="hidden" id = "id" name="id">'+
'<input type="hidden" id="crud" name= "CRUD">'+
'<input type="button" value = "Salva" onclick="doTipiContatto();">'+
'<input type="button" value = "Modifica" onclick="modiTipiContatto();">'+
'<input type="button" value = "Elimina" onclick="deleTipiContatto();"></form>';
document.getElementById("Tab2").innerHTML = "";
document.getElementById("Error").innerHTML = "";
document.getElementById("Debug").innerHTML = "";
loadTipiContatto();
hideForms();
}
											
function doTipiContatto() { 
  accessBackEnd("C","formTipiContatto","api/tipi_contatto", loadTipiContatto); 
}
function modiTipiContatto() { 
  accessBackEnd("U","formTipiContatto","api/tipi_contatto", loadTipiContatto); 
}
function deleTipiContatto() { 
  accessBackEnd("D","formTipiContatto","api/tipi_contatto", loadTipiContatto); 
}
											
function loadTipiContatto() { 
  accessBackEnd("R","","api/tipi_contatto", tabTipiContatto); 
}

function tabTipiContatto(rispo) {

var tabella ="";
tabella="<table><thead><tr><th>id</th><th>Tipo</th><th>Descrizione</th><th>Colore</th></tr></thead><tbody>";
var objRows =JSON.parse(rispo);

for (i = 0;i<objRows.length; i++) {
	var riga = '<tr onclick="valueTCont(this)">';
	riga=riga +'<td>'+objRows[i].idTipoContatto+'</td>';
	riga=riga +'<td>'+objRows[i].tipo+'</td>';
	riga=riga +'<td>'+objRows[i].descrizione+'</td>';
	riga=riga +'<td style="background-color:'+objRows[i].colore+';">'+objRows[i].colore+'</td>'; 
    riga=riga+'</tr>';
    tabella=tabella+riga;	
}
tabella=tabella+"</tbody></table>";

document.getElementById("Tab1").innerHTML= tabella;
document.getElementById("formTipiContatto").hidden=false;
}												
function valueTCont(x) {
document.getElementById("id").value = x.cells[0].textContent;
document.getElementById("tipo").value = x.cells[1].textContent;
document.getElementById("descrizione").value = x.cells[2].textContent;
document.getElementById("colore").value = x.cells[3].textContent;
                                         
}												


