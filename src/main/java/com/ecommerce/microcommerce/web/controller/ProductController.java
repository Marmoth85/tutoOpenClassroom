package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDAO;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exception.ProductNotFoundException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductDAO productDAO;

    @RequestMapping(value="/Produits", method= RequestMethod.GET)
    public MappingJacksonValue listeProduits() {

        List<Product> products = productDAO.findAll();

        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listeFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);

        MappingJacksonValue produitsFiltres = new MappingJacksonValue(products);
        produitsFiltres.setFilters(listeFiltres);

        return produitsFiltres;
    }

    @GetMapping(value="/Produits/{id}")
    public MappingJacksonValue afficherUnProduit(@PathVariable int id){

        Product product = productDAO.findById(id);

        if (product == null) throw new ProductNotFoundException("Le produit dont l'identifiant est " + id + " est introuvable");

        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listeFiltre = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitFiltre = new MappingJacksonValue(product);
        produitFiltre.setFilters(listeFiltre);

        return produitFiltre;
    }

    /*
    @GetMapping(value="test/produits/prix/{prixLimite}")
    public List<Product> testDeRequete(@PathVariable int prixLimite) {
        // TODO utiliser le SimpleBeanPropertyFilter + FilterProvider + MappingJacksonValue -> factoriser
        return productDAO.findByPrixGreaterThan(prixLimite);
    }

    @GetMapping(value="test/produits/nom/{recherche}")
    public List<Product> testDeRequete(@PathVariable String recherche) {
        // TODO utiliser le SimpleBeanPropertyFilter + FilterProvider + MappingJacksonValue -> factoriser
        return productDAO.findByNomLike("%" + recherche + "%");
    }
    */

    @PostMapping(value="/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        Product productAdded = productDAO.save(product);

        if (productAdded == null) return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value="/Produits/{id}")
    public void supprimerProduit(@PathVariable int id) {
        productDAO.deleteById(id);
    }

    @PutMapping(value="/Produits")
    public void updateProduit(@Valid @RequestBody Product product) {
        productDAO.save(product);
    }
}
