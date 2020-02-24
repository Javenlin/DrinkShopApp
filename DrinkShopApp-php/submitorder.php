<?php
require_once 'db_functions.php';
$db = new DB_Functions ();

/*
 * Endpoint : http://OldManPHP/DrinkShopPHP/submitorder.php
 * Method : POST
 * Params : orderDetail,phone,address,comment,price
 * Result : JSON
 */
$response = array ();
if (isset ( $_POST ['orderDetail'] ) &&
	isset ( $_POST ['phone'] ) &&
	isset ( $_POST ['address'] ) &&
	isset ( $_POST ['price'] ) &&
	isset ( $_POST ['comment'] )) 
{
	$phone = $_POST ['phone'];
	$orderDetail = $_POST ['orderDetail'];
	$orderAddress = $_POST ['address'];
	$orderComment = $_POST ['comment'];
	$orderPrice = $_POST ['price'];
	
	$result = $db->insertNewOrder($orderPrice,$orderComment,$orderAddress,$orderDetail,$phone);
	if($result)
		echo json_encode("true");
	else 
		echo json_encode($result);//$this->conn->error
	
} 
else 
{
 	echo json_encode (  "錯誤! 缺少需求參數 - (phone,orderDetail,address,comment,price)" );
}

?>