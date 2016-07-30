<?php
	$response=array();
	$response["notifs"]=array();
	if(isset($_GET["USER"])){
		include 'config.php';
		$ID = MYSQL_method($_GET["USER"]);
		if(strlen($ID)>0){
			$getir = mysql_query("SELECT * FROM olaylar WHERE kimin='$ID' AND olay='followers'");
			while ($bildirimler=mysql_fetch_array($getir)) {
				$bildirim = array();
				$bildirim["tarih"] = $bildirimler["tarih"];
				$follower = $bildirimler["kim"];
				$get_followers=mysql_query("SELECT id,name,profile_pic FROM uye WHERE id = '$follower'");
				$get_followers=mysql_fetch_array($get_followers);
				$bildirim["uye_ad"]=$get_followers["name"];
				$bildirim["uye_id"]=$get_followers["id"]*1;
				$bildirim["resim"]=$get_followers["profile_pic"];

				array_push($response["notifs"],$bildirim);
			}
			$response["succes"]=1;
		}else{
			$response["succes"]=-1;
			$response["message"]="Gerekli verilen sağlanamadı.";
		}
	}else{
		$response["succes"]=-1;
		$response["message"]="Gerekli verilen sağlanamadı.";
	}

	echo json_encode($response);
?>