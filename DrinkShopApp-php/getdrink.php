<?php
require_once 'db_functions.php';
$db = new DB_Functions ();

/*
 * Endpoint : http://OldManPHP/DrinkShopPHP/getdrink.php
 * Method : POST
 * Params : phone,name,birthdate,address
 * Result : JSON
 */
 
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
 
$response = array ();
if (isset ( $_POST ['menuID'] )) 
{
	$menuID = $_POST ['menuID'];
		
		 //Create new user
		 $drinks = $db->getDrinkByMenuID($menuID);
		  
		 echo json_encode($drinks);
		 // echo UTF8($drinks); 
} 
else 
{
	$response ['error_msg'] = "錯誤! 缺少需求參數 - (menuID)";
	echo json_encode ($response);
}

?>