<?php
	$response=array();
	$response["feed"]=array();
	if(isset($_GET["id"])){
		include 'config.php';
		$ID = MYSQL_method($_GET["id"]);
		$takipciler = mysql_query("SELECT * FROM takip WHERE kim='$ID'");
		if(mysql_num_rows($takipciler)!=0){
			$t = array();
			while ($o=mysql_fetch_array($takipciler)) {
				$t[]=$o["kimi"];
			}

			$komut;
			if(count($t)>0){
				$komut=$komut." WHERE ";
			}
			for ($i=0; $i < count($t); $i++) { 
				if($i<count($t)-1){
					$komut=$komut." olaylar.kim='".$t[$i]."' OR";
				}else{

					$komut=$komut." olaylar.kim='".$t[$i]."'";
				}
			}

			$anakomut = "
				SELECT uye.name,uye.profile_pic,olaylar.olay,olaylar.kimin,olaylar.mekan,olaylar.tarih,olaylar.sys_tarih FROM uye
				JOIN olaylar
				ON olaylar.kim=uye.id
				".$komut." AND olaylar.olay!='followers'
				ORDER BY olaylar.sys_tarih DESC
			";
			$response["q"]=$anakomut;
			$getir = mysql_query($anakomut);
			while ($u=mysql_fetch_array($getir)) {
				$olay = array();
				$olay["name"]=$u[0];
				$olay["url"]=$u[1];
				$olay["olay"]=$u[2];
				$olay["other"]=$u[4]*1;
				$p = $u[4];
				$g = mysql_query("SELECT isim FROM mekan WHERE id = '$p'");
				$g = mysql_fetch_array($g);
				$olay["place_name"]=$g[0];
				$olay["tarih"]=$u[5];
				$olay["s_tarih"]=$u[6];

				array_push($response["feed"],$olay);
			}
		}
	}else{
		$response["succes"]=-1;
	}

	echo json_encode($response);
?>