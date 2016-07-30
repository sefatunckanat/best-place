<?php
	$response = array();

	include 'config.php';
	if($_POST['security_post'] == SECURITY_KEY){
		$P_username = MYSQL_method($_POST['username']);
		$P_mail 	= MYSQL_method($_POST['mail']);
		$P_password = MYSQL_method($_POST['password']);
		$P_city 	= MYSQL_method($_POST['city']);
		$P_device 	= MYSQL_method($_POST['deviceID']);

		$P_password = md5($P_password);

		$check_username = mysql_query("SELECT id FROM uye WHERE username ='$P_username'");
		$check_mail 	= mysql_query("SELECT id FROM uye WHERE mail ='$P_mail'");
		if($check_mail && $check_username){
			if(mysql_num_rows($check_mail)!=0){
				$response["succes"] = -10;
				$response["message"] = "Girmiş olduğunuz mail kullanımda görünüyor.";
			}else if(mysql_num_rows($check_username)!=0){
				$response["succes"] = -9;
				$response["message"] = "Girmiş olduğunuz kullanıcı adı kullanımda görünüyor.";
			}else{
				$register = mysql_query("INSERT INTO uye(id,username,mail,password,city,deviceID,name,bio) VALUES('','$P_username','$P_mail','$P_password','$P_city','$P_device',' ',' ')");
				if($register){
					$response["succes"] = 1;
					$response["message"] = "Hesabınız başarılı bir şekilde oluşturuldu.";
				}else{
					$response["succes"] = -99;
					$response["message"] = "Kayıt sırasında beklenmedik bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.".mysql_error();
				}
			}
		}else{
			$response["succes"] = -99;
			$response["message"] = "Kayıt sırasında beklenmedik bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.".mysql_error();
		}
	}else{
		$response['succes'] = -1;
		$response['message'] = "İstenilen güvenlik gerekçeleri sağlanamadı"; 
	}

	echo json_encode($response);
?>