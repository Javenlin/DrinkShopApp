<?php

/**
 * ��Ʈw�s����k
 */

class DB_Connect{
	private $conn;
	
	public function connect()
	{
		require_once 'config.php';
		$this->conn = new mysqli(DB_HOST,DB_USER,DB_PASSWORD,DB_DATABASE);
		 // $db->set_charset('utf8');
		 // $this->conn = $db ;	
		return $this->conn;
	}		
}

?>