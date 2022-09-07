function formContatti() {
document.getElementById("Titolo").innerHTML = "Agenda - Contatti";
document.getElementById("Riga1").innerHTML = '<br><input type="button" value = "New" onclick="mostra(\'formContatti\');">';
document.getElementById("Form1").innerHTML = "";
document.getElementById("Form2").innerHTML = '<form id="formContatti"  hidden  ondblclick="nascondi(\'formContatti\');" style="position:fixed; top:100px; left:50px; border:1px solid black; padding:10px; background:FloralWhite;">'+
'<label for="nomeCognome">Nominativo:</label><br><input type="text" id = "nomeCognome" name="nomeCognome"><br>'+
'<label for="tipoC">Tipo contatto:</label><br><select id = "tipoC" name="tipo"><option value=""></option></select><br>'+
'<label for="tel">Telefono:</label><br><input type="text" id = "tel" name="telefono"><br>'+
'<label for="email">email address:</label><br><input type="text" id = "email" name="email"><br>'+
'<input type="hidden" id = "id" name="id">'+
'<input type="hidden" id="crud" name= "CRUD">'+
'<input type="button" value = "Salva" onclick="doContatti();">'+
'<input type="button" value = "Modifica" onclick="modiContatti();">'+
'<input type="button" value = "Elimina" onclick="deleContatti();">'+
'<input type="button" value = "Filtro" onclick="filtraContatti();">'+
'</form>';
document.getElementById("Tab2").innerHTML = "";
document.getElementById("Error").innerHTML = "";
document.getElementById("Debug").innerHTML = "";
loadContatti("");
opzioniTipoContatto("tipoC");
hideForms();
}

function doContatti() { 
  accessBackEnd("C","formContatti","api/contatti", loadContatti); 
}
function modiContatti() { 
  accessBackEnd("U","formContatti","api/contatti", loadContatti); 
}
function deleContatti() { 
  accessBackEnd("D","formContatti","api/contatti", loadContatti ); 
}
function filtraContatti() { 
  accessBackEnd("R","formContatti","api/contatti", tabContatti); 
}

function loadContatti(x) { 
  accessBackEnd("R","","api/contatti", tabContatti); 
}

function tabContatti(rispo) {

var tabella ="";
tabella="<table><thead><tr style='text-align: left;'><th>id</th><th>Tipo</th><th>Nominativo</th><th>Email</th><th>Telefono</th></tr></thead><tbody>";
var objRows =JSON.parse(rispo);
for (i = 0;i<objRows.length; i++) {
	var riga="";
	if (objRows[i].tipoContattoCon==null) { 
       riga = '<tr onclick="valueCont(this)">';
	} else {
   	   riga = '<tr onclick="valueCont(this)" style="background-color:' +objRows[i].tipoContattoCon.colore+';">';
    }
	riga=riga +'<td>'+objRows[i].idContatto+'</td>';
	riga=riga +'<td>';
	if (objRows[i].tipoContattoCon==null) { 
	   riga=riga +'';
    } else {
	   riga=riga +objRows[i].tipoContattoCon.tipo;
    }
	riga=riga+'</td>';
	riga=riga +'<td>'+objRows[i].nomeCognome+'</td>';
	riga=riga +'<td>'+objRows[i].email+'</td>'; 
	riga=riga +'<td>'+objRows[i].telefono+'</td>'; 
    riga=riga+'</tr>';
    tabella=tabella+riga;	
}
tabella=tabella+"</tbody></table>";

document.getElementById("Tab1").innerHTML= tabella;

}	

function valueCont(x) {
document.getElementById("id").value = x.cells[0].textContent;
document.getElementById("tipoC").value = x.cells[1].textContent;
document.getElementById("nomeCognome").value = x.cells[2].textContent;
document.getElementById("email").value = x.cells[3].textContent;
document.getElementById("tel").value = x.cells[4].textContent;
document.getElementById("formContatti").hidden=false;
                                         
}												
																
function opzioniTipoContatto(t) {
  accessBackEnd("R","","api/tipi_contatto", opzioniTC); 
}
function opzioniTC(rispo) {
  var target=document.getElementById("tipoC");
  lista=JSON.parse(rispo);
  for (i=0; i<lista.length;i++) {
     var o = document.createElement("option");
     o.text=lista[i].tipo;
     o.value=lista[i].tipo;
     target.add(o);
  }
}
