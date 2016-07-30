<?php
	$response=array();
	if(isset($_GET["kim"])){
		include 'config.php';
		$kim = MYSQL_method($_GET["kim"]);	
		$kimi = MYSQL_method($_GET["kimi"]);
		$ne = MYSQL_method($_GET["ne"]);
		$date = date("d-m-Y H:i");
		$sdate=date("YmdHi");
		if($ne == "1"){
			$yap = mysql_query("INSERT INTO takip VALUES('','$kim','$kimi','$date')");
			$yap2 = mysql_query("INSERT INTO olaylar VALUES('','$date','$kimi','followers','$kim','-1','$sdate')");
			$response["succes"]=1;
		}else{
			$yap = mysql_query("DELETE FROM takip WHERE kim ='$kim' AND kimi='$kimi'");
			$yap2 = mysql_query("DELETE FROM olaylar WHERE olay='followers' AND kimin ='$kimi' AND kim ='$kim'");
			$response["succes"]=1;
		}
	}else{
		$response["succes"]=-1;
	}
	echo json_encode($response);
?>