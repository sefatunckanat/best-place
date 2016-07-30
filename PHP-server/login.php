<?php
	$response = array();

	include 'config.php';
	if($_POST['security_post'] == SECURITY_KEY){

		if(!$_POST['username'] || !$_POST['password']){
			$response['succes'] = "0";
			$response['message'] = "Gerekli veriler sağlanmadı.";
		}else{
			$P_username = MYSQL_method($_POST['username']);
			$P_password = MYSQL_method($_POST['password']);

			$P_password = md5($P_password);

			$check_login = mysql_query("SELECT id FROM uye WHERE username='$P_username' AND password='$P_password'");

			if($check_login){
				if(mysql_num_rows($check_login)!=0){
					$response["succes"] = 1;
					$response["message"] = "Giriş başarılı.";
					$ID=mysql_fetch_array($check_login);
					$response["id"] = $ID["id"]*1;
				}else{
					$response["succes"] = -100;
					$response["message"] = "Kullanıcı adınız veya şifrenizle uyuşan kullanıcı bulamadık.";
					$response["id"] = -1;
				}
			}else{
				$response["succes"] = -99;
				$response["message"] = "Giriş sırasında beklenmedik bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.";
				$response["id"] = -1;
			}	
			
		}
	}else{
		$response['succes'] = -1;
		$response['message'] = "İstenilen güvenlik gerekçeleri sağlanamadı";
		$response["id"] = -1;
	}

	echo json_encode($response);
?>