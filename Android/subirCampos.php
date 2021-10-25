<?php
// Comprobacion de tipos de peticion http
if($_SERVER["REQUEST_METHOD"]=="POST"){
    //coneccion con la base de datos
    require_once("conection.php");
//recuperacion de datos desde android
    $title=$_POST['Titulo'];
    $Depiction=$_POST['Descripcion'];
    $BelongTo=$_POST['NacionalidadoPueblo'];
    $KindOf=$_POST['TipoArchivo'];
    $Public=$_POST['Publicado'];
    $Temas=$_POST['TagsTematicas'];
    $nombreImagen=($_POST['NombreSaber']);
    $rutaImagen="";
    if($KindOf=="Imagen"){
        // $imagen=$_POST['Imagen'];
        $rutaImagen="C:/xampp/htdocs/wordpress/wp-content/plugins/plugin-crud-ddrc/shortcode/../../../uploads/Saberes/Imagenes/$nombreImagen";
        $rutaImagenRelatica="../wordpress/wp-content/uploads/Saberes/Imagenes/$nombreImagen";   
        file_put_contents($rutaImagenRelatica,base64_decode($imagen));
        $bitesIMG=file_get_contents($rutaImagen); 
    }elseif($KindOf=="Video"){
        $rutaImagen="C:/xampp/htdocs/wordpress/wp-content/plugins/plugin-crud-ddrc/shortcode/../../../uploads/Saberes/Videos/$nombreImagen";
        $rutaImagenRelatica="../wordpress/wp-content/uploads/Saberes/Videos/$nombreImagen";    
    }elseif($KindOf=="Audio"){
        $rutaImagen="C:/xampp/htdocs/wordpress/wp-content/plugins/plugin-crud-ddrc/shortcode/../../../uploads/Saberes/Audios/$nombreImagen";
        $rutaImagenRelatica="../wordpress/wp-content/uploads/Saberes/Audios/$nombreImagen";    
    }elseif($KindOf=="Texto"){
        $rutaImagen="C:/xampp/htdocs/wordpress/wp-content/plugins/plugin-crud-ddrc/shortcode/../../../uploads/Saberes/Textos/$nombreImagen";
        $rutaImagenRelatica="../wordpress/wp-content/uploads/Saberes/Textos/$nombreImagen";    
    
    }
    $query="INSERT INTO `wp_android` (`ID`, `Titulo`, `Descripcion`, `Publicado`, `NacionalidadoPueblo`,`TipoArchivo`,`TagsTematicas`,`NombreSaber`,`RutaSaber`) 
    VALUES (null, '$title', '$Depiction', '$Public', '$BelongTo', '$KindOf','$Temas','$nombreImagen','$rutaImagen');";

     $result=mysqli_query($coneccion,$query);
    if ($result) {
        $Array=[];
        $response['mensaje']='Todo OK';
        array_push($Array,$response);
        echo (json_encode($response));
    }else{
        $Array=[];
        $response['mensaje']='Error al guardar el saber';
        array_push($Array,$response);
        echo (json_encode($response));
        
    }
}
mysqli_close($coneccion);
?>