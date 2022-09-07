function formAgenda(dati) {
document.getElementById("Titolo").innerHTML = "Agenda";
document.getElementById("Riga1").innerHTML = "<br>Agenda  <input type=\"button\" value=\"Filtro\" onclick=\"formFiltraAgenda();\">";
document.getElementById("Form1").innerHTML = "";
document.getElementById("Form2").innerHTML = '<form id="formAgenda"  hidden  ondblclick="nascondi(\'formAgenda\');"  style="position:fixed; top:100px; left:50px; border:1px solid black; padding:10px; background:FloralWhite;">'+
'Evento:<br> <input type="text" id = "titolo" name="titolo" readonly><br>'+
'<input type="text" id = "descrizione" name="descrizione" readonly><br>' +
'<input type="text" id = "tipo" name="tipo" readonly><br>' +
'<label for="iniz">Inizio: </label><br><input type="datetime-local" id = "iniz" name="inizio"><br>'+
'<label for="fine">Fine:   </label><br><input type="datetime-local" id = "fine" name="fine"><br>'+
'<label for="alar">Allarme:</label><br><input type="datetime-local" id = "alar" name="sveglia"><br>'+
'<input type="hidden" id = "id" name="id">'+
'<input type="hidden" id="crud" name= "CRUD">'+
'<input type="button" value = "Modifica" onclick="modiAgenda();">'+
'<input type="button" value = "Elimina" onclick="deleAgenda();">'+
'</form>';

document.getElementById("Tab2").innerHTML = ""; 
document.getElementById("Error").innerHTML = ""; 
document.getElementById("Debug").innerHTML = ""; 

if (dati=="") {loadAgenda();}

hideForms();	
}

function formFiltraAgenda() {
	nascondi('formAgenda');
	document.getElementById("Form1").innerHTML = '<form id="formFiltraAgenda"  hidden  ondblclick="nascondi(\'formAgenda\');"  style="position:fixed; top:100px; left:50px; border:1px solid black; padding:10px; background:FloralWhite;">'+
'<label for="titolo">Titolo: </label><br><input type="text" id = "titolo" name="titolo"><br>'+
'<label for="tipoA">Tipo: </label><br><select id="tipoA" name="tipo"><option value=""></option></select><br>' +
'<label for="iniz">Dal: </label><br><input type="datetime-local" id = "iniz" name="inizio"><br>'+
'<label for="fine">Al:   </label><br><input type="datetime-local" id = "fine" name="fine"><br>'+
'<input type="hidden" id = "id" name="id">'+
'<input type="hidden" id="crud" name= "CRUD">'+
'<input type="button" value = "Filtra" onclick="filtraAgenda();">'+
'</form>';
opzioniTipiAttivita("tipoA");
mostra('formFiltraAgenda');
}

function modiAgenda() { 
  accessBackEnd("U","formAgenda","api/eventi", loadAgenda); 
}
function deleAgenda() { 
  accessBackEnd("D","formAgenda","api/eventi", loadAgenda); 
}
function filtraAgenda() { 
  accessBackEnd("R","formFiltraAgenda","api/eventi", tabAgenda); 
}


function loadAgenda(x) {accessBackEnd("R","","api/eventi", tabAgenda); }

function impAgenda(rispo) {
	formAgenda(rispo);
	tabAgenda(rispo);
}
function attAgenda(rispo) {
    formAgenda(rispo);
    tabAgenda(rispo);
}
function tabAgenda(rispo) {

var tabella ="";
tabella="<table><thead><tr style='text-align:left;'>"+
"<th>id</th> <th>Titolo</th> <th>Descrizione</th> <th>Tipo</th> <th>Inizio</th> <th>Fine</th>"+
"<th>Allarme</th><th colspan='2'>Da</th>";
tabella=tabella + "</tr></thead><tbody>";

var objRows =JSON.parse(rispo);
var riga="";

for (i = 0;i<objRows.length; i++) {
    riga="";    
	
    let dett=attOimp(objRows[i]);
	
    riga = '<tr onclick="valueAge(this)" style="background-color:' +dett.colore+';">';

    riga=riga+'<td>'+dett.id+'</td>';
    riga=riga+'<td>'+dett.titolo+'</td>';
    riga=riga+'<td>'+dett.descrizione+'</td>';
    riga=riga+'<td>'+dett.tipo+'</td>';
    riga=riga+'<td>'+dett.inizio+'</td>';
    riga=riga+'<td>'+dett.fine+'</td>';
    riga=riga+'<td>'+dett.allarme+'</td>';
    riga=riga+'<td>'+dett.ori+'</td>';
    riga=riga+'<td>'+dett.id_ori+'</td>';
    tabella=tabella+riga+'</tr>';
}

tabella=tabella+"</tbody></table>";

document.getElementById("Tab1").innerHTML= tabella;

}

function valueAge(x) {
	
  document.getElementById("id").value=x.cells[0].textContent;
  document.getElementById("titolo").value=x.cells[1].textContent;
  document.getElementById("descrizione").value=x.cells[2].textContent;
  document.getElementById("tipo").value=x.cells[3].textContent;
  document.getElementById("iniz").value=x.cells[4].textContent;
  document.getElementById("fine").value=x.cells[5].textContent;
  document.getElementById("alar").value=x.cells[6].textContent;
  
  document.getElementById("formAgenda").hidden=false;

}


function attOimp(ev) {
	let rispo={id:0,titolo:"",descrizione:"",tipo:"",inizio:"",fine:"",allarme:"",ori:"",id_ori:0,colore:""};
	
	rispo.id=ev.idEvento;	rispo.inizio=ev.orarioInizio.substr(0,16).replace('T',' ');
    rispo.fine=ev.orarioFine.substr(0,16).replace('T',' ');
    rispo.allarme=ev.orarioAllarme.substr(0,16).replace('T',' ');
	if (ev.impegno != null) {
	    rispo.titolo=ev.impegno.titolo;
        rispo.descrizione=ev.impegno.descrizione;
		rispo.ori="Im ";
		rispo.id_ori=ev.impegno.idImpegno;
        if (ev.impegno.tipoEventoImp !=null){
			rispo.tipo =ev.impegno.tipoEventoImp.tipo;
			rispo.colore=ev.impegno.tipoEventoImp.colore;
		} else {	
			rispo.tipo ="";
			rispo.colore="#FFFFFF";
		}	
	} else if (ev.attivita != null) {
	    rispo.titolo=ev.attivita.titolo;
        rispo.descrizione=ev.attivita.descrizione;
		rispo.ori="At ";
		rispo.id_ori=ev.attivita.idAttivita;
        if (ev.attivita.tipoEventoAtt !=null){
			rispo.tipo =ev.attivita.tipoEventoAtt.tipo;
			rispo.colore=ev.attivita.tipoEventoAtt.colore;
		} else {	
			rispo.tipo ="";
			rispo.colore="#FFFFFF";
		}
    } else {
        rispo.titolo="";
		rispo.descrizione="";
		rispo.ori="";
		rispo.id_ori="";
		rispo.tipo="";
		rispo.colore="";
    }
    return rispo;	
}