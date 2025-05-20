package com.bendicion.la.carniceria.carniceria.service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;
    
    public String uploadFile(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(), 
                ObjectUtils.asMap(
                    "folder", "carniceria", // Opcional: organiza en carpetas
                    "resource_type", "auto" // Detecta automáticamente el tipo
                )
            );
            // Guarda tanto la URL como el public_id (puedes devolver un Map si necesitas ambos)
            return (String) uploadResult.get("secure_url"); // Usa secure_url para HTTPS
        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen a Cloudinary", e);
        }
    }
    
    public void deleteFile(String imageUrl) {
        try {
            // Extrae el public_id de la URL
            String publicId = extractPublicIdFromUrl(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen de Cloudinary", e);
        }
    }
    
    private String extractPublicIdFromUrl(String url) {
        // Ejemplo de URL: https://res.cloudinary.com/demo/image/upload/v12345/carniceria/abc123.jpg
        // Extraemos todo después de /upload/ hasta la extensión
        
        String uploadPrefix = "/upload/";
        int uploadIndex = url.indexOf(uploadPrefix);
        
        if (uploadIndex == -1) {
            throw new IllegalArgumentException("URL de Cloudinary no válida");
        }
        
        String afterUpload = url.substring(uploadIndex + uploadPrefix.length());
        // Eliminamos parámetros de transformación si los hay
        String publicIdWithExtension = afterUpload.split("/")[1]; // [v12345, carniceria/abc123.jpg]
        String publicId = publicIdWithExtension.substring(0, publicIdWithExtension.lastIndexOf('.'));
        
        return "carniceria/" + publicId; // Incluye el folder si usaste uno
    }
}