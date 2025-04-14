<?php
    // Parámetros de conexión a la base de datos
    define('DB_HOST', 'localhost');
    define('DB_NAME', 'miagendaDB');
    define('DB_USER', 'root');
    define('DB_PASS', '1234');

    // Función para crear la conexión PDO
    function getConnection() {
        try {
            $conn = new PDO("mysql:host=".DB_HOST.";dbname=".DB_NAME.";charset=utf8", DB_USER, DB_PASS);
            $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            return $conn;
        } catch (PDOException $e) {
            throw new Exception("Error de conexión: " . $e->getMessage());
        }
    }