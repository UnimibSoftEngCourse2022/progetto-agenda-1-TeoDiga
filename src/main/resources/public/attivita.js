function formAttivita() {

document.getElementById("Titolo").innerHTML = "Agenda - Attivita"; 
document.getElementById("Riga1").innerHTML = '<br><input type="button" value ="Suggerisci" onclick="hintAttivita();"'+        
' onmouseenter="help(\'Suggerisce l attività  più urgente\');" onmouseleave="help(\' \');">' +
'  <input type="button" value = "New" onclick="mostra(\'formAttivita\');">';

document.getElementById("Form1").innerHTML = ""; 
document.getElementById("Form2").innerHTML = '<form id="formAttivita" hidden  ondblclick="nascondi(\'formAttivita\');" style="position:fixed; top:100px; left:50px; border:1px solid black; padding:10px; background:FloralWhite;">'+
'<label for="titolo">Attività:</label><br><input type="text" id="titolo" name="titolo"><br>'+
'<label for="descr">Descrizione:</label><br><input type="text" id="descr" name="descrizione"><br>'+
'<label for="tipoA">Tipo attività:</label><br><select id="tipoA" name="tipo"><option value=""></option></select><br>'+
'<label for="creaz">Attivazione:</label><br><input type="datetime-local" id="creaz" name="creazione"><br>'+
'<label for="scad">Scadenza:</label><br><input type="datetime-local" id="scad" name="scadenza"><br>'+
'<label for="durata">Durata:</label><br><input type="number" id="durata" name="durataN"><select id="umtD" name="durata"></select><br>'+
'<label for="allarme">Preavviso:</label><br><input type="number" id="allarme" name="allarmeN"><select id="umtA" name="allarme"></select> <br>'+
'<input type="hidden" id="id" name="id"><input type="hidden" id="crud" name="CRUD">'+
    '<input type="hidden" id="idatt" name="att">'+
'<input type="button" value ="in Agenda" onclick="planAttivita();"'+
       ' onmouseenter="help(\'Inserisce attività in agenda (a partire da Attivazione)\')" onmouseleave="help(\' \');">'+

'<input type="button" value ="Salva" onclick="doAttivita();">'+
'<input type="button" value ="Modifica" onclick="modiAttivita();">'+
'<input type="button" value ="Elimina" onclick="deleAttivita();">'+
'<input type="button" value ="Filtro" onclick="loadAttivita();">'+
'<input type="button" value ="Ottimizza" onclick="maxAttivita();"'+
       ' onmouseenter="help(\'Lista massimale delle attività pianificabili tra Attivazione e Scadenza\')" onmouseleave="help(\' \');">'+
'<input type="button" value ="Agenda" onclick="filtraAgendaXAtt();"'+
    ' onmouseenter="help(\'Visualizza l evento pianificato in agenda\')" onmouseleave="help(\' \');">'+
'<br><r2 id="Help" style="background-color:yellow;">Passare su un tasto per vedere una descrizione</r2></form>';
document.getElementById("Tab2").innerHTML = ""; 
document.getElementById("Error").innerHTML = ""; 
document.getElementById("Debug").innerHTML = ""; 
loadAttivita(""); // li carica in Tab1 
opzioniTipiAttivita("tipoA"); 
opzioniUMT("umtD"); 
opzioniUMT("umtA"); 
hideForms();
}

function doAttivita()   {accessBackEnd("C","formAttivita","api/attivita", loadAttivita); } 
function modiAttivita() {accessBackEnd("U","formAttivita","api/attivita", loadAttivita); } 
function deleAttivita() {accessBackEnd("D","formAttivita","api/attivita", loadAttivita ); }

function hintAttivita() {accessBackEnd("H","formAttivita","api/attivita", tabAttivita); }
function maxAttivita()  {accessBackEnd("M","formAttivita","api/attivita", tabAttivita); }
function planAttivita() {accessBackEnd("P","formAttivita","api/attivita", loadAttivita); }

