<?php
	$response=array();
	$response["succes"]=-1;
	if(isset($_GET["filtre"])){
		$response["succes"]=1;
		include 'config.php';
		$tur = MYSQL_method($_GET["tur"]);
		$filtre=MYSQL_method($_GET["filtre"]);

		$response["mekanlar"]=array();

		$SQL = "
			SELECT mekan.id,mekan.isim,mekan.aciklama,mekan.sehir,mekan.picture FROM mekan
			JOIN olaylar
			ON mekan.id=olaylar.mekan
			WHERE olaylar.olay='$filtre' AND mekan.kategori='$tur'
			GROUP BY mekan.isim
			ORDER BY COUNT(olaylar.mekan) DESC
		";

		$olaylar = mysql_query($SQL);
		while ($bam=mysql_fetch_array($olaylar)) {
			$mekan = array();
			$mekan["id"]=$bam[0]*1;
			$mekan["name"]=$bam[1];
			$mekan["desc"]=$bam[2];
			$sehir = $bam[3];
			$sehirler=mysql_query("SELECT ad FROM sehir WHERE kod='$sehir'");
			$sehirler=mysql_fetch_array($sehirler);
			$mekan["sehir"] = $sehirler["ad"];
			$mekan["picture"]=$bam[4];

			array_push($response["mekanlar"],$mekan);
		}
	}elseif((isset($_GET["hepsi"]) || isset($_GET["arama"]))){
		include 'config.php';
		if(isset($_GET["arama"])){
			$ARAMA = MYSQL_method($_GET["arama"]);
			$getir = mysql_query("SELECT * FROM mekan WHERE isim LIKE '%$ARAMA%'");
		}else{
			$getir = mysql_query("SELECT * FROM mekan");	
		}
		$response["mekanlar"]=array();
		while ($bam=mysql_fetch_array($getir)) {
			$mekan = array();
			$mekan["id"] = $bam["id"]*1;
			$mekan["name"]=$bam["isim"];
			$mekan["desc"]=$bam["aciklama"];
			$mekan["picture"]=$bam["picture"];
			$sehir = $bam["sehir"];
				$sehir_get = mysql_query("SELECT ad FROM sehir WHERE kod = '$sehir'");
				$sehir_get = mysql_fetch_array($sehir_get);
			$sehir = $sehir_get["ad"];
			$mekan["sehir"]=$sehir;
			array_push($response["mekanlar"],$mekan);
		}
		$response["succes"]=1;
	}
	echo json_encode($response);
?>