package com.bendicion.la.carniceria.carniceria.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;
    
    public String uploadFile(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                file.getBytes(), 
                ObjectUtils.asMap(
                    "folder", "carniceria",
                    "resource_type", "auto" 
                )
            );

            return (String) uploadResult.get("secure_url"); 
        } catch (IOException e) {
            throw new RuntimeException("Error al subir la imagen a Cloudinary", e);
        }
    }
    
    public void deleteFile(String imageUrl) {
        try {
   
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                System.out.println("URL de imagen vacía, no se puede eliminar");
                return;
            }
            
  
            String publicId = extractPublicIdFromUrl(imageUrl);
            
            if (publicId == null || publicId.trim().isEmpty()) {
                System.err.println("No se pudo extraer public_id de la URL: " + imageUrl);
                return;
            }
            
            System.out.println("Intentando eliminar imagen con public_id: " + publicId);
            
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");
            
            if ("ok".equals(resultStatus)) {
                System.out.println("Imagen eliminada exitosamente: " + publicId);
            } else if ("not found".equals(resultStatus)) {
                System.out.println("Imagen no encontrada en Cloudinary: " + publicId);
            } else {
                System.err.println("Error al eliminar imagen. Status: " + resultStatus);
            }
            
        } catch (Exception e) {
            System.err.println("Error al eliminar la imagen de Cloudinary: " + e.getMessage());
            e.printStackTrace();

        }
    }
    
    private String extractPublicIdFromUrl(String url) {
        try {

            
            if (url == null || url.trim().isEmpty()) {
                return null;
            }
            
            String uploadPrefix = "/upload/";
            int uploadIndex = url.indexOf(uploadPrefix);
            
            if (uploadIndex == -1) {
                System.err.println("URL de Cloudinary no válida - no contiene '/upload/': " + url);
                return null;
            }
            
            String afterUpload = url.substring(uploadIndex + uploadPrefix.length());
            
            String[] parts = afterUpload.split("/");
            
            if (parts.length < 2) {
                System.err.println("URL de Cloudinary no válida - formato incorrecto: " + url);
                return null;
            }
            
            String publicIdPart;
            
            if (parts[0].matches("v\\d+")) {
                if (parts.length < 3) {
                    System.err.println("URL de Cloudinary no válida - faltan partes después de la versión: " + url);
                    return null;
                }

                StringBuilder publicIdBuilder = new StringBuilder();
                for (int i = 1; i < parts.length; i++) {
                    if (i > 1) {
                        publicIdBuilder.append("/");
                    }
                    publicIdBuilder.append(parts[i]);
                }
                publicIdPart = publicIdBuilder.toString();
            } else {

                StringBuilder publicIdBuilder = new StringBuilder();
                for (int i = 0; i < parts.length; i++) {
                    if (i > 0) {
                        publicIdBuilder.append("/");
                    }
                    publicIdBuilder.append(parts[i]);
                }
                publicIdPart = publicIdBuilder.toString();
            }
            
   
            int lastDotIndex = publicIdPart.lastIndexOf('.');
            if (lastDotIndex > 0) {
                publicIdPart = publicIdPart.substring(0, lastDotIndex);
            }
            
            System.out.println("Public ID extraído: " + publicIdPart + " de URL: " + url);
            return publicIdPart;
            
        } catch (Exception e) {
            System.err.println("Error al extraer public_id de la URL: " + url + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    

    public void debugUrl(String url) {
        System.out.println("=== DEBUG URL ===");
        System.out.println("URL original: " + url);
        System.out.println("Public ID extraído: " + extractPublicIdFromUrl(url));
        System.out.println("================");
    }
}