<?php
	header('Content-Type: text/html; charset=utf-8');
	$baglan = mysql_connect("localhost","ozlemtulekvt","fhAvyw6qarEnuaa4");
	$sec 	= mysql_select_db("ozlemtulekvt",$baglan);
	mysql_query("SET NAMES utf8");
	define('SECURITY_KEY', 'feakjh123712.1!231h');

	function MYSQL_method($value)
	{
		return mysql_real_escape_string(strip_tags(trim($value)));
	}
?>
