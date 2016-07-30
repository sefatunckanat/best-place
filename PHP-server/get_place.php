<?php
	$response=array();
	$response["olaylar"]=array();
	$response["oylayanlar"]=array();
	if(isset($_GET['ID'])){
		include 'config.php';
		$ID = MYSQL_method($_GET["ID"]);
		$getir = mysql_query("SELECT * FROM mekan WHERE id = '$ID'");
		while ($bam=mysql_fetch_array($getir)) {
			$response["id"]=$bam["id"];
			$response["isim"]=$bam["isim"];
			$response["aciklama"]=$bam["aciklama"];
			$response["picture"]=$bam["picture"];
		}
		$olaylara_bak = mysql_query("SELECT * FROM olaylar WHERE mekan='$ID' ORDER BY id DESC");
		while ($bam=mysql_fetch_array($olaylara_bak)) {
			$ooo=array();
			$u_id = $bam["kimin"];
			$ooo["olay"] = $bam["olay"];
			$b = mysql_query("SELECT id,name,profile_pic FROM uye WHERE id = '$u_id'");
			$b = mysql_fetch_array($b);
			$ooo["kullanici"]=$b["name"];
			$ooo["id"]=$b["id"];
			$ooo["url"]=$b["profile_pic"];
			$ooo["date"]=$bam["tarih"];

			$tarih = $bam["tarih"];
			$tarih = substr($tarih,0,5);
			$d = date("d-m");

			if($tarih==$d){
				array_push($response["oylayanlar"],$b["id"]*1);
			}
			array_push($response["olaylar"],$ooo);
		}

		$response["succes"] = 1;
	}else{
		$response["succes"]=-1;
	}

	echo json_encode($response);
?>