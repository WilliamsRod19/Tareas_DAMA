<?php
// Incluir archivo de configuración
require_once '../config/config.php';

class Contacto {
    private $idConection;
    
    // Constructor que inicializa la conexión
    public function __construct() {
        $this->idConection = getConnection();
    }

    /**
     * Valida un nombre
     * @param string $nombre El nombre a validar
     * @return bool True si el nombre es válido
     */
    private function validarNombre($nombre) {
        return !empty($nombre) && 
               strlen($nombre) >= 2 && 
               strlen($nombre) <= 100 && 
               preg_match('/^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\s]+$/', $nombre);
    }

    /**
     * Valida un número de teléfono
     * @param string $telefono El teléfono a validar
     * @return bool True si el teléfono es válido
     */
    private function validarTelefono($telefono) {
        return !empty($telefono) && 
               preg_match('/^[0-9+\(\)\-\s]{6,20}$/', $telefono);
    }

    /**
     * Valida un ID de contacto
     * @param int $id_contacto El ID a validar
     * @return bool True si el ID es válido
     */
    private function validarId($id_contacto) {
        return filter_var($id_contacto, FILTER_VALIDATE_INT) !== false && $id_contacto > 0;
    }

    // SELECT: obtener todos los contactos
    public function ListarContactos() {
        try {
            $stmt = $this->idConection->prepare("SELECT id_contacto, nombre_contacto, telefono_contacto FROM contactos");
            $stmt->execute();
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            throw new Exception("Error al listar contactos: " . $e->getMessage());
        }
    }

    // SELECT: obtener un contacto por id
    public function ListarContactoPorId($id_contacto) {
        try {
            // Validar ID
            if (!$this->validarId($id_contacto)) {
                throw new Exception("ID de contacto no válido");
            }
            
            $stmt = $this->idConection->prepare("SELECT id_contacto, nombre_contacto, telefono_contacto FROM contactos WHERE id_contacto = ?");
            $stmt->execute([$id_contacto]);
            return $stmt->fetch(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            throw new Exception("Error al listar contacto: " . $e->getMessage());
        }
    }

    // SELECT: obtener un contacto por filtro
    public function ListarContactoConFiltro($filtro) {
        try {
            // Sanitizar el filtro
            $filtro = htmlspecialchars(trim($filtro));
            
            $stmt = $this->idConection->prepare("SELECT id_contacto, nombre_contacto, telefono_contacto FROM contactos WHERE (nombre_contacto LIKE ? OR telefono_contacto LIKE ?) ORDER BY nombre_contacto ASC");
            $filtroParam = "%$filtro%";
            $stmt->execute([$filtroParam, $filtroParam]);
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            throw new Exception("Error al filtrar contactos: " . $e->getMessage());
        }
    }

    // INSERT: agregar un nuevo contacto
    public function InsertarContacto($nombre, $telefono) {
        try {
            // Validar nombre
            if (!$this->validarNombre($nombre)) {
                throw new Exception("Nombre no válido");
            }
            
            // Validar teléfono
            if (!$this->validarTelefono($telefono)) {
                throw new Exception("Teléfono no válido");
            }
            
            $stmt = $this->idConection->prepare("INSERT INTO contactos (nombre_contacto, telefono_contacto) VALUES (?, ?)");
            $stmt->execute([$nombre, $telefono]);
            return $this->idConection->lastInsertId();
        } catch (PDOException $e) {
            throw new Exception("Error al insertar contacto: " . $e->getMessage());
        }
    }

    // UPDATE: actualizar un contacto existente
    public function ActualizarContacto($id_contacto, $nombre, $telefono) {
        try {
            // Validar ID
            if (!$this->validarId($id_contacto)) {
                throw new Exception("ID de contacto no válido");
            }
            
            // Validar nombre
            if (!$this->validarNombre($nombre)) {
                throw new Exception("Nombre no válido");
            }
            
            // Validar teléfono
            if (!$this->validarTelefono($telefono)) {
                throw new Exception("Teléfono no válido");
            }
            
            $stmt = $this->idConection->prepare("UPDATE contactos SET nombre_contacto = ?, telefono_contacto = ? WHERE id_contacto = ?");
            $stmt->execute([$nombre, $telefono, $id_contacto]);
            return $stmt->rowCount();
        } catch (PDOException $e) {
            throw new Exception("Error al actualizar contacto: " . $e->getMessage());
        }
    }

    // DELETE: eliminar un contacto
    public function EliminarContacto($id_contacto) {
        try {
            // Validar ID
            if (!$this->validarId($id_contacto)) {
                throw new Exception("ID de contacto no válido");
            }
            
            $stmt = $this->idConection->prepare("DELETE FROM contactos WHERE id_contacto = ?");
            $stmt->execute([$id_contacto]);
            return $stmt->rowCount();
        } catch (PDOException $e) {
            throw new Exception("Error al eliminar contacto: " . $e->getMessage());
        }
    }
    
    // Verificar si existe un contacto
    public function ExisteContacto($id_contacto) {
        try {
            if (!$this->validarId($id_contacto)) {
                return false;
            }
            
            $stmt = $this->idConection->prepare("SELECT COUNT(*) as total FROM contactos WHERE id_contacto = ?");
            $stmt->execute([$id_contacto]);
            $resultado = $stmt->fetch(PDO::FETCH_ASSOC);
            return $resultado['total'] > 0;
        } catch (PDOException $e) {
            throw new Exception("Error al verificar existencia de contacto: " . $e->getMessage());
        }
    }
}