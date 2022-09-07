function formMenu() {

document.getElementById("menu").innerHTML = '<form id="formMenu" style="background:white;">'+
'<input type="button" value = "Agenda" onclick="formAgenda(\'\');">'+
'<input type="button" value = "Contatti" onclick="formContatti();">'+
'<input type="button" value = "Impegni" onclick="formImpegni();">'+
'<input type="button" value = "Attività" onclick="formAttivita();">'+
'<input type="button" value = "Tipi Contatto" onclick="formTipiContatto();">'+
'<input type="button" value = "Tipi Attività" onclick="formTipiAttivita();">'+
//'<input type="button" value = "Dati Utente" onclick="formUserModi();">'+
'<input type="button" value = "Logout" onclick="formLogin();"></form>';
hideForms();



}
											
