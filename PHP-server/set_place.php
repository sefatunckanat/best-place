<?php
	$response=array();
	if(isset($_GET["ID"])){
		include"config.php";
		$ID = MYSQL_method($_GET["ID"]);
		$PLACE = MYSQL_method($_GET["PLACE"]);
		$OY = MYSQL_method($_GET["OY"]);

		$date = date("d-m-Y H:i");
		$sdate=date("YmdHi");

		$bak = mysql_query("INSERT INTO olaylar VALUES ('','$date','$ID','$OY','$ID','$PLACE','$sdate')");
		$response["succes"]=1;
	}else{
		$response["succes"]=-1;
	}

	echo json_encode($response);
?>