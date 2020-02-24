<?php
require_once 'db_functions.php';
$db = new DB_Functions ();

/*
 * Endpoint : http://OldManPHP/DrinkShopPHP/register.php
 * Method : POST
 * Params : phone,name,birthdate,address
 * Result : JSON
 */
$response = array ();
if (isset ( $_POST ['phone'] ) &&
	isset ( $_POST ['name'] ) &&
	isset ( $_POST ['birthdate'] ) &&
	isset ( $_POST ['address'] )) 
{
	$phone = $_POST ['phone'];
	$name = $_POST ['name'];
	$birthdate = $_POST ['birthdate'];
	$address = $_POST ['address'];
	
	if ($db->checkExistsUser ( $phone )) 
	{
		$response ["error_msg"] = "使用者已存在 號碼 - ".$phone;
		echo json_encode ( $response );
	} 
	else 
	{
		 //Create new user
		 $user = $db->registerNewUser($phone, $name, $birthdate, $address);
		 if($user)
		 {
		 	$response["phone"] = $user["Phone"];
		 	$response["name"] = $user["Name"];
		 	$response["birthdate"] = $user["Birthdate"];		 
		 	$response["address"] = $user["Address"];
		 	echo json_encode($response);
		 }
		 else 
		 {
		 	$response["error_msg"] = "註冊中 發生未知錯誤!";
		 	echo json_encode($response);
		 }
	}
	
} 
else 
{
	$response ['error_msg'] = "錯誤! 缺少需求參數 - (phone,name,birthdate,address)";
	echo json_encode ( $response );
}

?>