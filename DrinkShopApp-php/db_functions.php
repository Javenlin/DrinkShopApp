<?php

class DB_Functions{
	private  $conn;
	
	function __construct()
	{
		require_once 'db_connect.php';
		$db = new DB_Connect();
		$this->conn = $db->connect();		
	}
	
	function __destruct()
	{
		// TODO: Implement __destruct() method.
	}
	
	/**
	 * 中文化工具函式
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
	
	/**
	 * 檢查用戶是否存在
	 * 回傳 true/false
	 */
	function checkExistsUser($phone)
	{		
		$stmt = $this->conn->prepare("SELECT * FROM User WHERE Phone = ?");
		$stmt ->bind_param("s",$phone);
		$stmt ->execute();
		$stmt ->store_result();
		
		if($stmt->num_rows > 0)
		{
			$stmt->close();
			return true;
		}
		else{
			$stmt->close();
			return false;
		}
	}
	
	/**
	 * 註冊新用戶
	 * 建立成功 -回傳  User Object
	 * 建立失敗 -回傳 Error Message
	 */
	public function registerNewUser($phone,$name,$birthdate,$address)
	{
		$stmt = $this->conn->prepare("INSERT INTO User(Phone,Name,Birthdate,Address) VALUES(?,?,?,?)");
		$stmt ->bind_param("ssss",$phone,$name,$birthdate,$address);
		$result =$stmt->execute();
		$stmt ->close();
		
		if($result)
		{
			$stmt = $this->conn->prepare("SELECT * FROM User WHERE Phone = ?");
			$stmt ->bind_param("s",$phone);
			$stmt ->execute();
			$user = $stmt->get_result()->fetch_assoc();
			$stmt ->close();
			return $user;
		}
		else 
			return false;
	}
	
	/**
	 * 獲得用戶數據
	 * 用戶存在     -回傳  User Object
	 * 用戶不存在 -回傳 false
	 */
	
	public function getUserInformation($phone)
	{
		 $stmt = $this->conn->prepare("SELECT * FROM User WHERE Phone = ?");
		 $stmt ->bind_param("s",$phone);
		 
		 if($stmt->execute())
		 {		
		 	$user = $stmt->get_result()->fetch_assoc();
		 	$stmt->close();
		 	
		 	return $user;
		 }
		 else 
		 	return NULL;
	}
	
	/**
	 * 獲得橫幅廣告
	 * 回傳 List Of Banners
	 */
	
	public function getBanners()
	{
		//選取5個最新廣告
		$result = $this->conn->query("SELECT * FROM Banner ORDER BY ID LIMIT 5");
		
		$banners = array();
		
		while($item = $result->fetch_assoc())
			$banners[] = $item;
		return $banners;
		
	}
	
	/**
	 * 獲得目錄
	 * 回傳 List Of Menu
	 */
	
	public function getMenu()
	{		
		$result = $this->conn->query("SELECT * FROM Menu");
	
		$menu = array();
	
		while($item = $result->fetch_assoc())
			$menu[] = $item;
			return $menu;
	
	}
	
	/**
	 * 獲得商品基本目錄
	 * 回傳 List Of Drink
	 */
	
	public function getDrinkByMenuID($menuID)
	{
		$query = "SELECT * FROM Drink WHERE MenuID = '".$menuID."'";
		$result = $this->conn->query($query);
	
		$drinks = array();
		
		while($item = $result->fetch_assoc())//$response //$item
		{	
			// $item = UTF8($response);
			$drinks[] = $item;
		}
			return $drinks;
		
	
	}
	
	/**
	 * 更新 頭像路徑
	 * 回傳 true/false
	 */
	
	public function updateAvatar($phone,$fileName)
	{
		return $result = $this->conn->query("UPDATE user SET avatarURL = '$fileName' WHERE Phone = '$phone'");	
	}
	
	/**
	 * 取得所有飲料
	 * 回傳 List of Drink / empty
	 */
	
	public function getAllDrinks()
	{
		$result = $this->conn->query("SELECT * FROM drink WHERE 1") or die($this->conn->error);
		
		$drinks = array();
		while($item = $result->fetch_assoc())
			$drinks[] = $item;
		return $drinks;
	}
	
	/**
	 * 輸入新訂單
	 * 回傳 true / false
	 */
	
	public function insertNewOrder($orderPrice,$orderComment,$orderAddress,$orderDetail,$userPhone)
	{
		 $stmt = $this->conn->prepare("INSERT INTO `order`(`OrderStatus`, `OrderPrice`, `OrderDetail`, `OrderComment`, `OrderAddress`, `UserPhone`) VALUES (0,?,?,?,?,?)");
		 $stmt->bind_param("sssss", $orderPrice,$orderDetail,$orderComment,$orderAddress,$userPhone)
		 or die($this->conn->error);
		 $result = $stmt->execute();
		 $stmt->close();
		 
		 if($result)
		 	return true;
		 else 
		 	return false;
	}
	
	/**
	 * 取得所有訂單 - 根據使用者電話和訂單編號
	 * 回傳 List / NULL
	 */
	
	public function getOrderByStatus($userPhone,$status)
	{
		$query = "SELECT * FROM `order` WHERE `OrderStatus` = '".$status."' AND `UserPhone` = '".$userPhone."'";
		$result = $this->conn->query($query) or die($this->conn->error);
		
		$orders = array();
		while($order = $result->fetch_assoc())
			$orders[] = $order;
		
		return $orders;
	}
	

}

?>