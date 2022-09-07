function formUserIns() {
document.getElementById("Titolo").innerHTML = "Agenda - Utente";
document.getElementById("Riga1").innerHTML = "Inserire dati Utente<br>";
document.getElementById("Form1").innerHTML = '<form id="formUserIns">'+
'<label for="nome">Nominativo</label><br><input type="text" id = "nome" name="nomeCognome"><br>'+
'<label for="userid">Userid: </label><br><input type="text" id = "userid" name="userName"><br>'+
'<label for="pwd">Password</label><br><input type="text" id = "pwd" name="password"><br><br>'+
'<input type="button" value = "Salva" onclick="doUser(); ">'+
'<input type="button" value = "Ritorna" onclick="formLogin();"></form>';
document.getElementById("Tab1").innerHTML = " ";
document.getElementById("Form2").innerHTML = " ";
document.getElementById("Tab2").innerHTML = " ";
document.getElementById("Error").innerHTML = " ";
document.getElementById("Debug").innerHTML = " ";
hideForms();
}
										

											

											
											
function formUserModi() {
document.getElementById("Titolo").innerHTML = "Agenda - Utente";
document.getElementById("Riga1").innerHTML = "<br>Modificare dati Utente";
document.getElementById("Form1").innerHTML = '<form id="formUserIns"><br>'+
'<label for="nome">Nominativo: </label><br><input type="text" id = "nome" name="nomeCognome"><br>'+
'<label for="userid">Userid: </label><br><input type="text" id = "userid" name="username" readonly><br>'+
'<label for="pwd">Password</label><br><input type="text" id = "pwd" name="password"><br><br>'+
'<input type="button" value = "Modifica" onclick="modiUser();">'+
'<input type="button" value = "Elimina" onclick="deleUser();"></form>';
document.getElementById("Tab1").innerHTML = "";
document.getElementById("Form2").innerHTML = "";
document.getElementById("Tab2").innerHTML = "";
document.getElementById("Error").innerHTML = "";
document.getElementById("Debug").innerHTML = "";
loadUser();
hideForms();
}

function doUser() {
  var frm = document.getElementById("formUserIns"); 
  var fd =new FormData(frm); 
  var js = JSON.stringify(Object.fromEntries(fd)); 
  var xhr = new XMLHttpRequest();
  xhr.open("POST", "register");
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.send(js);   
  xhr.onreadystatechange = function() {      
    if (xhr.readyState == 4 && xhr.status == 200) {         
	   document.getElementById("Error").innerHTML = "Tutto ok";
       formLogin();
    } else {         
	   document.getElementById("Error").innerHTML = "Errore ";      
	   document.getElementById("Debug").innerHTML = xhr.responseText;      
	}   
  } 
};
											
function loadUser() {
/*	
  xhr.open("POST", "register");
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.send(js);   
  xhr.onreadystatechange = function() {      
    if (xhr.readyState == 4 && xhr.status == 200) {         
	   document.getElementById("Error").innerHTML = "Tutto ok";
       formLogin();
    } else {         
	   document.getElementById("Error").innerHTML = "Errore ";      
	   document.getElementById("Debug").innerHTML = xhr.responseText;      
	}   
  } 
*/
}

function modiUser() {
	// modifica l'utente
	// loadUser
}
function deleUser() {
	// inserire qui le istruzioni di cancellazione
	formLogin();
}

