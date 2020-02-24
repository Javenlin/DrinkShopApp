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
	$UN_str[urlencode($key)] = urlencode($value);//�h������
	$DO_str = json_encode($UN_str);//��JSON
	$OK_str = urldecode($DO_str);//�[�^����
  return $OK_str; 
} 

if (isset ( $_POST ['phone'] )) 
{
	$phone = $_POST ['phone'];
		
		 //Create new user
		 $user = $db->getUserInformation($phone); //�Ouser object
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
		 	$response["error_msg"] = "�Τᤣ�s�b";
		 	echo UTF8($response);
		 }	
} 
else 
{
	$response ['error_msg'] = "���~! �ʤֻݨD�Ѽ� - (phone)"; //�Duser object
		echo UTF8($response);
}

?>