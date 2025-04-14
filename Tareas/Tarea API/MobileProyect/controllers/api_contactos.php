<?php

    // Incluir los archivos necesarios
    require_once '../config/config.php';
    require_once '../models/Contacto.php';

    // Crear una instancia de la clase Contacto
    $contacto = new Contacto();

    try{

        // Obtener la acción a realizar
        $action = isset($_GET['action']) ? $_GET['action'] : '';

        // Si no hay acción especificada, devolver error
        if (empty($action)) {
            echo json_encode([
                'status' => 'error',
                'message' => 'No se ha especificado ninguna acción.',
            ]);
            exit;
        }

        switch ($action) {
            //select: obtener todos los contactos
            case 'listarContactos':
                $contactos = $contacto->ListarContactos();
                echo json_encode([
                    'status' => 'success',
                    'data' => $contactos,
                    'total_rows' => count($contactos),
                ]);
                break;
            
            //select: obtener un contacto por id    
            case 'listarContactoById':
                $id_contacto = isset($_GET['id_contacto']) ? filter_var($_GET['id_contacto'], FILTER_VALIDATE_INT) : 0;
                if ($id_contacto === false || $id_contacto <= 0){
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error: ID de contacto no valido. Debe ser un numero entero positivo.',
                    ]);
                    exit;
                }
                $resultado = $contacto->ListarContactoPorId($id_contacto);
                if ($resultado) {
                    echo json_encode([
                        'status' => 'success',
                        'data' => $resultado,
                    ]);
                } else {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Contacto no encontrado.',
                    ]);
                }
                break;

            //select: obtener un contacto con filtro
            case 'listarContactosConFiltro':
                $filtro = isset($_GET['filtro']) ? htmlspecialchars(trim($_GET['filtro'])) : '';
                // Validar que el filtro tenga una longitud mínima
                if (strlen($filtro) < 2 && !empty($filtro)) {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'El filtro debe tener al menos 2 caracteres.',
                    ]);
                    exit;
                }
                $contactos = $contacto->ListarContactoConFiltro($filtro);
                echo json_encode([
                    'status' => 'success',
                    'data' => $contactos,
                    'total_rows' => count($contactos),
                ]);
                break;

            //insert: agregar un nuevo contacto
            case 'insertarContacto':
                if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error: Método no permitido. Use POST para esta acción.',
                    ]);
                    exit;
                }
                
                $data = json_decode(file_get_contents('php://input'), true);
                
                if (json_last_error() !== JSON_ERROR_NONE) {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error: Formato JSON inválido.',
                    ]);
                    exit;
                }
                
                $nombre = isset($data['nombre']) ? trim($data['nombre']) : '';
                $telefono = isset($data['telefono']) ? trim($data['telefono']) : '';
                
                // Validaciones de campos
                $errores = [];
                
                if (empty($nombre)) {
                    $errores[] = 'El nombre es obligatorio.';
                } elseif (strlen($nombre) < 2) {
                    $errores[] = 'El nombre debe tener al menos 2 caracteres.';
                } elseif (strlen($nombre) > 100) {
                    $errores[] = 'El nombre no puede exceder los 100 caracteres.';
                } elseif (!preg_match('/^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\s]+$/', $nombre)) {
                    $errores[] = 'El nombre solo debe contener letras y espacios.';
                }
                
                if (empty($telefono)) {
                    $errores[] = 'El telefono es obligatorio.';
                } elseif (!preg_match('/^[0-9+\(\)\-\s]{6,20}$/', $telefono)) {
                    $errores[] = 'El telefono debe contener solo numeros, +, (), - y espacios (entre 6 y 20 caracteres).';
                }
                
                if (!empty($errores)) {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error en los datos proporcionados.',
                        'errores' => $errores
                    ]);
                    exit;
                }
                
                // Si todo está bien, insertamos el contacto
                try {
                    $id_contacto = $contacto->InsertarContacto($nombre, $telefono);
                    echo json_encode([
                        'status' => 'success',
                        'message' => 'Contacto insertado correctamente.',
                        'id_contacto' => $id_contacto,
                    ]);
                } catch (Exception $e) {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error al insertar contacto: ' . $e->getMessage(),
                    ]);
                }
                break;

            case 'actualizarContacto':
                if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error: Metodo no permitido. Use POST para esta acción.',
                    ]);
                    exit;
                }
                
                $data = json_decode(file_get_contents('php://input'), true);
                
                if (json_last_error() !== JSON_ERROR_NONE) {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error: Formato JSON inválido.',
                    ]);
                    exit;
                }
                
                $id_contacto = isset($data['id_contacto']) ? filter_var($data['id_contacto'], FILTER_VALIDATE_INT) : 0;
                $nombre = isset($data['nombre']) ? trim($data['nombre']) : '';
                $telefono = isset($data['telefono']) ? trim($data['telefono']) : '';
                
                // Validaciones de campos
                $errores = [];
                
                if ($id_contacto === false || $id_contacto <= 0) {
                    $errores[] = 'ID de contacto no válido. Debe ser un número entero positivo.';
                }
                
                if (empty($nombre)) {
                    $errores[] = 'El nombre es obligatorio.';
                } elseif (strlen($nombre) < 2) {
                    $errores[] = 'El nombre debe tener al menos 2 caracteres.';
                } elseif (strlen($nombre) > 100) {
                    $errores[] = 'El nombre no puede exceder los 100 caracteres.';
                } elseif (!preg_match('/^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\s]+$/', $nombre)) {
                    $errores[] = 'El nombre solo debe contener letras y espacios.';
                }
                
                if (empty($telefono)) {
                    $errores[] = 'El teléfono es obligatorio.';
                } elseif (!preg_match('/^[0-9+\(\)\-\s]{6,20}$/', $telefono)) {
                    $errores[] = 'El teléfono debe contener solo números, +, (), - y espacios (entre 6 y 20 caracteres).';
                }
                
                if (!empty($errores)) {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error en los datos proporcionados.',
                        'errores' => $errores
                    ]);
                    exit;
                }
                
                // Si todo está bien, actualizamos el contacto
                try {
                    $filasAfectadas = $contacto->ActualizarContacto($id_contacto, $nombre, $telefono);
                    if ($filasAfectadas > 0) {
                        echo json_encode([
                            'status' => 'success',
                            'message' => 'Contacto actualizado correctamente.',
                        ]);
                    } else {
                        echo json_encode([
                            'status' => 'info',
                            'message' => 'No se detectaron cambios para actualizar.',
                        ]);                
                    }
                } catch (Exception $e) {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error al actualizar contacto: ' . $e->getMessage(),
                    ]);
                }
                break;

            case 'eliminarContacto':
                if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error: Método no permitido. Use POST para esta acción.',
                    ]);
                    exit;
                }
                
                $data = json_decode(file_get_contents('php://input'), true);
                
                if (json_last_error() !== JSON_ERROR_NONE) {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error: Formato JSON inválido.',
                    ]);
                    exit;
                }
                
                $id_contacto = isset($data['id_contacto']) ? filter_var($data['id_contacto'], FILTER_VALIDATE_INT) : 0;
                
                if ($id_contacto === false || $id_contacto <= 0) {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error: ID de contacto no válido. Debe ser un número entero positivo.',
                    ]);
                    exit;
                }
                
                try {
                    $filasAfectadas = $contacto->EliminarContacto($id_contacto);
                    if ($filasAfectadas > 0) {
                        echo json_encode([
                            'status' => 'success',
                            'message' => 'Contacto eliminado correctamente.',
                        ]);
                    } else {
                        echo json_encode([
                            'status' => 'error',
                            'message' => 'No se encontró el contacto para eliminar.',
                        ]);                
                    }
                } catch (Exception $e) {
                    echo json_encode([
                        'status' => 'error',
                        'message' => 'Error al eliminar contacto: ' . $e->getMessage(),
                    ]);
                }
                break;

            default:
                echo json_encode([
                    'status' => 'error',
                    'message' => 'Acción no válida.',
                ]);
                break;
        }
    }catch (Exception $e) {
        // Manejar cualquier excepción no capturada
        echo json_encode([
            'status' => 'error',
            'message' => 'Error en el servidor: ' . $e->getMessage(),
        ]);
    }