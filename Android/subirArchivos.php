<?php
// Comprobacion de tipos de peticion http

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    //coneccion con la base de datos
    require_once("conection.php");
    //recuperacion de datos desde android
    $SaberNombre = $_POST['NombreSaber'];
    $KindOf = $_POST['TipoArchivo'];
    // $infoWordpress = [
    //     'post_author' => '1',
    //     'post_date' => $date,
    //     'post_date_gmt' => $date,
    //     'post_content' => '',
    //     'post_title' => $SaberNombre,
    //     'post_status' => 'inherit',
    //     'comment_status' => 'open',
    //     'ping_status' => 'closed',
    //     'post_name' => $SaberNombre,
    //     'post_modified' => $date,
    //     'post_modified_gmt' => $date,
    //     'post_parent' => '0',
    //     'menu_order' => '0',
    //     'post_type' => 'attachment',
    //     'comment_count' => '0',
    // ];
    // require_once('http://localhost/wordpress/wp-load.php');
    // $date = date('Y/m/d h:i:s', time()); //the time() is by default taken and is an optional atribute that refers to the integer unix timestamp
    // $rutaI1 = "http://localhost/wordpress/wp-content/uploads/Saberes/Imagenes/";
    // $rutaV1 = "http://localhost/wordpress/wp-content/uploads/Saberes/Videos/";
    // $rutaA1 = "http://localhost/wordpress/wp-content/uploads/Saberes/Audios/";
    // $rutaT1 = "http://localhost/wordpress/wp-content/uploads/Saberes/Textos/";
    if ($KindOf == "Imagen" && isset($_FILES['nombreSaber'])) {

        /*informacion para guardar en la base de datos de wordpress*/
        // $infoWordpress['guid'] = $rutaI1 . $SaberNombre;
        // $infoWordpress['post_mime_type'] = 'image/jpeg';
        // $idPost = wp_insert_post($infoWordpress);
        // // $idSaber=wp_insert_attachment($infoWorpress);
        // if (!is_wp_error($idPost)) {
        //     require_once('http://localhost/wordpress/wp-admin/includes/image.php');
        //     require_once('http://localhost/wordpress/wp-admin/includes/media.php');
        //     require_once('http://localhost/wordpress/wp-admin/includes/file.php');
        //     // ABSPATH.'/wp-content/uploads/Saberes/Imagenes/'.$SaberNombre)
        //     // $infoWordpress['ID']=$idPost;
        //     $respuesta_meta_saber = wp_generate_attachment_metadata($idPost, $rutaI1 . $SaberNombre);
        //     wp_update_attachment_metadata($idPost, $respuesta_meta_saber);
        //     // set_post_thumbnail($idPost, $idPost);
        // }


        $rutaImagenRelatica = "../wordpress/wp-content/uploads/Saberes/Imagenes/$SaberNombre";
        if (move_uploaded_file($_FILES['nombreSaber']['tmp_name'], $rutaImagenRelatica)) {
            $response["mensaje"] = "Saber guardado con exito";
            echo json_encode($response);
        } else {
            $response["mensaje"] = "El saber no se pudo guardar";
            echo json_encode($response);
        }
    } elseif ($KindOf == "Video" && isset($_FILES['nombreSaber'])) {
        /* para guardar en wordpress */
        // $infoWordpress['guid'] = $rutaV1 . $SaberNombre;
        // $infoWordpress['post_mime_type'] = 'video/mp4';
        // $idPost = wp_insert_post($infoWordpress);
        // // $idSaber=wp_insert_attachment($infoWorpress);
        // if (!is_wp_error($idPost)) {
        //     require_once('http://localhost/wordpress/wp-admin/includes/image.php');
        //     require_once('http://localhost/wordpress/wp-admin/includes/media.php');
        //     require_once('http://localhost/wordpress/wp-admin/includes/file.php');
        //     $respuesta_meta_saber = wp_generate_attachment_metadata($idPost, $rutaV1. $SaberNombre);
        //     wp_update_attachment_metadata($idPost, $respuesta_meta_saber);
        //     // set_post_thumbnail($idPost, $idPost);
        // }

        $rutaImagenRelatica = "../wordpress/wp-content/uploads/Saberes/Videos/$SaberNombre";
        if (move_uploaded_file($_FILES['nombreSaber']['tmp_name'], $rutaImagenRelatica)) {
            $response["mensaje"] = "Saber guardado con exito";
            echo json_encode($response);
        } else {
            $response["mensaje"] = "El saber no se pudo guardar";
            echo json_encode($response);
        }
    } elseif ($KindOf == "Audio" && isset($_FILES['nombreSaber'])) {
        // /*para guardar en wordpress*/
        // $infoWordpress['guid'] = $rutaA1 . $SaberNombre;
        // $infoWordpress['post_mime_type'] = 'audio/mpeg';
        // $idPost = wp_insert_post($infoWordpress);
        // // $idSaber=wp_insert_attachment($infoWorpress);
        // if (!is_wp_error($idPost)) {
        //     require_once('http://localhost/wordpress/wp-admin/includes/image.php');
        //     require_once('http://localhost/wordpress/wp-admin/includes/media.php');
        //     require_once('http://localhost/wordpress/wp-admin/includes/file.php');
        //     $respuesta_meta_saber = wp_generate_attachment_metadata($idPost, $rutaA1. $SaberNombre);
        //     wp_update_attachment_metadata($idPost, $respuesta_meta_saber);
        //     // set_post_thumbnail($idPost, $idPost);
        // }


        $rutaImagenRelatica = "../wordpress/wp-content/uploads/Saberes/Audios/$SaberNombre";
        if (move_uploaded_file($_FILES['nombreSaber']['tmp_name'], $rutaImagenRelatica)) {
            $response["mensaje"] = "Saber guardado con exito";
            echo json_encode($response);
        } else {
            $response["mensaje"] = "El saber no se pudo guardar";
            echo json_encode($response);
        }
    } elseif ($KindOf == "Texto" && isset($_POST['PDF'])) {
        /* para guardar en wordpress */
        // $infoWordpress['guid'] = $rutaT1 . $SaberNombre;
        // $infoWordpress['post_mime_type'] = 'aplication/pdf';
        // $idPost = wp_insert_post($infoWordpress);
        // // $idSaber=wp_insert_attachment($infoWorpress,'Saberes/Textos/'.$SaberNombre,$idPost);
        // if (!is_wp_error($idPost)) {
        //     require_once('http://localhost/wordpress/wp-admin/includes/image.php');
        //     require_once('http://localhost/wordpress/wp-admin/includes/media.php');
        //     require_once('http://localhost/wordpress/wp-admin/includes/file.php');
        //     $respuesta_meta_saber = wp_generate_attachment_metadata($idPost, $rutaT1 . $SaberNombre);
        //     wp_update_attachment_metadata($idPost, $respuesta_meta_saber);
        //     // set_post_thumbnail($idPost, $idPost);
        // }


        $rutaImagenRelatica = "../wordpress/wp-content/uploads/Saberes/Textos/$SaberNombre";
        if (file_put_contents($rutaImagenRelatica, base64_decode($_POST['PDF'])) == false) {
            $response["mensaje"] = "El saber no se pudo guardar";
            echo json_encode($response);
        } else {
            $response["mensaje"] = "Saber guardado con exito";
            echo json_encode($response);
        }
    }
}
