<?php
	$response=array();
	$response["feed"]=array();
	if(isset($_GET["id"])){
		include 'config.php';
		$ID = MYSQL_method($_GET["id"]);
		$getir = mysql_query("SELECT * FROM olaylar ORDER BY sys_tarih DESC");
		while ($bam=mysql_fetch_array($getir)) {
			$bak = mysql_query("SELECT * FROM takip WHERE kim='$ID'");
			while ($bam2=mysql_fetch_array($bak)) {
				$u_id = $bam2["kimi"];
				$uye=mysql_query("SELECT name,url FROM uye WHERE id='$u_id'");
				$uye=mysql_fetch_array($uye);
				$olay = array();
				$olay["id"]=$u_id;
				$olay["name"]=$uye["name"];

				array_push($response["feed"],$olay);
			}
		}
		$response["succes"]=1;
	}else{
		$response["succes"]=-1;
	}

	echo json_encode($response);
?>