<?php
require_once 'db_functions.php';
$db = new DB_Functions();

/*
 * Endpoint : http://OldManPHP/DrinkShop/checkuser.php
 * Method   : POST
 * Params   : phone
 * Result   : JSON
 */
$response = array();
if(isset($_POST['phone']))
{
	$phone = $_POST['phone'];
	
	if($db->checkExistsUser($phone))
	{
		$response["exists"] = TRUE;
		echo json_encode($response);
	}
	else 
	{
		$response["exists"] = FALSE;
		echo json_encode($response);
	}
}
else
{
	$response['error_msg'] = "錯誤訊息:缺少需求參數 - phone";
	echo json_encode($response);
}

?>