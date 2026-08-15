package com.autoscaribe.service;

import com.autoscaribe.domain.ImagenVehiculo;
import com.autoscaribe.domain.Vehiculo;
import com.autoscaribe.repository.ImagenVehiculoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImagenVehiculoService {

    private final ImagenVehiculoRepository imagenVehiculoRepository;

    public ImagenVehiculoService(ImagenVehiculoRepository imagenVehiculoRepository) {
        this.imagenVehiculoRepository = imagenVehiculoRepository;
    }

    @Transactional(readOnly = true)
    public List<ImagenVehiculo> getImagenes(Integer idVehiculo) {
        return imagenVehiculoRepository.findByVehiculo_IdVehiculoOrderByOrdenAsc(idVehiculo);
    }

    @Transactional
    public void agregar(Vehiculo vehiculo, String rutaImagen) {
        long cantidadActual = imagenVehiculoRepository.countByVehiculo_IdVehiculo(vehiculo.getIdVehiculo());

        ImagenVehiculo imagen = new ImagenVehiculo();
        imagen.setVehiculo(vehiculo);
        imagen.setRutaImagen(rutaImagen.trim());
        imagen.setOrden((int) cantidadActual);

        imagenVehiculoRepository.save(imagen);
    }

    @Transactional
    public void eliminar(Integer idImagen) {
        if (!imagenVehiculoRepository.existsById(idImagen)) {
            throw new IllegalArgumentException("La imagen no existe");
        }
        imagenVehiculoRepository.deleteById(idImagen);
    }
}
