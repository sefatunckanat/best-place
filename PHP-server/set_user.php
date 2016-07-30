<?php
	$response = array();
	
	if($_POST['ID']){
		include 'config.php';
		$name = MYSQL_method($_POST['name']);
		$username = MYSQL_method($_POST['username']);
		$bio = MYSQL_method($_POST['bio']);
		$city = MYSQL_method($_POST['city']); 
		$ID = $_POST['ID']; 

		if(strlen($name)>0||strlen($username)>0||strlen($bio)>0||strlen($city)>0){
			if(strpos($username,' ')==false){
				$bak = mysql_query("SELECT id FROM uye WHERE username='$username' AND id<>'$ID'");
				if(mysql_num_rows($bak)==0){
					$sehirler = mysql_query("SELECT kod FROM sehir WHERE ad = '$city'");
					$sehirkod = mysql_fetch_array($sehirler);
					$sehir = $sehirkod["kod"]*1; 
					$degistir = mysql_query("UPDATE uye SET username='$username',name='$name',bio='$bio',city='$sehir' WHERE id = '$ID'");

					if($degistir){
						$response['succes'] = 1;
						$response["message"] = "Profil bilgilerinizi başarılı bir şekilde güncellendi.";
					}else{
						$response["succes"] = -99;
						$response["message"] = "Teknik bir problem oldu lütfen daha sonra tekrar deneyin.";
					}
				}else{
					$response["succes"] = -2;
					$response["Bu kullanıcı adınız kullanımda görünüyor. Lütfen başka kullanıcı adı deneyin."];
				}
			}else{
				$response["succes"] = -3;
				$response["message"] = "Farklı kullanıcı adı denemelisiniz.";
			}
		}else{
			$response["succes"] = 0;
			$response["succes"] = "Bilgileriniz yeterince uzun görünmüyor.";
		}
	}else{
		$response["succes"] = -1;
		$response["message"] = "Gerekli veriler sağlanmadı.";
	}

	echo json_encode($response);
?>
