<?php
//COMPRUEBA EL TIPO DE PETICION
if($_SERVER["REQUEST_METHOD"]=="GET"){
    //coneccion con la base de datos
        require_once("conection.php");
    //recuperacion de datos desde android
    $title=$_GET['Titulo'];
    $BelongTo=$_GET['NacionalidadoPueblo'];
    $All=$_GET['Todo'];
    $Audio=$_GET['Audio'];
    $Video=$_GET['Video'];
    $Texto=$_GET['Texto'];
    $Imagen=$_GET['Imagen'];
    if ($All!="" || $All!=null) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo';";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo';";
        }
    }else if (($Video!="" || $Video!=null)&& ($Imagen!="" || $Imagen!=null)&&($Texto!="" || $Texto!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             (TipoArchivo='$Video'||TipoArchivo='$Imagen'||TipoArchivo='$Texto');";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND (TipoArchivo='$Video'||TipoArchivo='$Imagen'
             ||TipoArchivo='$Texto');";
        }
    }else if (($Imagen!="" || $Imagen!=null)&&($Texto!="" || $Texto!=null)&&($Audio!="" || $Audio!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             (TipoArchivo='$Audio'||TipoArchivo='$Imagen'||TipoArchivo='$Texto');";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND (TipoArchivo='$Audio'||TipoArchivo='$Imagen'
             ||TipoArchivo='$Texto');";
        }
    }else if (($Texto!="" || $Texto!=null)&&($Video!="" || $Video!=null)&&($Audio!="" || $Audio!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             (TipoArchivo='$Video'||TipoArchivo='$Audio'||TipoArchivo='$Texto');";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND (TipoArchivo='$Video'||TipoArchivo='$Audio'
             ||TipoArchivo='$Texto');";
        }
    }else if (($Audio!="" || $Audio!=null)&&($Video!="" || $Video!=null)&&($Imagen!="" || $Imagen!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             (TipoArchivo='$Video'||TipoArchivo='$Imagen'||TipoArchivo='$Audio');";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND (TipoArchivo='$Video'||TipoArchivo='$Imagen'
             ||TipoArchivo='$Audio');";
        }
    }else if (($Texto!="" || $Texto!=null)&&($Video!="" || $Video!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             (TipoArchivo='$Video'||TipoArchivo='$Texto');";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND (TipoArchivo='$Video'||TipoArchivo='$Texto');";
        }
    }else if (($Audio!="" || $Audio!=null)&&($Imagen!="" || $Imagen!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             (TipoArchivo='$Audio'||TipoArchivo='$Imagen');";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND (TipoArchivo='$Audio'||TipoArchivo='$Texto');";
        }
    }else if (($Audio!="" || $Audio!=null)&&($Video!="" || $Video!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             (TipoArchivo='$Video'||TipoArchivo='$Audio');";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND (TipoArchivo='$Video'||TipoArchivo='$Audio');";
        }
    }else if (($Texto!="" || $Texto!=null)&&($Imagen!="" || $Imagen!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             (TipoArchivo='$Imagen'||TipoArchivo='$Texto');";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND (TipoArchivo='$Imagen'||TipoArchivo='$Texto');";
        }
    }else if (($Texto!="" || $Texto!=null)&&($Audio!="" || $Audio!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             (TipoArchivo='$Audio'||TipoArchivo='$Texto');";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND (TipoArchivo='$Audio'||TipoArchivo='$Texto');";
        }
    }else if (($Imagen!="" || $Imagen!=null)&&($Video!="" || $Video!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             (TipoArchivo='$Video'||TipoArchivo='$Imagen');";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND (TipoArchivo='$Video'||TipoArchivo='$Imagen');";
        }
    }else if (($Imagen!="" || $Imagen!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND 
             TipoArchivo='$Imagen';";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND TipoArchivo='$Imagen';";
        }
    }else if (($Video!="" || $Video!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND TipoArchivo='$Video';";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND TipoArchivo='$Video';";
        }
    }else if (($Audio!="" || $Audio!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND TipoArchivo='$Audio';";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND TipoArchivo='$Audio';";
        }
    }else if (($Texto!="" || $Texto!=null)) {
        if($title!="" || $title!=null){
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             Titulo='$title' AND NacionalidadoPueblo='$BelongTo' AND TipoArchivo='$Texto';";
        }else{
            $query="SELECT Titulo,TipoArchivo,NacionalidadoPueblo,ID FROM `wp_android` WHERE
             NacionalidadoPueblo='$BelongTo' AND TipoArchivo='$Texto';";
        }
    }
    
        
        $result=mysqli_query($coneccion,$query);
        $array=[];
        if ($result->num_rows>0) {
            while ($row=mysqli_fetch_assoc($result)) {
                //$array=$row ;
                array_push($array,$row);
                //echo $row['Titulo']."<br>";
            }
            echo (json_encode($array));
        }else{
            $response['mensaje']="Saber no encontrado";
            echo json_encode($response);
        }
    }
   // mysqli_close($coneccion);
?>