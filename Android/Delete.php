<?php
if($_SERVER["REQUEST_METHOD"]=="GET"){
    //coneccion con la base de datos
        require_once("conection.php");
    //recuperacion de datos desde android

    $ident=$_GET['ID'];
        $query="DELETE FROM `wp_android` WHERE ID='$ident';";
        $result=mysqli_query($coneccion,$query);
        if ($result) {
            $response['mensaje']="Saber eliminado correctamente";
            echo json_encode($response);
        }else{
            $response['mensaje']="Ha ocurrido un error al eliminar el saber";
            echo json_encode($response);
        }

        /*if ($coneccion->affected_rows > 0) {
            if ($result==TRUE) {
                echo "se elimino correctamente";
            }
            
        }else{
            echo "No hay nada para eliminar";
        }*/
    }
    mysqli_close($coneccion);
?>