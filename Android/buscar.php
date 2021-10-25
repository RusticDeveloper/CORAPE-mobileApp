<?php
if($_SERVER["REQUEST_METHOD"]=="GET"){
//coneccion con la base de datos
    require_once("conection.php");
//recuperacion de datos desde android
// $title=$_GET['Titulo'];
$NPD=$_GET['NacionPueblo'];
$TPA=$_GET['TipoArchivo'];
//variables de consulta
$query = "SELECT * FROM `wp_android` WHERE `NacionalidadoPueblo`='$NPD' && `TipoArchivo`='$TPA'";
// $query="SELECT * FROM `wp_android` WHERE Titulo = %'$title'%;";
$result=mysqli_query($coneccion,$query);
$array=[];
if ($result->num_rows > 0) {
    while ($row=mysqli_fetch_assoc($result)) {
        array_push($array,$row);
    }
    echo json_encode($array);
}else{
    $Respuesta=[];
    array_push($Respuesta,array("ID"=>"no encontro"));
    // array("mensaje"=>"No se encontraron resultados")
    echo json_encode($Respuesta);
    // echo ("No encontraron resultados");
}
}
//mysqli_close($coneccion);
?>