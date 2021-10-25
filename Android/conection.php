<?php
$coneccion=mysqli_connect(
    "localhost",
    "Daniel",
    "1234",
    "wordpress"
);
if($coneccion->connect_error){
    die("Error de coneccion por:".$coneccion->connect_error);
}else{
    //echo "Conexion Exitosa";
}
?>