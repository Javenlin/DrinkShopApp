<?php
require_once 'db_functions.php';
$db = new DB_Functions ();

/*
 * Endpoint : http://OldManPHP/DrinkShop/getorder.php
 * Method : POST
 * Params : userPhone,status
 * Result : JSON
 */

if (isset ( $_POST ['userPhone'] ) && isset ( $_POST ['status'] ))
{
	$userPhone = $_POST ['userPhone'];
	$status = $_POST ['status'];

	$orders = $db->getOrderByStatus($userPhone,$status);

	echo json_encode($orders);

}
else
{
	$response = "錯誤! 缺少需求參數 - (userPhone,status)";
	echo json_encode ( $response );
}

?>

