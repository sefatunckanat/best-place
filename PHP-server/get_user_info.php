<?php
	$response = array();
	$response["takipciler"]=array();
	$response["olaylar"]=array();

	include 'config.php';
	if(isset($_GET['USER'])){
		$ID = $_GET['USER'];
		$GET_USER = mysql_query("SELECT id,name,username,bio,profile_pic,city FROM uye WHERE id='$ID'");
		if($GET_USER){
			if(mysql_num_rows($GET_USER)>0){
				$get_info = mysql_fetch_array($GET_USER);
 
				$response["succes"] = 1;
				$response["name"] = $get_info["name"];
				$response["username"] = $get_info["username"];	
				$response["bio"] = $get_info["bio"];	
				$response["image"] = $get_info["profile_pic"];
				$response["cityID"] = $get_info["city"];
				$cityID = $get_info["city"];

				$sehir_getir = mysql_query("SELECT ad FROM sehir WHERE kod = '$cityID'");
				$sehir_getir = mysql_fetch_array($sehir_getir);
				$response["city"] = $sehir_getir["ad"];

				$takip_ettikleri = mysql_query("SELECT id FROM takip WHERE kim = '$ID'");
				$response["follow"] = mysql_num_rows($takip_ettikleri);

				$takipci = mysql_query("SELECT id,kim FROM takip WHERE kimi = '$ID'");
				$response["followers"] = mysql_num_rows($takipci);

				while ($bam=mysql_fetch_array($takipci)) {
					$kisi = array();
					$i = $bam["kim"];
					$getir = mysql_query("SELECT id FROM uye WHERE id = '$i'");
					$i = mysql_fetch_array($getir);
					$kisi['id']=$i["id"]*1;
					array_push($response["takipciler"],$kisi);
				}

				$olaylar = mysql_query("SELECT * FROM olaylar WHERE kimin='$ID' ORDER BY id DESC");
				while ($olay=mysql_fetch_array($olaylar)) {
					$O = array();
					$O["tarih"] = $olay["tarih"];
					$O["olay"] = $olay["olay"];
					if($O["olay"] == "followers"){
						$ii = $olay["kim"];
						$bul = mysql_query("SELECT id,name,profile_pic FROM uye WHERE id = '$ii'");
						$bam = mysql_fetch_array($bul);
						$O["name"] = $bam["name"];
						$O["id"] = $bam["id"]*1; 
						$O["picture"]=$bam["profile_pic"];
					}else{
						$ii=$olay["mekan"];
						$bul = mysql_query("SELECT id,isim,picture FROM mekan WHERE id = '$ii'");
						$bam = mysql_fetch_array($bul);
						$O["name"] = $bam["isim"];
						$O["id"] = $bam["id"]*1;
						$O["picture"]=$bam["picture"];
					}
					array_push($response["olaylar"],$O);
				}
			}else{
				$response["succes"] = 0;
				$response["message"] = "Bir hata oluştu.";
			}
		}else{
			$response["succes"] = -99;
			$response["message"] = "Sunucu ile alakalı bir sorun oluştu lütfen tekrar deneyin.";
		}
	}else{
		$response['succes'] = -1;
		$response['message'] = "İstenilen güvenlik gerekçeleri sağlanamadı";
	}

	echo json_encode($response);
?>