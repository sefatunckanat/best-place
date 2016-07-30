<?php
	include 'config.php';
	$rows = mysql_query("SELECT * FROM sehir");
	$response = array();
	$response["city"] = array();
	while ($row = mysql_fetch_assoc($rows)) {
		$city["kod"] 	= $row["kod"];
		$city["ad"] 	= $row["ad"];
		array_push($response["city"], $city);
	}

	echo json_encode($response);
?>