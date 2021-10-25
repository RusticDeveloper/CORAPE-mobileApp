<?php

if($_SERVER["REQUEST_METHOD"]=="GET"){
    //coneccion con la base de datos
        require_once("conection.php");
        require_once("PasswordHash.php");
    //recuperacion de datos desde android
    $NPD=$_GET['userName'];
    $TPA=$_GET['password'];
    /* validar contraseña  */
    $passHash=new PasswordHash(8,false);
    //variables de consulta
    $query = "SELECT * FROM `wp_users` WHERE `user_login`='$NPD'";
    $result=mysqli_query($coneccion,$query);
    if ($result->num_rows > 0) {
        $usuario=mysqli_fetch_array($result);
        
        $User=$usuario['user_login'];
        $Pass=$usuario['user_pass'];
        if ($passHash->CheckPassword($TPA,$Pass)) {
            
            $Respuesta['Respuesta']="Usuario correcto";
            
            echo json_encode($Respuesta);
        }else{
            // $Respuesta=[];
            $Respuesta['Respuesta']="no encontro";
            // array_push($Respuesta,$val);
            echo json_encode($Respuesta);
        }
    }else{
        // $Respuesta=[];
        $Respuesta['Respuesta']="no encontro";
        // array_push($Respuesta,$val);
        echo json_encode($Respuesta);
    }
    }
    //mysqli_close($coneccion);

?>