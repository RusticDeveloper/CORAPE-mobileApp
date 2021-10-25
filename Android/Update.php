<?php

// use function PHPSTORM_META\type;
// if (isset($_FILES['filename'])) {
//     echo 'reconoce el arcjhivo';
// }else{echo'no reconoce el archivo';}
if($_SERVER["REQUEST_METHOD"]=="POST"){
    //coneccion con la base de datos
        require_once("conection.php");
    //recuperacion de datos desde android
    $ident=$_POST['Identificador'];
    $title=$_POST['Titulo'];
    $Depiction=$_POST['Descripcion'];
    $BelongTo=$_POST['NacionalidadoPueblo'];
    $KindOf=$_POST['TipoArchivo'];
    $Public=$_POST['Publicado'];
    $Tematicas=$_POST['TagsTematicas'];
    $SaberNombre=$_POST['NombreSaber'];
    if($KindOf=="Imagen"){
        $rutaImagen="C:/xampp/htdocs/wordpress/wp-content/plugins/plugin-crud-ddrc/shortcode/../../../uploads/Saberes/Imagenes/$SaberNombre";
        $rutaImagenRelatica="../wordpress/wp-content/uploads/Saberes/Imagenes/$SaberNombre";   
        // $imagen=$_POST['Imagen'];
    //     file_put_contents($rutaImagenRelatica,base64_decode($imagen));
    // $bitesIMG=file_get_contents($rutaImagenRelatica);
    }elseif($KindOf=="Video"){
        $rutaImagen="C:/xampp/htdocs/wordpress/wp-content/plugins/plugin-crud-ddrc/shortcode/../../../uploads/Saberes/Videos/$SaberNombre";
        $rutaImagenRelatica="../wordpress/wp-content/uploads/Saberes/Videos/$SaberNombre";    
         // if(isset($_FILES['filename'])){
        //     $originalImgName= $_FILES['filename']['name'];
        //     $tempName= $_FILES['filename']['tmp_name'];
        // }
        // if(move_uploaded_file($tempName,$rutaImagenRelatica)){
        //     echo json_encode(array( "status" => "true","message" => "Successfully file added!" , "data" => $emparray) );
        // }else{
        //     echo json_encode(array( "status" => "false","message" => "Failed!") );
        // }
    }elseif($KindOf=="Audio"){
        $rutaImagen="C:/xampp/htdocs/wordpress/wp-content/plugins/plugin-crud-ddrc/shortcode/../../../uploads/Saberes/Audios/$SaberNombre";
        $rutaImagenRelatica="../wordpress/wp-content/uploads/Saberes/Audios/$SaberNombre";    
        
    }elseif($KindOf=="Texto"){
        $rutaImagen="C:/xampp/htdocs/wordpress/wp-content/plugins/plugin-crud-ddrc/shortcode/../../../uploads/Saberes/Textos/$SaberNombre";
        $rutaImagenRelatica="../wordpress/wp-content/uploads/Saberes/Textos/$SaberNombre";    
        
    }
    
    // echo json_encode($response);

        $query="UPDATE `wp_android` SET Titulo='$title',Descripcion='$Depiction',NacionalidadoPueblo='$BelongTo',
        TipoArchivo='$KindOf',Publicado='$Public',TagsTematicas='$Tematicas',NombreSaber='$SaberNombre',RutaSaber='$rutaImagen'  WHERE ID='$ident';";
        $result=mysqli_query($coneccion,$query);

        if ($result) {
            $Array=[];
        $response['mensaje']='Saber Actualizado con exito';
        array_push($Array,$response);
        echo (json_encode($response));
        }else{
            $Array=[];
        $response['mensaje']='Error al guardar el saber';
        array_push($Array,$response);
        echo (json_encode($response));
        }
        /*if ($coneccion->affected_rows > 0) {
            if ($result==TRUE) {
                echo "actualizo el registro";
            }else{
                echo "error";
            }
            
        }else{
            
            echo "No encontraron resultados";
        }*/
    }
    mysqli_close($coneccion);

?>

  
     
  
