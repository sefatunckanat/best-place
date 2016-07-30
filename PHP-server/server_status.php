<?php
	$response = array();
	if($_POST["deviceID"]){
		include "config.php";
		$result = mysql_query("SELECT * FROM server");
		if($result){
			while($bam=mysql_fetch_assoc($result)){
				$response["status"] = $bam["status"];
				$response["message"]= $bam["message"];
			}
		}else{
			$response["status"]=-2;
			$response["message"]="Beklenmedik bir hata oluştu.";
		}
	}else{
		$response["status"]=0;
		$response["message"]="Gerekli veriler sağlanmadı.";
	}
	echo json_encode($response);
?>

