<?php
require_once 'db_functions.php';
$db = new DB_Functions ();

/*
 * Endpoint : http://OldManPHP/DrinkShopPHP/getuser.php
 * Method : POST
 * Params : phone,name,birthdate,address
 * Result : JSON
 */
$response = array ();

function UTF8($response){  

$UN_str = array ();
$DO_str = array ();
$OK_str = array ();

   foreach($response as $key => $value)
	$UN_str[urlencode($key)] = urlencode($value);//去掉中文
	$DO_str = json_encode($UN_str);//轉JSON
	$OK_str = urldecode($DO_str);//加回中文
  return $OK_str; 
} 

if (isset ( $_POST ['phone'] )) 
{
	$phone = $_POST ['phone'];
		
		 //Create new user
		 $user = $db->getUserInformation($phone); //是user object
		 if($user)
		 {
		 	$response["phone"] = $user["Phone"];
		 	$response["name"] = $user["Name"];
		 	$response["birthdate"] = $user["Birthdate"];		 
		 	$response["address"] = $user["Address"];
		 	$response["avatarUrl"] = $user["avatarUrl"];
		 	echo UTF8($response);
		 }
		 else 
		 {
		 	$response["error_msg"] = "用戶不存在";
		 	echo UTF8($response);
		 }	
} 
else 
{
	$response ['error_msg'] = "錯誤! 缺少需求參數 - (phone)"; //非user object
		echo UTF8($response);
}

?>