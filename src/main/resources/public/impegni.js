function formImpegni() {

document.getElementById("Titolo").innerHTML = "Agenda - Impegni"; 
document.getElementById("Riga1").innerHTML = '<br><input type="button" value = "New" onclick="mostra(\'formImpegni\');">';
document.getElementById("Form1").innerHTML = ""; 
document.getElementById("Form2").innerHTML = '<form id="formImpegni" hidden  ondblclick="nascondi(\'formImpegni\');" style="position:fixed; top:100px; left:50px; border:1px solid black; padding:10px; background:FloralWhite;">'+
'<label for="titolo">Impegno:</label><br><input type="text" id="titolo" name="titolo"><br>'+
'<label for="descr">Descrizione:</label><br><input type="text" id="descr" name="descrizione"><br>'+
'<label for="tipoA">Tipo impegno:</label><br><select id="tipoA" name="tipo"><option value=""></option></select><br>'+
'<label for="inizio">Data apertura:</label><br><input type="datetime-local" id="inizio" name="inizioImpegno"><br>'+
'<label for="fine">Scadenza:</label><br><input type="datetime-local" id="fine" name="fineImpegno"><br>'+
'<label for="durata">Durata occorrenza:</label><br><input type="number" id="durata" name="durataN"><select id="umtD" name="durata"> </select> <br>'+
'<label for="allarme">Preavviso:</label><br><input type="number" id="allarme" name="allN"><select id="umtA" name="all"></select><br>'+
'<label for="freq">Frequenza:</label><br><input type="number" id="freq" name="freqN"><select id="umtF" name="freq"></select><br>'+
'<input type="hidden" id="id" name="id"><input type="hidden" id="crud" name="CRUD">'+
'<input type="hidden" id="idimp" name="imp">'+
'<input type="button" value ="Salva" onclick="doImpegni();"'+ 
       ' onmouseenter="help(\'Inserisce nuovo Impegno\')" onmouseleave="help(\' \');">'+
'<input type="button" value ="Modifica" onclick="modiImpegni();">'+
'<input type="button" value ="Elimina" onclick="deleImpegni();">'+
'<input type="button" value ="Filtro" onclick="filtraImpegni();">'+
'<input type="button" value ="Agenda" onclick="filtraAgendaXImp();"'+
       ' onmouseenter="help(\'Visualizza eventi agenda collegati all impegno\')" onmouseleave="help(\' \');">'+
'<br><r2 id="Help" style="background-color:yellow;"></r2></form>'; 
document.getElementById("Tab2").innerHTML = ""; 
document.getElementById("Error").innerHTML = ""; 
document.getElementById("Debug").innerHTML = ""; 
loadImpegni(""); // li carica in Tab1 
opzioniTipiAttivita("tipoA"); 
opzioniUMT("umtD"); 
opzioniUMT("umtA"); 
opzioniUMT("umtF"); 
hideForms();
}

function doImpegni() {
	accessBackEnd("C","formImpegni","api/impegni", loadImpegni); 
} 
function modiImpegni() {
	accessBackEnd("U","formImpegni","api/impegni", loadImpegni); 
} 
function deleImpegni() {
  accessBackEnd("D","formImpegni","api/impegni", loadImpegni ); 
}
function filtraImpegni() {
	accessBackEnd("R","formImpegni","api/impegni", tabImpegni); 
} 
function filtraAgendaXImp() {
    document.getElementById("idimp").value=document.getElementById("id").value;
    document.getElementById("Riga1").innerHTML ="";
    accessBackEnd("I", "formImpegni", "api/eventi", impAgenda);
}

function loadImpegni(x) {accessBackEnd("R","","api/impegni", tabImpegni); }

function tabImpegni(rispo) {

var tabella ="";
tabella="<table><thead><tr style='text-align:left;'>"+
"<th>id</th> <th>Titolo</th> <th>Descrizione</th> <th>Tipo</th> <th>Inizio</th> <th>Fine</th>"+
"<th colspan='2'>Durata</th><th colspan='2'>Allarme</th> <th colspan='2'>Frequenza</th>";
tabella=tabella + "</tr></thead><tbody>";

var objRows =JSON.parse(rispo);
for (i = 0;i<objRows.length; i++) {
     var riga="";
  
    if (objRows[i].tipoEventoImp==null) {
        riga = '<tr onclick="valueImp(this)">';
    } else {
       riga = '<tr onclick="valueImp(this)"style="background-color:' +objRows[i].tipoEventoImp.colore+';">';
    }

    riga=riga+'<td>'+objRows[i].idImpegno+'</td>';
    riga=riga+'<td>'+objRows[i].titolo+'</td>';
    riga=riga+'<td>'+objRows[i].descrizione+'</td>';
    riga=riga+'<td>'+(objRows[i].tipoEventoImp==null ? '' : objRows[i].tipoEventoImp.tipo)+'</td>';
    riga=riga+'<td>'+objRows[i].inizioImpegno.substr(0,16).replace('T',' ')+'</td>';
    riga=riga+'<td>'+objRows[i].fineImpegno.substr(0,16).replace('T',' ')+'</td>';
    riga=riga+'<td>'+objRows[i].durataIN+'</td>';
    riga=riga+'<td>'+(objRows[i].umtDurataImp==null ? '' : objRows[i].umtDurataImp.idUMT)+'</td>';
    riga=riga+'<td>'+objRows[i].allarmeIN+'</td>';
    riga=riga+'<td>'+(objRows[i].umtAllImp == null ? '' : objRows[i].umtAllImp.idUMT)+'</td>';
    riga=riga+'<td>'+objRows[i].frequenzaIN+'</td>';
    riga=riga+'<td>'+(objRows[i].umtFreqImp==null ? '' : objRows[i].umtFreqImp.idUMT)+'</td>';

    tabella=tabella+riga+'</tr>';
}
tabella=tabella+"</tbody></table>";

document.getElementById("Tab1").innerHTML= tabella;

}

function valueImp(x) {
  document.getElementById("titolo").value=x.cells[1].textContent;
  document.getElementById("descr").value=x.cells[2].textContent;
  document.getElementById("tipoA").value=x.cells[3].textContent;
  document.getElementById("inizio").value=x.cells[4].textContent;
  document.getElementById("fine").value=x.cells[5].textContent;
  document.getElementById("durata").value=x.cells[6].textContent;
  document.getElementById("umtD").value=x.cells[7].textContent;
  document.getElementById("allarme").value=x.cells[8].textContent;
  document.getElementById("umtA").value=x.cells[9].textContent;
  document.getElementById("freq").value=x.cells[10].textContent;
  document.getElementById("umtF").value=x.cells[11].textContent;
  document.getElementById("id").value=x.cells[0].textContent;
  document.getElementById("formImpegni").hidden=false;  
}

function opzioniTipiAttivita(t) {
   accessBackEnd("R","","api/tipi_evento", opzioniTA); } 

function opzioniTA(rispo) {
   var target=document.getElementById("TipoA");
   lista=JSON.parse(rispo);
   for (i=0; i<lista.length;i++) {
      var o = document.createElement("option");
      o.text=lista[i].tipo;
      o.value=lista[i].tipo;
      target.add(o);
   }
}
