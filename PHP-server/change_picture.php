<?php
	$response = array();
	if($_POST["ID"]){
		include 'config.php';
		$ID = MYSQL_method($_POST["ID"]);
		if (isset($_FILES['image']['name'])) {
			$hedef_yol = "profile_picture/";
			$yukleme = false;
			$rand = rand(0,500000);
			$bul = pathinfo($_FILES['image']['name']);
			$dosya = $hedef_yol . $rand . '.' . $bul['extension'];
			
			if(!file_exists($dosya)){
				if (move_uploaded_file($_FILES['image']['tmp_name'],$dosya)) {
					$yukleme = true;
				}
			}

			if($yukleme){
				$getir = mysql_query("SELECT profile_pic FROM uye WHERE id = '$ID'");
				$eski = mysql_fetch_array($getir);
				$eski = $eski["profile_pic"];
				unlink($eski);
				$degistir = mysql_query("UPDATE uye SET profile_pic = '$dosya' WHERE id = '$ID'");
				if($degistir){
					$response["succes"] = 1;
					$response["message"] = "Profil fotoğrafın güncellendi.";
				}else{
					$response["succes"] = -99;
					$response["message"] = "Sunucu ile ilgili bir sorun oluştu.";
				}
			}else{
				$response["succes"] = -99;
				$response["message"] = "Sunucu ile ilgili bir sorun oluştu.";
			}
		}else{	
			$response["succes"] = -1;
			$response["message"] = "Gerekli bilgiler doğrulanmadı.";
		}
	}else{
		$response["succes"] = -1;
		$response["message"] = "Gerekli bilgiler doğrulanmadı.";
	}

	echo json_encode($response);
?>