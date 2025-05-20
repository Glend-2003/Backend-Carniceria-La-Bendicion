package com.bendicion.la.carniceria.carniceria.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bendicion.la.carniceria.carniceria.domain.Categoria;
import com.bendicion.la.carniceria.carniceria.domain.Producto;
import com.bendicion.la.carniceria.carniceria.service.CloudinaryService;
import com.bendicion.la.carniceria.carniceria.service.ProductoService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/producto")
public class ProductoController {

     @Autowired
    private CloudinaryService cloudinaryService; // Nuevo servicio

    @Autowired
    private ProductoService productoService;

    @GetMapping("/{id}")
    public ResponseEntity<Producto> ObtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(productoService.ObtenerPorId(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<Producto>> listProductos(boolean estadoProducto) {
        List<Producto> productos = productoService.getProducto(estadoProducto);
        return ResponseEntity.ok(productos);
    }

    @PostMapping("/agregarProducto")
    public ResponseEntity<?> addProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.addProducto(producto);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Producto agregado con éxito con ID: " + nuevoProducto.getIdProducto());
            response.put("id", nuevoProducto.getIdProducto());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error al agregar el producto: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/agregarConImagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProductoWithImage(
            @RequestParam("nombreProducto") String nombreProducto,
            @RequestParam("montoPrecioProducto") double montoPrecioProducto,
            @RequestParam("descripcionProducto") String descripcionProducto,
            @RequestParam("cantidadProducto") double cantidadProducto,
            @RequestParam("tipoPesoProducto") String tipoPesoProducto,
            @RequestParam("codigoProducto") String codigoProducto,
            @RequestParam("stockProducto") int stockProducto,
            @RequestParam("idCategoria") int idCategoria,
            @RequestParam("estadoProducto") int estadoProducto,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se seleccionó ningún archivo");
        }

        try {
            // Subir imagen a Cloudinary
            String imageUrl = cloudinaryService.uploadFile(file);
            
            Producto nuevoProducto = new Producto();
            nuevoProducto.setNombreProducto(nombreProducto);
            nuevoProducto.setMontoPrecioProducto(BigDecimal.valueOf(montoPrecioProducto));
            nuevoProducto.setDescripcionProducto(descripcionProducto);
            nuevoProducto.setCantidadProducto(cantidadProducto);
            nuevoProducto.setTipoPesoProducto(tipoPesoProducto);
            nuevoProducto.setCodigoProducto(codigoProducto);
            nuevoProducto.setStockProducto(stockProducto);

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(idCategoria);
            nuevoProducto.setCategoria(categoria);

            nuevoProducto.setEstadoProducto(estadoProducto == 1);
            nuevoProducto.setImgProducto(imageUrl); // Guardamos la URL de Cloudinary

            Producto savedProducto = productoService.addProducto(nuevoProducto);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Producto agregado con éxito con ID: " + savedProducto.getIdProducto());
            response.put("id", savedProducto.getIdProducto());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error al guardar el producto: " + e.getMessage()));
        }
    }

  

    @PutMapping(value = "/actualizar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProducto(
            @RequestParam("idProducto") int idProducto,
            @RequestParam("nombreProducto") String nombreProducto,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("montoPrecioProducto") double montoPrecioProducto,
            @RequestParam("descripcionProducto") String descripcionProducto,
            @RequestParam("cantidadProducto") double cantidadProducto,
            @RequestParam("tipoPesoProducto") String tipoPesoProducto,
            @RequestParam("codigoProducto") String codigoProducto,
            @RequestParam("stockProducto") int stockProducto,
            @RequestParam("idCategoria") int idCategoria,
            @RequestParam("estadoProducto") int estadoProducto) {

        try {
            Producto productoExistente = productoService.ObtenerPorId(idProducto);
            Producto productoActualizar = new Producto();
            productoActualizar.setIdProducto(idProducto);
            productoActualizar.setNombreProducto(nombreProducto);
            productoActualizar.setMontoPrecioProducto(BigDecimal.valueOf(montoPrecioProducto));
            productoActualizar.setDescripcionProducto(descripcionProducto);
            productoActualizar.setCantidadProducto(cantidadProducto);
            productoActualizar.setTipoPesoProducto(tipoPesoProducto);
            productoActualizar.setCodigoProducto(codigoProducto);
            productoActualizar.setStockProducto(stockProducto);

            Categoria categoria = new Categoria();
            categoria.setIdCategoria(idCategoria);
            productoActualizar.setCategoria(categoria);
            productoActualizar.setEstadoProducto(estadoProducto == 1);

            if (file != null && !file.isEmpty()) {
                // Eliminar imagen anterior si existe
                if (productoExistente != null && productoExistente.getImgProducto() != null) {
                    cloudinaryService.deleteFile(productoExistente.getImgProducto());
                }
                
                // Subir nueva imagen
                String newImageUrl = cloudinaryService.uploadFile(file);
                productoActualizar.setImgProducto(newImageUrl);
            } else if (productoExistente != null) {
                // Mantener la misma imagen si no se sube una nueva
                productoActualizar.setImgProducto(productoExistente.getImgProducto());
            }

            Producto productoActualizado = productoService.updateProducto(productoActualizar);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Producto actualizado con Ã©xito");
            response.put("id", productoActualizado.getIdProducto());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error al actualizar el producto: " + e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Boolean> deleteProducto(@PathVariable int id) {
        boolean eliminado = productoService.deleteProducto(id);
        if (eliminado) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @PutMapping("/activar/{id}")
    public ResponseEntity<Boolean> activarProducto(@PathVariable int id) {
        boolean estado = productoService.activarProducto(id);

        if (estado) {
            System.out.println("producto activado: ID -->" + id);
            return ResponseEntity.ok(true);
        } else {
            System.out.println("No se pudo activar el producto: ID -->" + id + " no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
}