function loadAttivita(x) {accessBackEnd("R","","api/attivita", tabAttivita); }

function filtraAgendaXAtt() {
    document.getElementById("idatt").value=document.getElementById("id").value;
    document.getElementById("Riga1").innerHTML ="";
    accessBackEnd("A", "formAttivita", "api/eventi", attAgenda);
}
function tabAttivita(rispo) {

var tabella ="";
tabella="<table><thead><tr style='text-align:left;'>"+
"<th>id</th> <th>Titolo</th> <th>Descrizione</th> <th>Tipo</th> <th>Attivazione</th> <th>Scadenza</th> <th colspan='2'>Durata</th> <th colspan='2'>Allarme</th><th>Plan</th><th>Prio</th>";
tabella=tabella + "</tr></thead><tbody>";
var objRows =JSON.parse(rispo);

for (i = 0;i<objRows.length; i++) {
     var riga="";
 
//colore riga  a inizio riga  
    if (objRows[i].tipoEventoAtt==null) {
        riga = '<tr onclick="valueAtt(this)">';
    } else {
       riga = '<tr onclick="valueAtt(this)" style="background-color:' +objRows[i].tipoEventoAtt.colore+';">';
    }

    riga=riga+'<td>'+objRows[i].idAttivita+'</td>';
	riga=riga+'<td>'+objRows[i].titolo+'</td>';
	riga=riga+'<td>'+objRows[i].descrizione+'</td>';
	riga=riga+'<td>'+(objRows[i].tipoEventoAtt==null ? '' : objRows[i].tipoEventoAtt.tipo)+'</td>';
	riga=riga+'<td>'+objRows[i].creazione.substr(0,16).replace('T',' ')+'</td>';
	riga=riga+'<td>'+objRows[i].scadenza.substr(0,16).replace('T',' ')+'</td>';
    riga=riga+'<td>'+objRows[i].durataAN+'</td>';
	riga=riga+'<td>'+(objRows[i].umtDurataAtt==null ? ' ' : objRows[i].umtDurataAtt.descrizione)+'</td>';
  	riga=riga+'<td>'+objRows[i].allarmeAN+'</td>';
	riga=riga+'<td>'+(objRows[i].umtAllAtt==null ? ' ' :objRows[i].umtAllAtt.descrizione)+'</td>';
	riga=riga+'<td>'+(objRows[i].pianificata ? 'Si' : 'No')+'</td>';
	riga=riga+'<td>'+objRows[i].priorita+'</td>';
	
    tabella=tabella+riga+'</tr>';
 	
}
tabella=tabella+"</tbody></table>";

document.getElementById("Tab1").innerHTML= tabella;

}

function valueAtt(x) {
document.getElementById("titolo").value=x.cells[1].textContent;
document.getElementById("descr").value=x.cells[2].textContent;
document.getElementById("tipoA").value=x.cells[3].textContent;
document.getElementById("creaz").value=x.cells[4].textContent;
document.getElementById("scad").value=x.cells[5].textContent;
document.getElementById("durata").value=x.cells[6].textContent;
document.getElementById("umtD").value=umt(x.cells[7].textContent);
document.getElementById("allarme").value=x.cells[8].textContent;
document.getElementById("umtA").value=umt(x.cells[9].textContent);
document.getElementById("id").value=x.cells[0].textContent;
document.getElementById("formAttivita").hidden=false;
}

function opzioniTipiAttivita(t) {
   accessBackEnd("R","","api/tipi_evento", opzioniTA); 
} 

function opzioniTA(rispo) {
   var target=document.getElementById("tipoA");
   lista=JSON.parse(rispo);
   for (i=0; i<lista.length;i++) {
      var o = document.createElement("option");
      o.text=lista[i].tipo;
      o.value=lista[i].tipo;
      target.add(o);
   }
}

										
	
