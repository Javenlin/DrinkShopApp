<?php

require_once 'db_functions.php';
$db = new DB_Functions();

if(isset($_FILES["upload_file"]["name"]))
{
	if(isset($_POST["phone"]))
	{
		$phone = $_POST["phone"];
		
		$name = $_FILES["upload_file"]["name"];
		$tmp_name = $_FILES["upload_file"]["tmp_name"];
		$error = $_FILES["upload_file"]["error"];
		
		if(!empty($name))
		{
			$location ='./user_avatar/';
			if(!is_dir($location))
					mkdir($location);
			if(move_uploaded_file($tmp_name, $location . $name))
			{
				$result = $db->updateAvatar($phone, $name);
				if($result)
					echo json_encode("Uploaded");
				else 
					echo json_encode("Error while write to database");
			}
		}
	}
	else 
		echo json_encode("Missing phone field");
		
}
else 
	echo json_encode("Please select file !")
?>