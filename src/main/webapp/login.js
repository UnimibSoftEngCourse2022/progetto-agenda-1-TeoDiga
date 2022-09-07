function formLogin() {
document.getElementById("Titolo").innerHTML = "Agenda - Login";
document.getElementById("Riga1").innerHTML = "Inserire credenziali<br>";
document.getElementById("menu").innerHTML = "";
document.getElementById("Form1").innerHTML = '<form id="formLogin">'+
'<label for="userid">Userid: </label><br><input type="text" id = "userid" name="username"><br>'+
'<label for="pwd">Password</label><br><input type="password" id = "pwd" name="password"><br><br>'+
'<input type="button" value = "Login" onclick="doLogin();">'+
'<input type="button" value = "Registrazione" onclick="formUserIns();"></form>';
document.getElementById("Tab1").innerHTML = "";
document.getElementById("Form2").innerHTML = "";
document.getElementById("Tab2").innerHTML = "";
document.getElementById("Error").innerHTML = "";
document.getElementById("Debug").innerHTML = "";
token="";
}

function doLogin() {
   var frm = document.getElementById("formLogin");
   var fd = new FormData(frm);
   var js=JSON.stringify(Object.fromEntries(fd)); 

   var xhr = new XMLHttpRequest();
   xhr.open("POST", "authenticate");
   xhr.setRequestHeader("Content-Type", "application/json");
   xhr.send(js);
   xhr.onreadystatechange = function() {
      if (xhr.readyState == 4 && xhr.status == 200) {
         document.getElementById("Error").innerHTML = "Tutto ok";
         token = "Bearer " +xhr.responseText;
		 formMenu();
         formAgenda(''); 
	  } else {
         document.getElementById("Error").innerHTML = "Errore ";
      }
   };
}
											
