package pe.authentique.inventario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.authentique.inventario.Repository.EntradaInventarioRepository;
import pe.authentique.inventario.Repository.ProductoRepository;
import pe.authentique.inventario.model.EntradaInventario;

@Service
public class EntradaInventarioService {

    @Autowired
    private EntradaInventarioRepository entradaInventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public void registrarEntrada(EntradaInventario entradaInventario) {
        entradaInventarioRepository.save(entradaInventario);
    }
}

